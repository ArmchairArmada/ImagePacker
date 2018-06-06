package net.natewm.imagepacker;

import net.natewm.imagepacker.rectpacker.BinarySubdividePacker;
import net.natewm.imagepacker.rectpacker.Rectangle;
import net.natewm.imagepacker.rectpacker.RectanglePacker;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Application implements AppListenable, ImageListener, OptionListener, ErrorListenable {
    private static Logger logger = Logger.getLogger(Application.class.getName());

    private List<AppListener> listeners = new ArrayList<>();
    private List<ErrorListener> errorListeners = new ArrayList<>();

    private Options options;
    private ImageManager imageManager;

    private Options outputOptions = null;
    private RectanglePacker.Results<PackableImage> outputResults = null;

    public Application(Options options, ImageManager imageManager) {
        this.options = options;
        this.imageManager = imageManager;
        logger.info("Using " + Runtime.getRuntime().availableProcessors() + " threads.");
    }

    @Override
    public void addAppListener(AppListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeAppListener(AppListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void onLoadImages(List<File> files) {
        ImageIO.setUseCache(false);

        files.forEach(file -> {
            Services.getExecutorService().submit(() -> {
                try {
                    final Image image = ImageIO.read(file);
                    if (image == null) throw new IOException();
                    final PackableImage packableImage = new PackableImage(image, file.getName());
                    imageLoaded(packableImage);
                } catch (IOException e) {
                    // TODO: Display error alert
                    errorLoadingImage(file);
                    //e.printStackTrace();
                }
            });
        });
    }

    @Override
    public void onAddImage(PackableImage image) {
        doLiveUpdate();
    }

    @Override
    public void onRemoveImage(PackableImage image) {
        doLiveUpdate();
    }

    @Override
    public void onRenameImage(PackableImage image) {
        // TODO: Figure out what to do about this.
    }

    // TODO: Move this into ImageManager and remove onPackImages from ImageListener?
    @Override
    public synchronized void onPackImages(List<PackableImage> images) {
        outputOptions = new Options(options);

        Services.getExecutorService().submit(() -> {
            RectanglePacker<PackableImage> packer = new BinarySubdividePacker<>();
            List<net.natewm.imagepacker.rectpacker.Rectangle<PackableImage>> rectangles;
            if (outputOptions.getTrimImages()) {
                rectangles = images.stream().map(i ->
                        new net.natewm.imagepacker.rectpacker.Rectangle<>(
                                i.trimmedWidth() + outputOptions.getPadding() * 2,
                                i.trimmedHeight() + outputOptions.getPadding() * 2,
                                i
                        )
                ).collect(Collectors.toList());
            }
            else {
                rectangles = images.stream().map(i ->
                        new net.natewm.imagepacker.rectpacker.Rectangle<>(
                                i.getImage().getWidth(null) + outputOptions.getPadding() * 2,
                                i.getImage().getHeight(null) + outputOptions.getPadding() * 2,
                                i
                        )
                ).collect(Collectors.toList());
            }
            RectanglePacker.Results<PackableImage> results = packer.pack(
                    rectangles,
                    outputOptions.getOutputWidth(),
                    outputOptions.getOutputHeight());

            BufferedImage outputImage = new BufferedImage(
                    outputOptions.getOutputWidth(),
                    outputOptions.getOutputHeight(),
                    BufferedImage.TYPE_4BYTE_ABGR);

            if (outputOptions.getTrimImages()) {
                for (Rectangle<PackableImage> rect : results.packed) {
                    // TODO: Outline option
                    // outputImage.getGraphics().drawRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
                    outputImage.getGraphics().drawImage(
                            rect.getContent().getImage(),
                            rect.getX() + outputOptions.getPadding(),
                            rect.getY() + outputOptions.getPadding(),
                            rect.getX() + outputOptions.getPadding() + rect.getContent().trimmedWidth(),
                            rect.getY() + outputOptions.getPadding() + rect.getContent().trimmedHeight(),
                            rect.getContent().getMinX(),
                            rect.getContent().getMinY(),
                            rect.getContent().getMaxX(),
                            rect.getContent().getMaxY(),
                            null);
                }
            }
            else {
                for (Rectangle<PackableImage> rect : results.packed) {
                    // TODO: Outline option
                    // outputImage.getGraphics().drawRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
                    outputImage.getGraphics().drawImage(
                            rect.getContent().getImage(),
                            rect.getX() + outputOptions.getPadding(),
                            rect.getY() + outputOptions.getPadding(),
                            null);
                }
            }

            packCompleted(outputImage, results);
        });
    }

    @Override
    public void onSetOutputImage(Image outputImage) {
    }

    private void imageLoaded(PackableImage packableImage) {
        imageManager.addImage(packableImage);
    }

    private synchronized void errorLoadingImage(File file) {
        for (int i=errorListeners.size()-1; i>=0; i--)
            errorListeners.get(i).onErrorLoadingImage(file);
    }

    private synchronized void packCompleted(Image outputImage, RectanglePacker.Results<PackableImage> results) {
        outputResults = results;
        imageManager.setOutputImage(outputImage);
        for (int i=listeners.size()-1; i>=0; i--)
            listeners.get(i).onPackCompleted(outputImage, results);
    }

    private void doLiveUpdate() {
        if (options.getLiveUpdate())
            onPackImages(imageManager.getImages());
    }

    public void saveRectangles(File file) {
        if (outputOptions == null) {
            // TODO: Error logging and alert
            return;
        }

        try {
            PrintWriter writer = new PrintWriter(file);
            writer.println("{");
            writer.println("    \"image\": \"" + outputOptions.getOutputName() + "\",");
            writer.println("    \"type\": \"map\",");
            writer.println("    \"regions\": [");

            for (int i=0; i<outputResults.packed.size(); i++) {
                Rectangle<PackableImage> rectangle = outputResults.packed.get(i);
                writer.println("        {");
                writer.println("            \"id\": \"" + rectangle.getContent().getName() + "\",");
                writer.println("            \"x\": " + (rectangle.getX() + outputOptions.getPadding()) + ",");
                writer.println("            \"y\": " + (rectangle.getY() + outputOptions.getPadding()) + ",");
                writer.println("            \"width\": " + (rectangle.getWidth() - outputOptions.getPadding() * 2) + ",");
                writer.println("            \"height\": " + (rectangle.getHeight() - outputOptions.getPadding() * 2) + ",");
                writer.println("            \"offsetX\": " + rectangle.getContent().getMinX() + ", ");
                writer.println("            \"offsetY\": " + rectangle.getContent().getMinY());
                if (i < outputResults.packed.size()-1)
                    writer.println("        },");
                else
                    writer.println("        }");
            }

            writer.println("    ]");
            writer.println("}");

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTrimImagesChange(boolean value) {
        doLiveUpdate();
    }

    @Override
    public void onExtendEdgesChange(boolean value) {
        doLiveUpdate();
    }

    @Override
    public void onLiveUpdateChange(boolean value) {
    }

    @Override
    public void onOutputNameChange(String name) {
        doLiveUpdate();
    }

    @Override
    public void onOutputSizeChange(int width, int height) {
        doLiveUpdate();
    }

    @Override
    public void onPaddingChange(int value) {
        doLiveUpdate();
    }

    @Override
    public void addErrorListener(ErrorListener listener) {
        errorListeners.add(listener);
    }

    @Override
    public void removeErrorListener(ErrorListener listener) {
        errorListeners.remove(listener);
    }
}

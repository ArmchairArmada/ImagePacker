package net.natewm.imagepacker;

import net.natewm.imagepacker.rectpacker.BinarySubdividePacker;
import net.natewm.imagepacker.rectpacker.Rectangle;
import net.natewm.imagepacker.rectpacker.RectanglePacker;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Application implements AppListenable, ImageListener, OptionListener, ErrorListenable {
    private static Logger logger = Logger.getLogger(Application.class.getName());

    private List<AppListener> listeners = new ArrayList<>();
    private List<ErrorListener> errorListeners = new ArrayList<>();

    private ExecutorService executorService;
    private Options options;
    private ImageManager imageManager;

    public Application(Options options, ImageManager imageManager) {
        this.options = options;
        this.imageManager = imageManager;
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
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
        // TODO: Job queue with worker threads for loading images.
        ImageIO.setUseCache(false);

        files.forEach(file -> {
            executorService.submit(() -> {
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
        // TODO: Need to keep a list of images if I am doing live updating.
        doLiveUpdate();
    }

    @Override
    public void onRenameImage(PackableImage image) {
        // TODO: Figure out what to do about this.
    }

    @Override
    public synchronized void onPackImages(List<PackableImage> images) {
        int outputWidth = options.getOutputWidth();
        int outputHeight = options.getOutputHeight();
        int padding = options.getPadding();

        executorService.submit(() -> {
            RectanglePacker<PackableImage> packer = new BinarySubdividePacker<>();
            List<net.natewm.imagepacker.rectpacker.Rectangle<PackableImage>> rectangles = images.stream().map(i ->
                    new net.natewm.imagepacker.rectpacker.Rectangle<>(
                            i.getImage().getWidth(null) + padding*2,
                            i.getImage().getHeight(null) + padding*2,
                            i
                    )
            ).collect(Collectors.toList());
            RectanglePacker.Results<PackableImage> results = packer.pack(rectangles, outputWidth, outputHeight);

            BufferedImage outputImage = new BufferedImage(outputWidth, outputHeight, BufferedImage.TYPE_4BYTE_ABGR);
            for (Rectangle<PackableImage> rect : results.packed) {
                // TODO: Outline option
                // outputImage.getGraphics().drawRect(rect.getX() + 1, rect.getY() + 1, rect.getWidth(), rect.getHeight());
                outputImage.getGraphics().drawImage(
                        rect.getContent().getImage(),
                        rect.getX() + padding + 1,
                        rect.getY() + padding + 1,
                        null);
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
        imageManager.setOutputImage(outputImage);
        // TODO: Store results somewhere.
        for (int i=listeners.size()-1; i>=0; i--)
            listeners.get(i).onPackCompleted(outputImage, results);
    }

    private void doLiveUpdate() {
        if (options.getLiveUpdate())
            onPackImages(imageManager.getImages());
    }

    @Override
    public void onTrimImagesChange(boolean value) {
        // TODO: if value==true, trim images edges.
        // TODO: if live update, repack
        doLiveUpdate();
    }

    @Override
    public void onExtendEdgesChange(boolean value) {
        // TODO: if value==true, extend edges.
        // TODO: if live update, repack
        doLiveUpdate();
    }

    @Override
    public void onLiveUpdateChange(boolean value) {
    }

    @Override
    public void onOutputSizeChange(int width, int height) {
        // TODO: If live update, repack.
        doLiveUpdate();
    }

    @Override
    public void onPaddingChange(int value) {
        // TODO: If live update, repack.
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

package net.natewm.imagepacker;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageManager implements ImageListenable {
    private List<PackableImage> images = new ArrayList<>();
    private List<ImageListener> listeners = new ArrayList<>();
    private Image outputImage;

    public ImageManager() {
    }

    public List<PackableImage> getImages() {
        return images;
    }

    @Override
    public synchronized void addImageListener(ImageListener listener) {
        listeners.add(listener);
    }

    @Override
    public synchronized void removeImageListener(ImageListener listener) {
        listeners.remove(listener);
    }

    public synchronized void addImage(PackableImage image) {
        images.add(image);
        for (int i=listeners.size()-1; i>=0; i--)
            listeners.get(i).onAddImage(image);
    }

    public synchronized void removeImage(PackableImage image) {
        images.remove(image);
        for (int i=listeners.size()-1; i>=0; i--)
            listeners.get(i).onRemoveImage(image);
    }

    public synchronized void renameImage(PackableImage image, String name) {
        image.setName(name);
        for (int i=listeners.size()-1; i>=0; i--)
            listeners.get(i).onRenameImage(image);
    }

    public synchronized void packImages() {
        for (int i=listeners.size()-1; i>=0; i--)
            listeners.get(i).onPackImages(images);
    }

    public synchronized void setOutputImage(Image image) {
        outputImage = image;
        for (int i=listeners.size()-1; i>=0; i--)
            listeners.get(i).onSetOutputImage(outputImage);
    }

    public Image getOutputImage() {
        return outputImage;
    }

    public void saveOutputImage(File file) {
        try {
            //ImageIO.write((RenderedImage) outputImage, "PNG", file);
            String format = file.getName().substring(file.getName().lastIndexOf('.')+1).toUpperCase();
            ImageIO.write((RenderedImage) outputImage, format, file);
        } catch (IOException e) {
            // TODO: Logging
            e.printStackTrace();
        }
    }
}

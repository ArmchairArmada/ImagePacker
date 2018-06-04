package net.natewm.imagepacker;

import java.awt.*;

public class PackableImage {
    private static final int THUMBNAIL_SIZE = 32;

    private final Image image;
    private Image modifiedImage = null;  // Trimmed and/or extended
    private Image thumbnail;
    private String name;

    public PackableImage(Image image, String name) {
        this.image = image;
        this.name = name;
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        if (width > height) {
            thumbnail = image.getScaledInstance(THUMBNAIL_SIZE, THUMBNAIL_SIZE * height / width, Image.SCALE_SMOOTH);
        }
        else {
            thumbnail = image.getScaledInstance(THUMBNAIL_SIZE * width / height, THUMBNAIL_SIZE, Image.SCALE_SMOOTH);
        }
    }

    public Image getImage() {
        return image;
    }

    public Image getThumbnail() {
        return thumbnail;
    }

    public Image getModifiedImage() {
        return modifiedImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        name = value;
    }

    public void makeModifiedImage() {
        // TODO: Generate trimmed image
    }
}

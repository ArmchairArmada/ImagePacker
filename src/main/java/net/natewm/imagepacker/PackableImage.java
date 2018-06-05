package net.natewm.imagepacker;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PackableImage {
    private static final int THUMBNAIL_SIZE = 32;

    private final Image image;
    private Image modifiedImage = null;  // Trimmed and/or extended
    private Image thumbnail;
    private String name;
    // Used for trimming transparency:
    private int minX;
    private int maxX;
    private int minY;
    private int maxY;

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
        calculateTrim();
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

    public void calculateTrim() {
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        BufferedImage buffer = (BufferedImage)image;
        int c;
        minX = Integer.MAX_VALUE;
        maxX = 0;
        minY = Integer.MAX_VALUE;
        maxY = 0;
        for (int j=0; j<height; j++) {
            for (int i=0; i<width; i++) {
                c = buffer.getRGB(i, j);
                if ((c & 0xFF000000) != 0) {
                    minX = Math.min(minX, i);
                    maxX = Math.max(maxX, i);
                    minY = Math.min(minY, j);
                    maxY = Math.max(maxY, j);
                }
            }
        }
        maxX++;
        maxY++;
    }

    public int getMinX() {
        return minX;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMinY() {
        return minY;
    }

    public int getMaxY() {
        return maxY;
    }

    public int trimmedWidth() {
        return maxX - minX;
    }

    public int trimmedHeight() {
        return maxY - minY;
    }
}

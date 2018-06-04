package net.natewm.imagepacker;

import net.natewm.imagepacker.rectpacker.RectanglePacker;

import java.awt.*;

public interface AppListener {
    void onImageLoaded(PackableImage image);
    void onPackCompleted(Image packedImage, RectanglePacker.Results<PackableImage> results);
}

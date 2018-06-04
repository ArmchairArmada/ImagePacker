package net.natewm.imagepacker;

import java.awt.*;
import java.io.File;
import java.util.List;

public interface ImageListener {
    void onLoadImages(List<File> files);
    void onAddImage(PackableImage image);
    void onRemoveImage(PackableImage image);
    void onRenameImage(PackableImage image);
    void onPackImages(List<PackableImage> images);
    void onSetOutputImage(Image outputImage);
}

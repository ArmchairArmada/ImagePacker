package net.natewm.imagepacker;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ImageLoader {
    private ImageManager imageManager;
    private ErrorHandler errorHandler;

    public ImageLoader(ImageManager imageManager, ErrorHandler errorHandler) {
        this.imageManager = imageManager;
        this.errorHandler = errorHandler;
    }

    public void loadImages(List<File> files) {
        ImageIO.setUseCache(false);

        files.forEach(file -> {
            Services.getExecutorService().submit(() -> {
                try {
                    final Image image = ImageIO.read(file);
                    if (image == null) throw new IOException();
                    final PackableImage packableImage = new PackableImage(image, file.getName());
                    imageManager.addImage(packableImage);
                } catch (IOException e) {
                    // TODO: Display error alert
                    errorHandler.errorLoadingImage(file);
                    //e.printStackTrace();
                }
            });
        });
    }
}

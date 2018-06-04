package net.natewm.imagepacker.gui;

import net.natewm.imagepacker.ImageListener;
import net.natewm.imagepacker.ImageManager;
import net.natewm.imagepacker.PackableImage;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageListPanel extends JPanel implements ImageListener {
    private final ImageManager imageManager;
    private final JPanel listPanel;
    private final JScrollPane scrollPane;

    public ImageListPanel(ImageManager imageManager) {
        this.imageManager = imageManager;
        setLayout(new BorderLayout());

        listPanel = new JPanel();
        scrollPane = new JScrollPane(listPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        scrollPane.getVerticalScrollBar().setUnitIncrement(32);

        BoxLayout layout = new BoxLayout(listPanel, BoxLayout.PAGE_AXIS);
        listPanel.setLayout(layout);

        imageManager.addImageListener(this);

        add(scrollPane, BorderLayout.CENTER);
    }

    public void addImage(PackableImage image) {
        addImageItem(new ImageItemPanel(this, image));
    }

    public void loadImages(List<File> files) {
        imageManager.loadImages(files);
    }

    public void addImageItem(ImageItemPanel imageItemPanel) {
        listPanel.add(imageItemPanel);
        scrollPane.setMinimumSize(scrollPane.getPreferredSize());
        revalidate();
        repaint();
    }

    public void removeImageItem(ImageItemPanel imageItemPanel) {
        listPanel.remove(imageItemPanel);
        scrollPane.setMinimumSize(scrollPane.getPreferredSize());
        revalidate();
        repaint();
        imageRemove(imageItemPanel.getImage());
    }

    public void packImages() {
        List<PackableImage> images = new ArrayList<>();
        for (Component component : listPanel.getComponents()) {
            images.add(((ImageItemPanel)component).getImage());
        }
        packImages(images);
    }

    private void imageRemove(PackableImage image) {
        imageManager.removeImage(image);
    }

    private void packImages(List<PackableImage> images) {
        imageManager.packImages();
    }

    @Override
    public void onLoadImages(List<File> files) {
    }

    @Override
    public void onAddImage(PackableImage image) {
        SwingUtilities.invokeLater(() -> {
            addImageItem(new ImageItemPanel(this, image));
        });
    }

    @Override
    public void onRemoveImage(PackableImage image) {
        SwingUtilities.invokeLater(() -> {
            for (Component imageItemPanel : listPanel.getComponents()) {
                if (((ImageItemPanel)imageItemPanel).getImage() == image) {
                    listPanel.remove(imageItemPanel);
                    scrollPane.setMinimumSize(scrollPane.getPreferredSize());
                    revalidate();
                    repaint();
                }
            }
        });
    }

    @Override
    public void onRenameImage(PackableImage image) {

    }

    @Override
    public void onPackImages(List<PackableImage> images) {
    }

    @Override
    public void onSetOutputImage(Image outputImage) {
    }
}

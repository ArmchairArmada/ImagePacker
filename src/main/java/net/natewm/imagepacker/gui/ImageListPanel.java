package net.natewm.imagepacker.gui;

import net.natewm.imagepacker.ImageListener;
import net.natewm.imagepacker.ImageManager;
import net.natewm.imagepacker.PackableImage;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.io.File;
import java.util.List;

/**
 * Lists panels of images that have been loaded into the application.
 */
public class ImageListPanel extends JPanel implements ImageListener {
    private final ImageManager imageManager;
    private final JPanel listPanel;
    private final JScrollPane scrollPane;

    /**
     * Constructs the panel for listing images.
     *
     * @param imageManager Manages the images used by the application.
     */
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

    /**
     * When an image is added, an image item panel will be created and added for it.
     *
     * @param image Image to add to the list.
     */
    public void addImage(PackableImage image) {
        addImageItem(new ImageItemPanel(this, image));
    }

    /**
     * Adds an image item to the image list panel.
     *
     * @param imageItemPanel Image item to add.
     */
    private void addImageItem(ImageItemPanel imageItemPanel) {
        listPanel.add(imageItemPanel);
        scrollPane.setMinimumSize(scrollPane.getPreferredSize());
        revalidate();
        repaint();
    }

    /**
     * Removes an image item panel from this list panel.
     *
     * @param imageItemPanel Image item panel to remove.
     */
    public void removeImageItem(ImageItemPanel imageItemPanel) {
        listPanel.remove(imageItemPanel);
        scrollPane.setMinimumSize(scrollPane.getPreferredSize());
        revalidate();
        repaint();
        imageManager.removeImage(imageItemPanel.getImage());
    }

    /**
     * Called when images are loaded.
     *
     * @param files List of images that have loaded.
     */
    @Override
    public void onLoadImages(List<File> files) {
    }

    /**
     * Called when an image has been added to the application.
     *
     * @param image Image that has been added.
     */
    @Override
    public void onAddImage(PackableImage image) {
        SwingUtilities.invokeLater(() -> {
            addImageItem(new ImageItemPanel(this, image));
        });
    }

    /**
     * Called when an image has been removed from the application.
     *
     * @param image Image that has been removed.
     */
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

    /**
     * Called when an image has been renamed.
     *
     * @param image Image that has been renamed.
     */
    @Override
    public void onRenameImage(PackableImage image) {
    }

    /**
     * Called when images have been packed.
     *
     * @param images List of images that have been packed.
     */
    @Override
    public void onPackImages(List<PackableImage> images) {
    }

    /**
     * Called when the output image has changed in the ImageManager.
     *
     * @param outputImage Image the output image has been changed to.
     */
    @Override
    public void onSetOutputImage(Image outputImage) {
    }
}

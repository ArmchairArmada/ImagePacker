package net.natewm.imagepacker.gui;

import net.natewm.imagepacker.ImageListener;
import net.natewm.imagepacker.PackableImage;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.io.File;
import java.util.List;

public class ImagePreview extends JPanel implements ImageListener {
    private JPanel previewPanel;
    private JLabel imageLabel;

    public ImagePreview() {
        setLayout(new BorderLayout());

        previewPanel = new JPanel();
        previewPanel.setPreferredSize(new Dimension(542, 542));
        previewPanel.setBackground(Color.DARK_GRAY);
        JScrollPane scrollPane = new JScrollPane(previewPanel);
        scrollPane.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        scrollPane.getVerticalScrollBar().setUnitIncrement(32);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(32);

        previewPanel.setLayout(new GridBagLayout());
        imageLabel = new JLabel();
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        //imageLabel.setPreferredSize(new Dimension(512, 512));
        previewPanel.add(imageLabel);

        add(scrollPane, BorderLayout.CENTER);
    }

    public void setImage(Image image) {
        imageLabel.setIcon(new ImageIcon(image));
        previewPanel.setPreferredSize(new Dimension(image.getWidth(null), image.getHeight(null)));
    }

    @Override
    public void onLoadImages(List<File> files) {
    }

    @Override
    public void onAddImage(PackableImage image) {
    }

    @Override
    public void onRemoveImage(PackableImage image) {
    }

    @Override
    public void onRenameImage(PackableImage image) {
    }

    @Override
    public void onPackImages(List<PackableImage> images) {
    }

    @Override
    public void onSetOutputImage(Image outputImage) {
        SwingUtilities.invokeLater(() -> {
            setImage(outputImage);
        });
    }
}

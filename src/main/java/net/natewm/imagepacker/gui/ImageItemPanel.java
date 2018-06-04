package net.natewm.imagepacker.gui;

import net.natewm.imagepacker.PackableImage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ImageItemPanel extends JPanel {
    private final ImageListPanel imageListPanel;
    private final PackableImage image;

    private static JLabel imageLabel;
    private static JButton removeButton;

    public ImageItemPanel(ImageListPanel imageListPanel, PackableImage image) {
        this.imageListPanel = imageListPanel;
        this.image = image;

        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);
        GridBagConstraints constraints = new GridBagConstraints();

        setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        setBackground(Color.WHITE);
        /*
        if (Math.random() < 0.2)
            setBackground(Color.RED);
        else
            setBackground(Color.WHITE);
        */

        JLabel thumbnail = new JLabel(new ImageIcon(image.getThumbnail()));
        //thumbnail.setBackground(new Color((float)Math.random(), (float)Math.random(), (float)Math.random()));
        thumbnail.setPreferredSize(new Dimension(32, 32));
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 0;
        constraints.weighty = 0;
        constraints.insets = new Insets(5, 5, 5, 5);
        add(thumbnail, constraints);

        imageLabel = new JLabel(image.getName());
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 1;
        constraints.weighty = 0;
        constraints.insets = new Insets(0,0,0,0);
        add(imageLabel, constraints);

        // TODO: Replace X with an icon
        removeButton = new JButton("X");
        removeButton.setToolTipText("Remove this image.");
        removeButton.setMargin(new Insets(0,5,0,5));
        removeButton.setBackground(getBackground());
        removeButton.addActionListener((ActionEvent e) -> {
            imageListPanel.removeImageItem(this);
        });
        constraints.gridx = 2;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 0;
        constraints.weighty = 0;
        constraints.insets = new Insets(0, 5, 0, 5);
        add(removeButton, constraints);

        setMaximumSize(new Dimension(100000, getPreferredSize().height));
    }

    public PackableImage getImage() {
        return image;
    }
}

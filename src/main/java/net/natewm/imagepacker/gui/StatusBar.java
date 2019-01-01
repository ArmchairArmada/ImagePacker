package net.natewm.imagepacker.gui;

import net.natewm.imagepacker.AppListenable;
import net.natewm.imagepacker.AppListener;
import net.natewm.imagepacker.PackableImage;
import net.natewm.imagepacker.rectpacker.RectanglePacker;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

public class StatusBar extends JPanel implements AppListener {
    private JLabel label;

    public StatusBar(AppListenable application) {
        setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        label = new JLabel();
        add(label);

        application.addAppListener(this);
    }

    @Override
    public void onImageLoaded(PackableImage image) {
    }

    @Override
    public void onPackCompleted(Image packedImage, RectanglePacker.Results<PackableImage> results) {
        SwingUtilities.invokeLater(() -> {
            label.setText(
                    "Image Count: " + (results.packed.size() + results.notPacked.size()) + ", " +
                    "Packed: " + results.packed.size() + ", " +
                    "Not Packed: " + results.notPacked.size()
            );
        });
    }
}

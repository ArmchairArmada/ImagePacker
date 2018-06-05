package net.natewm.imagepacker.gui;

import net.natewm.imagepacker.ImageManager;
import net.natewm.imagepacker.Options;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.logging.Logger;

public class ControlsPanel extends JPanel {
    private static Logger logger = Logger.getLogger(ControlsPanel.class.getName());

    private final Options options;
    private final ImageManager imageManager;

    private final JCheckBox trimImages;
    private final JCheckBox extendEdges;
    private final JCheckBox liveUpdate;

    private final JSpinner widthSpinner;
    private final JSpinner heightSpinner;
    private final JSpinner paddingSpinner;

    public ControlsPanel(Options options, ImageManager imageManager) {
        // TODO: Add "remove file extension" option.

        this.options = options;
        this.imageManager = imageManager;

        JLabel tmpLabel;

        setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);
        GridBagConstraints constraints = new GridBagConstraints();

        trimImages = new JCheckBox("Trim Images");
        trimImages.setToolTipText("Trims away transparent areas from images.");
        trimImages.addActionListener((ActionEvent e) -> {
            logger.info("Trim Images: " + trimImages.isSelected());
            options.setTrimImages(trimImages.isSelected());
        });
        trimImages.setAlignmentX(Component.LEFT_ALIGNMENT);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        add(trimImages, constraints);

        extendEdges = new JCheckBox("Extend Edges");
        extendEdges.setToolTipText("Extends the edge pixels into the padding.");
        extendEdges.addActionListener((ActionEvent e) -> {
            logger.info("Extend Padding: " + extendEdges.isSelected());
            options.setExtendEdges(extendEdges.isSelected());
        });
        extendEdges.setAlignmentX(Component.LEFT_ALIGNMENT);
        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        add(extendEdges, constraints);

        liveUpdate = new JCheckBox("Live Update");
        liveUpdate.setToolTipText("Updates image packing as soon as a change is made.");
        liveUpdate.addActionListener((ActionEvent e) -> {
            logger.info("Live Update: " + liveUpdate.isSelected());
            options.setLiveUpdate(liveUpdate.isSelected());
        });
        liveUpdate.setAlignmentX(Component.LEFT_ALIGNMENT);
        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        add(liveUpdate, constraints);

        tmpLabel = new JLabel("Output Name: ");
        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(5,5,5,5);
        add(tmpLabel, constraints);

        JTextField outputName = new JTextField();
        outputName.setToolTipText("Name of output image in JSON file.");
        outputName.addActionListener(e -> {
            options.setOutputName(outputName.getText());
        });

        outputName.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                options.setOutputName(outputName.getText());
            }
        });

        constraints.gridx = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        add(outputName, constraints);

        tmpLabel = new JLabel("Output Width: ");
        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(5,5,5,5);
        add(tmpLabel, constraints);

        widthSpinner = new JSpinner();
        widthSpinner.setModel(new SpinnerNumberModel(512, 0, 65536, 1));
        widthSpinner.setToolTipText("Width of packing area.");

        constraints.gridx = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        add(widthSpinner, constraints);

        tmpLabel = new JLabel("Output Height: ");
        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        add(tmpLabel, constraints);

        heightSpinner = new JSpinner();
        heightSpinner.setModel(new SpinnerNumberModel(512, 0, 65536, 1));
        heightSpinner.setToolTipText("Height of packing area.");
        constraints.gridx = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        add(heightSpinner, constraints);

        widthSpinner.addChangeListener((ChangeEvent e) -> {
            options.setOutputWidth((Integer)widthSpinner.getValue());
        });

        heightSpinner.addChangeListener((ChangeEvent e) -> {
            options.setOutputHeight((Integer)heightSpinner.getValue());
        });

        tmpLabel = new JLabel("Padding: ");
        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        add(tmpLabel, constraints);

        paddingSpinner = new JSpinner();
        paddingSpinner.setModel(new SpinnerNumberModel(0, 0, 65536, 1));
        paddingSpinner.setToolTipText("Padding to add around packed images.");
        paddingSpinner.addChangeListener((ChangeEvent e) -> {
            options.setPadding((Integer)paddingSpinner.getValue());
        });
        constraints.gridx = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        add(paddingSpinner, constraints);

        JButton packButton = new JButton("Pack Images");
        packButton.addActionListener(e -> {
            imageManager.packImages();
        });
        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        add(packButton, constraints);
    }
}

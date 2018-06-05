package net.natewm.imagepacker.gui;

import net.natewm.imagepacker.*;
import net.natewm.imagepacker.rectpacker.RectanglePacker;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Arrays;
import java.util.logging.Logger;

public class ApplicationWindow extends JFrame implements
        AppListener, ErrorListener {
    private final Logger logger = Logger.getLogger(ApplicationWindow.class.getName());

    private final ControlsPanel controlsPanel;
    private final ImageListPanel imageListPanel;
    private final ImagePreview imagePreview;

    private final JFileChooser fileChooser = new JFileChooser();
    private final JFileChooser fileSaveChooser = new JFileChooser();
    private final JFileChooser fileJsonSaveChooser = new JFileChooser();
    private Icon errorIcon = UIManager.getIcon("OptionPane.errorIcon");

    public ApplicationWindow(Options options, ImageManager imageManager, Application application) {
        setTitle("Image Packer");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        fileChooser.setFileFilter(new FileNameExtensionFilter("Images", "jpg", "png", "gif", "bmp"));
        fileChooser.setMultiSelectionEnabled(true);

        fileSaveChooser.setFileFilter(new FileNameExtensionFilter("PNG", "png"));
        fileJsonSaveChooser.setFileFilter(new FileNameExtensionFilter("JSON", "json"));

        GridBagConstraints constraints = new GridBagConstraints();
        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);

        // Menu Bar
        final JMenuBar menuBar = new JMenuBar();
        final JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(fileMenu);

        final JMenuItem savePackedImage = new JMenuItem("Save Packed Image");
        savePackedImage.setMnemonic(KeyEvent.VK_S);
        savePackedImage.addActionListener(e -> {
            int retval = fileSaveChooser.showSaveDialog(this);
            if (retval == JFileChooser.APPROVE_OPTION) {
                imageManager.saveOutputImage(fileSaveChooser.getSelectedFile());
            }
        });
        fileMenu.add(savePackedImage);

        final JMenuItem saveRectangles = new JMenuItem("Save Rectangles File");
        saveRectangles.setMnemonic(KeyEvent.VK_S);
        saveRectangles.addActionListener(e -> {
            int retval = fileJsonSaveChooser.showSaveDialog(this);
            if (retval == JFileChooser.APPROVE_OPTION) {
                application.saveRectangles(fileJsonSaveChooser.getSelectedFile());
            }
        });
        fileMenu.add(saveRectangles);

        fileMenu.addSeparator();

        final JMenuItem exitMenu = new JMenuItem("Exit");
        exitMenu.setMnemonic(KeyEvent.VK_X);
        exitMenu.addActionListener(e -> {
            System.exit(0);
        });
        fileMenu.add(exitMenu);

        final JMenu imageMenu = new JMenu("Images");
        imageMenu.setMnemonic(KeyEvent.VK_I);
        menuBar.add(imageMenu);

        final JMenuItem loadImagesMenu = new JMenuItem("Load Images");
        loadImagesMenu.setMnemonic(KeyEvent.VK_L);
        imageMenu.add(loadImagesMenu);

        final JMenu packMenu = new JMenu("Pack");
        packMenu.setMnemonic(KeyEvent.VK_P);
        menuBar.add(packMenu);

        final JMenuItem doPackMenu = new JMenuItem("Pack Images");
        doPackMenu.setMnemonic(KeyEvent.VK_I);
        packMenu.add(doPackMenu);

        setJMenuBar(menuBar);

        imageListPanel = new ImageListPanel(imageManager);
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 0;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;
        add(imageListPanel, constraints);

        // Panels
        controlsPanel = new ControlsPanel(options, imageManager);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 0;
        constraints.weighty = 0;
        constraints.fill = GridBagConstraints.BOTH;
        add(controlsPanel, constraints);

        imagePreview = new ImagePreview();
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 2;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;
        add(imagePreview, constraints);
        imageManager.addImageListener(imagePreview);

        // TODO: Fill out more status information
        StatusBar statusBar = new StatusBar(application);
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        constraints.weightx = 1;
        constraints.weighty = 0;
        constraints.fill = GridBagConstraints.BOTH;
        add(statusBar, constraints);

        loadImagesMenu.addActionListener(listener -> {
            int retval = fileChooser.showOpenDialog(this);
            if (retval == JFileChooser.APPROVE_OPTION) {
                imageListPanel.loadImages(Arrays.asList(fileChooser.getSelectedFiles()));
            }
        });

        doPackMenu.addActionListener(listener -> {
            imageManager.packImages();
        });

        pack();
        setLocationRelativeTo(null);
    }

    @Override
    public void onImageLoaded(PackableImage image) {
        SwingUtilities.invokeLater(() -> {
            imageListPanel.addImage(image);
        });
    }

    @Override
    public void onPackCompleted(Image packedImage, RectanglePacker.Results<PackableImage> results) {
    }

    @Override
    public void onErrorLoadingImage(File file) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load file as image: " + file.getName(),
                    "Error",
                    JOptionPane.PLAIN_MESSAGE,
                    errorIcon);
        });
    }
}

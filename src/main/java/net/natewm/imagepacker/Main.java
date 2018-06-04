package net.natewm.imagepacker;

import net.natewm.imagepacker.gui.ApplicationWindow;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

public class Main {
    private static Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        logger.info("Starting application.");

        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            Options options = new Options();
            ImageManager imageManager = new ImageManager();
            Application app = new Application(options, imageManager);
            ApplicationWindow win = new ApplicationWindow(options, imageManager, app);

            imageManager.addImageListener(app);
            app.addAppListener(win);
            app.addErrorListener(win);
            options.addOptionListener(app);

            win.setVisible(true);
        });
    }
}

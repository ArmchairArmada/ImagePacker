package net.natewm.imagepacker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ErrorHandler implements ErrorListenable {
    private List<ErrorListener> listeners = new ArrayList<>();

    @Override
    public synchronized void addErrorListener(ErrorListener listener) {
        listeners.add(listener);
    }

    @Override
    public synchronized void removeErrorListener(ErrorListener listener) {
        listeners.remove(listener);
    }

    public synchronized void errorLoadingImage(File file) {
        for (int i=listeners.size()-1; i>=0; i--)
            listeners.get(i).onErrorLoadingImage(file);
    }
}

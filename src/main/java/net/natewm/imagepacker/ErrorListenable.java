package net.natewm.imagepacker;

public interface ErrorListenable {
    void addErrorListener(ErrorListener listener);
    void removeErrorListener(ErrorListener listener);
}

package net.natewm.imagepacker;

import java.io.File;

public interface ErrorListener {
    void onErrorLoadingImage(File file);
}

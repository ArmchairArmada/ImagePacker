package net.natewm.imagepacker;

public interface AppListenable {
    void addAppListener(AppListener listener);
    void removeAppListener(AppListener listener);
}

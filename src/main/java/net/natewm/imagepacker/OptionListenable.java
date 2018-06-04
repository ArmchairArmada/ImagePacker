package net.natewm.imagepacker;

public interface OptionListenable {
    void addOptionListener(OptionListener listener);
    void removeOptionListener(OptionListener listener);
}

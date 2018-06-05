package net.natewm.imagepacker;

public interface OptionListener {
    void onTrimImagesChange(boolean value);
    void onExtendEdgesChange(boolean value);
    void onLiveUpdateChange(boolean value);
    void onOutputNameChange(String name);
    void onOutputSizeChange(int width, int height);
    void onPaddingChange(int value);
}

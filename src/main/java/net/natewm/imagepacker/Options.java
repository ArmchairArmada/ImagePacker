package net.natewm.imagepacker;

import java.util.ArrayList;
import java.util.List;

public class Options implements OptionListenable {
    private List<OptionListener> listeners = new ArrayList<>();

    private boolean trimImages = false;
    private boolean extendEdges = false;
    private boolean liveUpdate = false;
    private String outputName = "";
    private int outputWidth = 512;
    private int outputHeight = 512;
    private int padding = 0;

    public Options() {
    }

    public Options(Options other) {
        trimImages = other.trimImages;
        extendEdges = other.extendEdges;
        liveUpdate = other.liveUpdate;
        outputName = other.outputName;
        outputWidth = other.outputWidth;
        outputHeight = other.outputHeight;
        padding = other.padding;
    }

    public boolean getTrimImages() {
        return trimImages;
    }

    public synchronized void setTrimImages(boolean value) {
        trimImages = value;
        for (int i=listeners.size()-1; i>=0; i--)
            listeners.get(i).onTrimImagesChange(trimImages);
    }

    public boolean getExtendEdges() {
        return extendEdges;
    }

    public synchronized void setExtendEdges(boolean value) {
        extendEdges = value;
        for (int i=listeners.size()-1; i>=0; i--)
            listeners.get(i).onExtendEdgesChange(extendEdges);
    }

    public boolean getLiveUpdate() {
        return liveUpdate;
    }

    public synchronized void setLiveUpdate(boolean value) {
        liveUpdate = value;
        for (int i=listeners.size()-1; i>=0; i--)
            listeners.get(i).onLiveUpdateChange(liveUpdate);
    }

    public String getOutputName() {
        return outputName;
    }

    public synchronized void setOutputName(String value) {
        outputName = value;
        for (int i=listeners.size()-1; i>=0; i--)
            listeners.get(i).onOutputNameChange(outputName);
    }

    public int getOutputWidth() {
        return outputWidth;
    }

    public synchronized void setOutputWidth(int value) {
        outputWidth = value;
        for (int i=listeners.size()-1; i>=0; i--)
            listeners.get(i).onOutputSizeChange(outputWidth, outputHeight);
    }

    public int getOutputHeight() {
        return outputHeight;
    }

    public synchronized void setOutputHeight(int value) {
        outputHeight = value;
        for (int i=listeners.size()-1; i>=0; i--)
            listeners.get(i).onOutputSizeChange(outputWidth, outputHeight);
    }

    public int getPadding() {
        return padding;
    }

    public synchronized void setPadding(int value) {
        padding = value;
        for (int i=listeners.size()-1; i>=0; i--)
            listeners.get(i).onPaddingChange(padding);
    }

    @Override
    public synchronized void addOptionListener(OptionListener listener) {
        listeners.add(listener);
    }

    @Override
    public synchronized void removeOptionListener(OptionListener listener) {
        listeners.remove(listener);
    }
}

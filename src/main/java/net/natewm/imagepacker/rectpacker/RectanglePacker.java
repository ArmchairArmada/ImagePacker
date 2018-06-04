package net.natewm.imagepacker.rectpacker;

import java.util.ArrayList;
import java.util.List;

public interface RectanglePacker<T> {
    public class Results<T> {
        public List<Rectangle<T>> packed = new ArrayList<>();
        public List<Rectangle<T>> notPacked = new ArrayList<>();
        public int waste = 0;
    }

    Results<T> pack(List<Rectangle<T>> rectangles, int width, int height);
}

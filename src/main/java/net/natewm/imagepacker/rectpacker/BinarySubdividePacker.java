package net.natewm.imagepacker.rectpacker;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class BinarySubdividePacker<T> implements RectanglePacker<T> {
    @Override
    public Results<T> pack(List<Rectangle<T>> rectangles, int width, int height) {
        Results<T> results = new Results<>();

        List<Rectangle<T>> rectsW = new ArrayList<>(rectangles);
        rectsW.sort((x, y) -> {
            int dw = y.getWidth() - x.getWidth();
            if (dw == 0) {
                return y.getHeight() - x.getHeight();
            }
            return dw;
        });

        List<Rectangle<T>> rectsH = new ArrayList<>(rectangles);
        rectsH.sort((x, y) -> {
            int dh = y.getHeight() - x.getHeight();
            if (dh == 0) {
                return y.getWidth() - x.getWidth();
            }
            return dh;
        });

        Rectangle space = new Rectangle(0, 0, width, height);

        int waste = recursivePack(rectsW, rectsH, space, results);

        results.notPacked = rectsW;
        results.waste = waste;

        return results;
    }

    private int recursivePack(List<Rectangle<T>> rectsW, List<Rectangle<T>> rectsH, Rectangle space, Results<T> results) {
        List<Rectangle<T>> rects;
        if (space.getWidth() < space.getHeight())
            rects = rectsW;
        else
            rects = rectsH;

        Rectangle<T> rect = rects.stream().filter(r -> space.contains(r)).findFirst().orElse(null);
        if (rect == null)
            return space.getArea();

        rectsW.remove(rect);
        rectsH.remove(rect);
        rect.setX(space.getX());
        rect.setY(space.getY());
        results.packed.add(rect);

        Rectangle spaceA;
        Rectangle spaceB;
        if (space.getWidth() - rect.getWidth() < space.getHeight() - rect.getHeight()) {
            Pair<Rectangle, Rectangle> splitA = space.splitHorizontal(rect.getHeight());
            Pair<Rectangle, Rectangle> splitB = splitA.getKey().splitVertically(rect.getWidth());
            spaceA = splitB.getValue();
            spaceB = splitA.getValue();
        }
        else {
            Pair<Rectangle, Rectangle> splitA = space.splitVertically(rect.getWidth());
            Pair<Rectangle, Rectangle> splitB = splitA.getKey().splitHorizontal(rect.getHeight());
            spaceA = splitB.getValue();
            spaceB = splitA.getValue();
        }

        int wasteA = recursivePack(rectsW, rectsH, spaceA, results);
        int wasteB = recursivePack(rectsW, rectsH, spaceB, results);

        return wasteA + wasteB;
    }
}

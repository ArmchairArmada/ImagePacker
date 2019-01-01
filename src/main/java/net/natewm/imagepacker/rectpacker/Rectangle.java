package net.natewm.imagepacker.rectpacker;

import java.util.AbstractMap.SimpleEntry;

public class Rectangle<T> {
    private int x;
    private int y;
    private int width;
    private int height;
    private T content;

    public Rectangle(int x, int y, int width, int height, T content) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.content = content;
    }

    public Rectangle(int width, int height, T content) {
        this.x = -1;
        this.y = -1;
        this.width = width;
        this.height = height;
        this.content = content;
    }

    public Rectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.content = null;
    }

    public Rectangle(int width, int height) {
        this.x = -1;
        this.y = -1;
        this.width = width;
        this.height = height;
        this.content = null;
    }

    public Rectangle(Rectangle<T> other) {
        this.x = other.x;
        this.y = other.y;
        this.width = other.width;
        this.height = other.height;
        this.content = other.content;
    }

    public int getX(){
        return x;
    }

    public void setX(int value) {
        x = value;
    }

    public int getY() {
        return y;
    }

    public void setY(int value) {
        y = value;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int value) {
        width = value;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int value) {
        height = value;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T value) {
        content = value;
    }

    public boolean contains(Rectangle other) {
        return width >= other.width && height >= other.height;
    }

    public int getArea() {
        return width * height;
    }

    public SimpleEntry<Rectangle, Rectangle> splitHorizontal(int splitHeight) {
        Rectangle rectA = new Rectangle(x, y, width, splitHeight);
        Rectangle rectB = new Rectangle(x, y + splitHeight, width, height - splitHeight);
        return new SimpleEntry<>(rectA, rectB);
    }

    public SimpleEntry<Rectangle, Rectangle> splitVertically(int splitWidth) {
        Rectangle rectA = new Rectangle(x, y, splitWidth, height);
        Rectangle rectB = new Rectangle(x + splitWidth, y, width - splitWidth, height);
        return new SimpleEntry<>(rectA, rectB);
    }
}

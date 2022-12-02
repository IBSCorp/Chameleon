package ru.ibsqa.qualit.uia.palette;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;

public class ColorBean implements IColor {

    @Getter
    @Setter
    private short r;
    @Getter
    @Setter
    private short g;
    @Getter
    @Setter
    private short b;

    @Setter
    private String name;
    @Setter
    private double delta;

    private Color color;

    public ColorBean(short r, short g, short b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public ColorBean() {

    }

    @Override
    public Color getColor() {
        if (color == null) {
            color = new Color(r, g, b);
        }
        return color;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getDelta() {
        return delta;
    }

    @Override
    public int getRed() {
        return r;
    }

    @Override
    public int getGreen() {
        return g;
    }

    @Override
    public int getBlue() {
        return b;
    }

}

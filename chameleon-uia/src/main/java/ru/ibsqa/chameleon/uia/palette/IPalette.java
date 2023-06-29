package ru.ibsqa.chameleon.uia.palette;


import java.awt.*;
import java.util.List;

public interface IPalette {
    IColor getColor(Point point);

    List<IColor> getColor(String name);

}

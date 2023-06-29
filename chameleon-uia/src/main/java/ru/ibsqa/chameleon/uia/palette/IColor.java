package ru.ibsqa.chameleon.uia.palette;

import java.awt.*;

public interface IColor {

    Color getColor();

    String getName();

    double getDelta();

    int getRed();

    int getGreen();

    int getBlue();

    /**
     * Метод не жесткого сравнения цветов, основан на степени дальтонизама разработчика =)
     *
     * @param target
     * @return
     */
    default boolean equals(IColor target) {
        return this.getName().equals(target.getName());
    }
}

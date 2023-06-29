package ru.ibsqa.chameleon.uia.palette;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PaletteImpl implements IPalette {

    private IColor unknown;

    private List<IColor> list;

    private Robot r;

    public PaletteImpl() {
        try {
            r = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }

        short arg = -1;
        unknown = new ColorBean(arg, arg, arg);
    }

    @Autowired
    private void collectColors(List<IColor> pool) {
        this.list = pool;
    }

    @Override
    public IColor getColor(Point point) {
        if (r != null) {
            Color current = r.getPixelColor((int) point.getX(), (int) point.getY());

            List<IColor> candidates = list.stream().filter(i -> {
                        double vec = Math.sqrt(
                                Math.pow((i.getRed() - current.getRed()), 2) +
                                        Math.pow((i.getGreen() - current.getGreen()), 2) +
                                        Math.pow((i.getBlue() - current.getBlue()), 2)
                        );
                        return vec <= i.getDelta();
                    }
            ).collect(Collectors.toList());

            if (candidates.size() == 0) {
                return unknown;
            } else if (candidates.size() == 1) {
                return candidates.get(0);
            } else {
                return candidates.stream().sorted((o1, o2) -> {
                            double vec1 =
                                    Math.sqrt(
                                            Math.pow((o1.getRed() - current.getRed()), 2) +
                                                    Math.pow((o1.getGreen() - current.getGreen()), 2) +
                                                    Math.pow((o1.getBlue() - current.getBlue()), 2)
                                    );
                            double vec2 =
                                    Math.sqrt(
                                            Math.pow((o2.getRed() - current.getRed()), 2) +
                                                    Math.pow((o2.getGreen() - current.getGreen()), 2) +
                                                    Math.pow((o2.getBlue() - current.getBlue()), 2)
                                    );

                            if (vec1 > vec2)
                                return 1;
                            else if (vec1 < vec2)
                                return -1;
                            else
                                return 0;
                        }
                ).findFirst().get();
            }
        }
        return unknown;
    }

    @Override
    public List<IColor> getColor(String name) {
        return list.stream().filter(c -> name.toLowerCase().equals(c.getName())).collect(Collectors.toList());
    }
}

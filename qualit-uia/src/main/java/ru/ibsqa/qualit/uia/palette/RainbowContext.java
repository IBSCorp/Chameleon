package ru.ibsqa.qualit.uia.palette;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;


@Configuration
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class RainbowContext {

    private int delta = 30;

    @Bean
    public IColor createBlack() {
        short r = 0;
        short g = 0;
        short b = 0;
        ColorBean black = new ColorBean(r, g, b);
        black.setDelta(delta);
        black.setName("черный");

        return black;
    }

    @Bean
    public IColor createGray() {
        short r = 150;
        short g = 150;
        short b = 150;
        ColorBean gray = new ColorBean(r, g, b);
        gray.setDelta(delta);
        gray.setName("серый");

        return gray;
    }

    @Bean
    public IColor createWhite() {
        short r = 255;
        short g = 255;
        short b = 255;
        ColorBean white = new ColorBean(r, g, b);
        white.setDelta(delta);
        white.setName("белый");

        return white;
    }

    @Bean
    public IColor createRed() {
        short r = 255;
        short g = 0;
        short b = 0;
        ColorBean red = new ColorBean(r, g, b);
        red.setDelta(delta);
        red.setName("красный");

        return red;
    }

    @Bean
    public IColor createOrange() {
        short r = 255;
        short g = 150;
        short b = 0;
        ColorBean orange = new ColorBean(r, g, b);
        orange.setDelta(delta);
        orange.setName("оранжевый");

        return orange;
    }

    @Bean
    public IColor createYellow() {
        short r = 255;
        short g = 255;
        short b = 0;
        ColorBean yellow = new ColorBean(r, g, b);
        yellow.setDelta(delta);
        yellow.setName("желтый");

        return yellow;
    }

    @Bean
    public IColor createGreen() {
        short r = 0;
        short g = 255;
        short b = 0;
        ColorBean green = new ColorBean(r, g, b);
        green.setDelta(delta);
        green.setName("зеленый");

        return green;
    }

    @Bean
    public IColor createCyan() {
        short r = 0;
        short g = 255;
        short b = 255;
        ColorBean cyan = new ColorBean(r, g, b);
        cyan.setDelta(delta);
        cyan.setName("голубой");

        return cyan;
    }

    @Bean
    public IColor createBlue() {
        short r = 0;
        short g = 0;
        short b = 255;
        ColorBean blue = new ColorBean(r, g, b);
        blue.setDelta(delta);
        blue.setName("синий");

        return blue;
    }

    @Bean
    public IColor createPurple() {
        short r = 150;
        short g = 0;
        short b = 255;
        ColorBean purple = new ColorBean(r, g, b);
        purple.setDelta(delta);
        purple.setName("фиолетовый");

        return purple;
    }
}

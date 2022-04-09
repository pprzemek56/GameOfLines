package com.example.lines;


import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.Random;

public class Ball extends Circle{

    private static final Color[] colors = {
            Color.BLUE, Color.AQUA, Color.GREEN, Color.YELLOW, Color.RED, Color.ORANGE, Color.BROWN
    };

    private int i;
    private int j;


    public Ball() {
        Random random = new Random();

        this.i = random.nextInt(9);
        this.j = random.nextInt(9);
        this.setFill(colors[random.nextInt(colors.length)]);
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }

}

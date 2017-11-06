package com.mikroacse.engine.utils;

public class Vector2D {
    public int x;
    public int y;

    public Vector2D(int x, int y) {
        set(x, y);
    }

    public Vector2D() {
        set(0, 0);
    }

    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void set(Vector2D vector2D) {
        set(vector2D.x, vector2D.y);
    }

    public boolean equals(int x, int y) {
        return this.x == x && this.y == y;
    }

    public boolean equals(Vector2D vector2D) {
        return x == vector2D.x && y == vector2D.y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}

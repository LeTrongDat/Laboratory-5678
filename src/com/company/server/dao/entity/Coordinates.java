package com.company.server.dao.entity;

import com.company.shared.annotations.FieldAnnotation;

import java.io.Serializable;

/**
 * Coordinates of Space Marine
 * @see SpaceMarine SpaceMarine
 * @author Le Trong Dat
 */
public class Coordinates implements Serializable {
    @FieldAnnotation(type="Integer", min_value = 0, max_value = 451) private Integer x; //Максимальное значение поля: 451
    @FieldAnnotation(type="Integer", min_value = 0, max_value = 273) private Integer y; //Максимальное значение поля: 273

    public Coordinates(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }
    public String csv() {
        return "\"(" + x + ", " + y + ")\"";
    }
}
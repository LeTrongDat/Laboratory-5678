package com.company.shared.entity;

import com.company.shared.annotations.Field;

import java.io.Serializable;

/**
 * Chapter of Space Marine.
 * @see SpaceMarine SpaceMarine
 * @author Le Trong Dat
 */
public class Chapter implements Serializable {
    @Field(type="String") private String name; //Поле не может быть null, Строка не может быть пустой
    @Field(type="Long", min_value = 1, max_value = 1000) private Long marinesCount; //Поле может быть null, Значение поля должно быть больше 0, Максимальное значение поля: 1000

    public Chapter(String name, Long marinesCount) {
        this.name = name;

        this.marinesCount = marinesCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getMarinesCount() {
        return marinesCount;
    }

    public void setMarinesCount(Long marinesCount) {
        this.marinesCount = marinesCount;
    }

    public String toString() {
        return "(" + name + ", " + marinesCount + ")";
    }
}
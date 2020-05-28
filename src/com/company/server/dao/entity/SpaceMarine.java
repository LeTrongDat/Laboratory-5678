package com.company.server.dao.entity;

import com.company.shared.annotations.FieldAnnotation;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

/**
 * Space Marine object.
 * @author Le Trong Dat
 */
public class SpaceMarine implements Comparable<SpaceMarine>, Serializable {
    private static Integer count = 1;
    @FieldAnnotation(type="Integer", ignore = true) private Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    @FieldAnnotation(type="String") private String name; //Поле не может быть null, Строка не может быть пустой
    @FieldAnnotation(type="Coordinates") private Coordinates coordinates; //Поле не может быть null
    @FieldAnnotation(type="ZonedDateTime", ignore = true) private ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    @FieldAnnotation(type="Integer", min_value = 1) private Integer health; //Поле не может быть null, Значение поля должно быть больше 0
    @FieldAnnotation(type="Enum") private AstartesCategory category; //Поле может быть null
    @FieldAnnotation(type="Enum") private Weapon weaponType; //Поле может быть null
    @FieldAnnotation(type="Enum") private MeleeWeapon meleeWeapon; //Поле может быть null
    @FieldAnnotation(type="Chapter") private Chapter chapter; //Поле не может быть null

    public SpaceMarine(String name, Coordinates coordinates, Integer health, AstartesCategory category, Weapon weaponType, MeleeWeapon meleeWeapon, Chapter chapter) {
        this.id = count++;
        this.creationDate = LocalDateTime.now().atZone(ZoneId.of("UTC+7"));
        this.name = name;
        this.coordinates = coordinates;
        this.health = health;
        this.category = category;
        this.weaponType = weaponType;
        this.meleeWeapon = meleeWeapon;
        this.chapter = chapter;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getHealth() {
        return health;
    }

    public void setHealth(Integer health) {
        this.health = health;
    }

    public AstartesCategory getCategory() {
        return category;
    }

    public void setCategory(AstartesCategory category) {
        this.category = category;
    }

    public Weapon getWeaponType() {
        return weaponType;
    }

    public void setWeaponType(Weapon weaponType) {
        this.weaponType = weaponType;
    }

    public MeleeWeapon getMeleeWeapon() {
        return meleeWeapon;
    }

    public void setMeleeWeapon(MeleeWeapon meleeWeapon) {
        this.meleeWeapon = meleeWeapon;
    }

    public Chapter getChapter() {
        return chapter;
    }

    public void setChapter(Chapter chapter) {
        this.chapter = chapter;
    }

    @Override
    public int compareTo(@org.jetbrains.annotations.NotNull SpaceMarine o) {
        if (this.id > o.id) return 1;
        if (this.id == o.id) return 0;
        return -1;
    }

    @Override
    public String toString() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm, dd MMM yyyy");
        return "Id: " + id + '\n' +
                "Name: " + name + '\n' +
                "Coordinates: " + coordinates.toString() + '\n' +
                "Creation Date: " + dtf.format(creationDate) + '\n' +
                "Health: " + health + '\n' +
                "Astartes Category: " + category + '\n' +
                "Weapon: " + weaponType + '\n' +
                "Melee Weapon: " + meleeWeapon + '\n' +
                "Chapter: " + chapter.toString() + '\n';
    }
    public String csv() {
        Object[] objects = {name, coordinates.csv(), health, category, weaponType, meleeWeapon, chapter.csv()};
        return Arrays.toString(objects).replaceAll("[\\[\\]]", "");
    }
}

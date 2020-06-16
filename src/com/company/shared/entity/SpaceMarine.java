package com.company.shared.entity;

import com.company.shared.annotations.Field;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Space Marine object.
 * @author Le Trong Dat
 */
public class SpaceMarine implements Comparable<SpaceMarine>, Serializable {
    @Field(type="Integer", ignore = true) private Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    @Field(type="String") private String name; //Поле не может быть null, Строка не может быть пустой
    @Field(type="Coordinates") private Coordinates coordinates; //Поле не может быть null
    @Field(type="ZonedDateTime", ignore = true) private ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    @Field(type="Integer", min_value = 1) private Integer health; //Поле не может быть null, Значение поля должно быть больше 0
    @Field(type="Enum") private AstartesCategory category; //Поле может быть null
    @Field(type="Enum") private Weapon weaponType; //Поле может быть null
    @Field(type="Enum") private MeleeWeapon meleeWeapon; //Поле может быть null
    @Field(type="Chapter") private Chapter chapter; //Поле не может быть null

    private String createdBy;

    public SpaceMarine(String name, Coordinates coordinates, Integer health, AstartesCategory category, Weapon weaponType, MeleeWeapon meleeWeapon, Chapter chapter) {
        this.id = 0;
        this.creationDate = LocalDateTime.now().atZone(ZoneId.of("UTC+7"));
        this.name = name;
        this.coordinates = coordinates;
        this.health = health;
        this.category = category;
        this.weaponType = weaponType;
        this.meleeWeapon = meleeWeapon;
        this.chapter = chapter;
    }

    public SpaceMarine(){}
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public int compareTo(@org.jetbrains.annotations.NotNull SpaceMarine o) {
        if (this.id > o.id) return 1;
        if (this.id.equals(o.id)) return 0;
        return -1;
    }

    @Override
    public String toString() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy");
        return "Id: " + id + '\n' +
                "Name: " + name + '\n' +
                "Coordinates: " + coordinates.toString() + '\n' +
                "Creation Date: " + dtf.format(creationDate) + '\n' +
                "Health: " + health + '\n' +
                "Astartes Category: " + category + '\n' +
                "Weapon: " + weaponType + '\n' +
                "Melee Weapon: " + meleeWeapon + '\n' +
                "Chapter: " + chapter.toString() + '\n' +
                "Created by: " + createdBy + '\n';
    }
}

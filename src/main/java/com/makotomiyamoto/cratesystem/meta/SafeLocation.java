package com.makotomiyamoto.cratesystem.meta;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

public class SafeLocation {

    private String worldByName;
    private double x, y, z;

    public SafeLocation() {
        worldByName = "world"; // get default world
        x = 0;
        y = 0;
        z = 0;
    }

    public static SafeLocation parseFromYamlSection(ConfigurationSection section) {

        SafeLocation safeLocation = new SafeLocation();

        String worldByName = section.getString("world");
        if (worldByName != null) {
            safeLocation.worldByName = worldByName;
        } else {
            safeLocation.worldByName = "world";
        }

        try {
            safeLocation.x = section.getDouble("x");
            safeLocation.y = section.getDouble("y");
            safeLocation.z = section.getDouble("z");
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("x, y, and z should be defined in the location section.");
        }

        return safeLocation;

    }

    public String getWorldByName() {
        return worldByName;
    }
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public double getZ() {
        return z;
    }

    public void setWorldByName(String worldByName) {
        this.worldByName = worldByName;
    }
    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
    }
    public void setZ(double z) {
        this.z = z;
    }

    public Location build() {

        // noinspection all
        Location location = new Location(Bukkit.getWorld(worldByName), x, y, z);

        return location;

    }

}

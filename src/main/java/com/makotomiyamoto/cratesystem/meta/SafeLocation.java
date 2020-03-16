package com.makotomiyamoto.cratesystem.meta;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class SafeLocation {
    private String worldName;
    private int x,y,z;
    public SafeLocation(String worldName, int x, int y, int z) {
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public static SafeLocation parseFromBukkitLocation(Location location) {
        assert location.getWorld() != null;
        return new SafeLocation(
                location.getWorld().getName(),
                location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ()
        );
    }
    public Location build() {
        return new Location(Bukkit.getWorld(worldName), x, y, z);
    }
    public String getWorldName() {
        return worldName;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getZ() {
        return z;
    }
}

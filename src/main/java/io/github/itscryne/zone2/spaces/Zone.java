package io.github.itscryne.zone2.spaces;

import org.bukkit.World;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import io.github.itscryne.zone2.config.ConfigReader;
import io.github.itscryne.zone2.extensions.Zonecation;
import io.github.itscryne.zone2.perms.Permission;

/**
 * @serial JSON
 */
public class Zone extends Area {
    private int priority;
    private int id;
    private UUID zoneUUID;
    private String name;
    private List<Permission> perms;

    /**
     * @param l1       First Zonecation (Higher Coordinates)
     * @param l2       Second Zonecation (Lower Coordinates)
     * @param priority Priority of the zone
     * @param id       ID of the zone
     * @param name     Name of the zone
     * @param perms    Permissions of the zone
     */
    protected Zone(Zonecation l1, Zonecation l2, int priority, int id, String name, List<Permission> perms) {
        super(l1, l2); // -> l1, l2
        this.id = id;
        this.priority = priority;
        this.zoneUUID = UUID.randomUUID();
        this.name = name;
        this.perms = perms;
    }

    /**
     * @param hx       Hgher x coordinate
     * @param lx       Lower x coordinate
     * @param hy       Higher y coordinate
     * @param ly       Lower l coordinate
     * @param hz       Higher z coordinate
     * @param lz       Lower z coordinate
     * @param w        World
     * @param priority Priority of the Zone
     * @param id       ID of the Zone
     * @param name     Name of the zone
     * @param perms    Permissions of the zone
     */
    protected Zone(int hx, int lx, int hy, int ly, int hz, int lz, World w, int priority, int id, String name, List<Permission> perms) {
        super(hx, lx, hy, ly, hz, lz, w); // -> l1, l2
        this.id = id;
        this.priority = priority;
        this.zoneUUID = UUID.randomUUID();
        this.name = name;
        this.perms = perms;
    }

    /**
     * Checks wether the instance Zone collides with any other Zone
     * 
     * @param plugin the plugin
     * @return wether the instance collides
     * @throws IOException if it cant find a file et al
     */
    public boolean collidesWithAnyZone() throws IOException {
        ConfigReader reader = ConfigReader.getInstance();
        List<PlayerZone> playerZoneList = reader.getPlayerZoneList();
        List<ServerZone> serverZoneList = reader.getServerZoneList();

        if (playerZoneList != null) {
            if (!playerZoneList.isEmpty()) {
                for (PlayerZone i : playerZoneList) {
                    i.setL1(new Zonecation(Zonecation.deserialize(i.getSerL1())));
                    i.setL2(new Zonecation(Zonecation.deserialize(i.getSerL2())));

                    if (this.getL2().getX() <= i.getL1().getX() && this.getL1().getX() >= i.getL2().getX()
                            && this.getL2().getY() <= i.getL1().getY() && this.getL1().getY() >= i.getL2().getY()
                            && this.getL2().getZ() <= i.getL1().getZ() && this.getL1().getZ() >= i.getL2().getZ()
                            && this.getL1().getWorld().equals(i.getL1().getWorld())) {
                        return true;
                    }
                }
            }
        }

        if (playerZoneList != null) {
            if (!playerZoneList.isEmpty()) {
                for (ServerZone i : serverZoneList) {
                    i.setL1(new Zonecation(Zonecation.deserialize(i.getSerL1())));
                    i.setL2(new Zonecation(Zonecation.deserialize(i.getSerL2())));

                    if (this.getL2().getX() <= i.getL1().getX() && this.getL1().getX() >= i.getL2().getX()
                            && this.getL2().getY() <= i.getL1().getY() && this.getL1().getY() >= i.getL2().getY()
                            && this.getL2().getZ() <= i.getL1().getZ() && this.getL1().getZ() >= i.getL2().getZ()
                            && this.getL1().getWorld().equals(i.getL1().getWorld())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Returns the next available ID
     * 
     * @param plugin the plugin
     * @return the ID
     * @throws IOException if it cant find a file et al
     */
    public static int getNextId() throws IOException {
        ConfigReader reader = ConfigReader.getInstance();
        List<ServerZone> serverZoneList = reader.getServerZoneList();
        List<PlayerZone> playerZoneList = reader.getPlayerZoneList();
        List<SubZone> subZoneList = reader.getSubZoneList();
        if ((serverZoneList == null || serverZoneList.isEmpty()) && (playerZoneList == null || playerZoneList.isEmpty())
                && (subZoneList == null || subZoneList.isEmpty())) {
            return 0;
        }

        List<Integer> zoneIds = new ArrayList<>();
        if (serverZoneList != null && !serverZoneList.isEmpty()){
            serverZoneList.forEach(element -> zoneIds.add(element.getId()));
        }
        if (playerZoneList != null && !playerZoneList.isEmpty()){
            playerZoneList.forEach(element -> zoneIds.add(element.getId()));
        }
        if (subZoneList != null && !subZoneList.isEmpty()){
            subZoneList.forEach(element -> zoneIds.add(element.getId()));
        }

        if (zoneIds != null && !zoneIds.isEmpty()){
            return Collections.max(zoneIds) + 1;
        }
        return 0;
    }

    public List<SubZone> getSubZones() throws IOException {
        ConfigReader reader = ConfigReader.getInstance();
        List<SubZone> subZoneList = reader.getSubZoneList();
        List<SubZone> matches = new ArrayList<>();
        for (SubZone zone : subZoneList) {
            if (zone.getSuperZoneUUID().equals(this.getZoneUUID())) {
                matches.add(zone);
            }
        }
        return matches;
    }

    /**
     * @return Priority of the Zone
     */
    public int getPriority() {
        return this.priority;
    }

    /**
     * @return ID of the Zone
     */
    public int getId() {
        return this.id;
    }

    public UUID getZoneUUID() {
        return this.zoneUUID;
    }

    public String getName() {
        return this.name;
    }

    public boolean equals(Zone zone) {
        return this.getZoneUUID().equals(zone.getZoneUUID());
    }

     /**
     * @return Permissions of the zone
     */
    public List<Permission> getPerms() {
        return this.perms;
    }

    public void setPerms(List<Permission> perms) {
        this.perms = perms;
    }
}

package io.github.itscryne.zone2;

import org.bukkit.Location;
import org.bukkit.World;

import java.io.Serializable;

/**
 * @serial JSON
 */
public class ServerZone extends Zone implements Serializable {
    private String name;

    /**
     *
     * @param l1 First Location (higher coordinates)
     * @param l2 Second Location (lower coordinates)
     * @param priority Priority of the Zone
     * @param id ID of the Zone
     * @param name Name of the Zone
     */
    protected ServerZone(Location l1, Location l2, int priority, int id, String name) {
        super(l1, l2, priority, id);
        this.name = name;
    }

    /**
     *
     * @param hx Hgher x coordinate
     * @param lx Lower x coordinate
     * @param hy Higher y coordinate
     * @param ly Lower l coordinate
     * @param hz Higher z coordinate
     * @param lz Lower z coordinate
     * @param w World
     * @param priority Priority of the Zone
     * @param id ID of the Zone
     * @param name Name of the Zone
     */
    //TODO: Move name to Zone
    protected ServerZone(int hx, int lx, int hy, int ly, int hz, int lz, World w, int priority, int id, String name) {
        super(hx, lx, hy, ly, hz, lz, w, priority, id);
        this.name = name;
    }

    /**
     *
     * @return Name of the zone
     */
    public String getName() {
        return this.name;
    }
}

package io.github.itscryne.zone2.events;

import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.spigotmc.event.entity.EntityMountEvent;

import java.io.IOException;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import io.github.itscryne.zone2.Zone2;
import io.github.itscryne.zone2.perms.PermissionType;

public class Zone2TransportTypeEvent implements Listener {
    private Zone2 plugin;

    public Zone2TransportTypeEvent(Zone2 plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) throws IOException { //TODO: Teleport outside of zones allow into config
        if(event.getCause() != TeleportCause.CHORUS_FRUIT){
            if(event.getCause() != TeleportCause.ENDER_PEARL){
                return;
            }
        }

        Location eventLocation = event.getTo();

        if (!Zone2PermCheck.inZone(eventLocation, this.plugin)) return;

        Player eventPlayer = event.getPlayer();

        boolean allowed = Zone2PermCheck.isAllowed(eventLocation, eventPlayer, PermissionType.TRANSPORT, this.plugin);
        this.plugin.getLogger().info(String.valueOf(allowed));
     }

     @EventHandler
     public void onEntityMount (EntityMountEvent event) throws IOException { //Transport or Entity
         Location eventLocation = event.getMount().getLocation();

         if (!(event.getEntity() instanceof Player)) return;

         Player eventPlayer = (Player) event.getEntity();

         boolean allowed = Zone2PermCheck.isAllowed(eventLocation, eventPlayer, PermissionType.TRANSPORT, this.plugin);
         this.plugin.getLogger().info(String.valueOf(allowed));
     }
}
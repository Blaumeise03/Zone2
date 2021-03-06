package io.github.itscryne.zone2.commands;

import io.github.itscryne.zone2.Zone2;
import io.github.itscryne.zone2.config.ConfigWriter;
import io.github.itscryne.zone2.perms.Permission;
import io.github.itscryne.zone2.spaces.ServerZone;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class Zone2CreateServerZoneCommand implements CommandExecutor {
    /**
     * Executes the given command, returning its success. <br>
     * If false is returned, then the "usage" plugin.yml entry for this command (if
     * defined) will be sent to the player.
     *
     * @param sender  Source of the command
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) { // hx, lx, hy, ly,
                                                                                                   // hz. lz, w,
                                                                                                   // priority, name
        if (args.length != 9) {
            return false;
        }

        Integer hx = null;
        Integer lx = null;
        Integer hy = null;
        Integer ly = null;
        Integer hz = null;
        Integer lz = null;

        try {
            hx = Integer.parseInt(args[0]);
            lx = Integer.parseInt(args[1]);
            hy = Integer.parseInt(args[2]);
            ly = Integer.parseInt(args[3]);
            hz = Integer.parseInt(args[4]);
            lz = Integer.parseInt(args[5]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.YELLOW + "Die Koordinaten müssen Ganzzahlen sein!");
            return true;
        }

        if (hx < lx || hz < lz || hy < ly) {
            sender.sendMessage(ChatColor.YELLOW + "h <-> High coordinates | l <-> Low coordinates");
            return true;
        }

        if (hx.equals(lx) || hz.equals(lz) || hy.equals(ly)) {
            sender.sendMessage(ChatColor.YELLOW + "Eine Zone muss in alle Richtungen mindestens zwei Blöcke lang sein");
            return true;
        }

        World w = Bukkit.getWorld(args[6]);

        if (w == null) {
            sender.sendMessage(ChatColor.YELLOW + "Diese Welt existiert nicht!");
            sender.sendMessage(ChatColor.DARK_GREEN + "Mögliche Welten sind:");
            for (World i : Bukkit.getWorlds()) {
                sender.sendMessage(ChatColor.GREEN + "  - \"" + i.getName() + "\"");
            }
            return true;
        }

        int priority = Integer.parseInt(args[7]);

        int id = 0;
        try {
            id = ServerZone.getNextId();
        } catch (IOException e) {
            Zone2.getPlugin().getLogger().log(Level.SEVERE, e.getMessage(), e.getCause());
            sender.sendMessage(ChatColor.DARK_RED + "Etwas ist schiefgelaufen! Bitte kontaktiere einen Developer");
            sender.sendMessage(ChatColor.RED + "Zone konnte nicht erstellt werden");
            return true;
        }

        String name = args[8];

        List<Permission> perms = new ArrayList<>();

        ServerZone sz = new ServerZone(hx, lx, hy, ly, hz, lz, w, priority, id, name, perms);

        try {
            ConfigWriter writer = ConfigWriter.getInstance();
            writer.writeServerZone(sz);
            sender.sendMessage(ChatColor.GREEN + "Zone wurde erstellt!");
            writer.destroy();
            return true;
        } catch (IOException e) {
            sender.sendMessage(ChatColor.DARK_RED + "Etwas ist schiefgelaufen! Bitte kontaktiere einen Developer");
            sender.sendMessage(ChatColor.RED + "Zone konnte nicht erstellt werden");
            Zone2.getPlugin().getLogger().log(Level.SEVERE, e.getMessage(), e.getCause());
            return true;
        }
    }
}

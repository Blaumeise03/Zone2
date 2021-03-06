package io.github.itscryne.zone2.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.itscryne.zone2.Zone2;
import io.github.itscryne.zone2.config.ConfigWriter;
import io.github.itscryne.zone2.extensions.Zonecation;
import io.github.itscryne.zone2.extensions.Zoneler;
import io.github.itscryne.zone2.perms.PermissionType;
import io.github.itscryne.zone2.spaces.SubZone;

public class Zone2DeleteSubZoneCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.YELLOW + "Nur Spieler können Spielerzonen erstellen!");
            return true;
        }

        if (args.length != 1) {
            return false;
        }

        Zoneler senderZoneler = new Zoneler((Player) sender);

        List<SubZone> subZoneList = new ArrayList<>();
        SubZone toDelete = null;

        int id;

        try {
            id = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.YELLOW + "Die Koordinaten müssen Ganzzahlen sein!");
            return true;
        }

        for (SubZone zone : subZoneList) {
            if (zone.getId() == id) {
                toDelete = zone;
            }
        }

        if (toDelete == null) {
            sender.sendMessage(ChatColor.YELLOW + "Diese Subzone existiert nicht");
            return true;
        }

        Zonecation l1 = toDelete.getL1();
        Zonecation l2 = toDelete.getL2();

        try {
            if (!(senderZoneler.isAllowed(l1, PermissionType.MANAGE)
                    && senderZoneler.isAllowed(l2, PermissionType.MANAGE))) {
                sender.sendMessage(ChatColor.RED + "Du darfst diese Subzone nicht löschen");
                return true;
            }

            ConfigWriter writer = ConfigWriter.getInstance();
            writer.deleteSubZone(toDelete.getId());

            return true;
        } catch (IOException e) {
            Zone2.getPlugin().getLogger().log(Level.SEVERE, e.getMessage(), e.getCause());
            sender.sendMessage(ChatColor.DARK_RED + "Etwas ist schiefgelaufen! Bitte kontaktiere einen Developer");
            sender.sendMessage(ChatColor.RED + "Zone konnte nicht gelöscht werden");
            return true;
        }
    }
}
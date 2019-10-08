package io.github.itscryne.zone2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.bukkit.Location;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ConfigWriter {
    private final String playerZonePath;
    private final String serverZonePath;
    private Zone2 plugin;
    private File dataDir;
    private File playerZonesFile;
    private File serverZonesFile;

    public ConfigWriter(Zone2 plugin) throws IOException {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Location.class, new LocationAdapter());
        Gson gson = builder.setPrettyPrinting().create();;

        this.plugin = plugin;
        this.dataDir = this.plugin.getDataFolder();
        dataDir.mkdir();

        String dataDirPath = dataDir.getAbsolutePath();
        this.serverZonePath = dataDirPath + "/serverZones.json";
        this.playerZonePath = dataDirPath + "/playerZones.json";

        this.playerZonesFile = new File(playerZonePath);
        playerZonesFile.createNewFile();
        if (playerZonesFile.length() == 0){ //writing empty List<PlayerZone> to JSON file so we can access it later
            List<PlayerZone> playerZoneList = new ArrayList<>();
            Type playerZoneListType = new TypeToken<List<PlayerZone>>(){}.getType();

            JsonWriter playerZoneListWriter = new JsonWriter(new FileWriter(this.playerZonesFile));
            gson.toJson(playerZoneList, playerZoneListType, playerZoneListWriter);
        }

        this.serverZonesFile = new File(serverZonePath);
        serverZonesFile.createNewFile();
        if (serverZonesFile.length() == 0){ //writing empty List<ServerZone> to JSON file so we can access it later
            List<ServerZone> serverZoneList = new ArrayList<>();
            Type serverZoneListType = new TypeToken<List<ServerZone>>(){}.getType();

            JsonWriter serverZoneListWriter = new JsonWriter(new FileWriter(this.serverZonesFile));
            gson.toJson(serverZoneList, serverZoneListType, serverZoneListWriter);
        }
    }

    public void writePlayerZone(PlayerZone zone) throws IOException {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Location.class, new LocationAdapter());
        Gson gson = builder.setPrettyPrinting().create();;

        JsonReader playerZoneListReader = new JsonReader(new FileReader(this.playerZonesFile));
        Type playerZoneListType = new TypeToken<List<PlayerZone>>(){}.getType();
        List<PlayerZone> playerZoneList = gson.fromJson(playerZoneListReader, playerZoneListType);
        playerZoneList.add(zone);

        JsonWriter playerZoneListWriter = new JsonWriter(new FileWriter(this.playerZonesFile));
        gson.toJson(playerZoneList, playerZoneListType, playerZoneListWriter);
    }

    public void writeServerZone(ServerZone zone) throws IOException {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Location.class, new LocationAdapter());
        Gson gson = builder.setPrettyPrinting().create();;

        JsonReader serverZoneListReader = new JsonReader(new FileReader(this.serverZonesFile));

        Type serverZoneListType = new TypeToken<List<ServerZone>>(){}.getType();
        List<ServerZone> serverZoneList = gson.fromJson(serverZoneListReader, serverZoneListType);
        serverZoneList.add(zone);

        JsonWriter serverZoneListWriter = new JsonWriter(new FileWriter(this.serverZonesFile));
        gson.toJson(serverZoneList, serverZoneListType, serverZoneListWriter);
    }

    private int findPlayerZoneIndexById(List<PlayerZone> playerZoneList, int id){
        for (PlayerZone p : playerZoneList){
            if (p.getId() == id){
                return playerZoneList.indexOf(p);
            }
        }
        return -1;
    }

    public void deletePlayerZone(int id) throws IOException {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Location.class, new LocationAdapter());
        Gson gson = builder.setPrettyPrinting().create();;

        JsonReader playerZoneListReader = new JsonReader(new FileReader(this.serverZonesFile));

        Type playerZoneListType = new TypeToken<List<PlayerZone>>() {}.getType();
        List<PlayerZone> playerZoneList = gson.fromJson(playerZoneListReader, playerZoneListType);

        int indexToDelete = findPlayerZoneIndexById(playerZoneList, id);
        if (indexToDelete == -1){
            return;
        } else {
            playerZoneList.remove(indexToDelete);
        }

        JsonWriter playerZoneListWriter = new JsonWriter(new FileWriter(this.playerZonesFile));
        gson.toJson(playerZoneList, playerZoneListType, playerZoneListWriter);
    }

    private int findServerZoneIndexById(List<ServerZone> serverZoneList, int id){
        for (ServerZone p : serverZoneList){
            if (p.getId() == id){
                return serverZoneList.indexOf(p);
            }
        }
        return -1;
    }

    public void deleteServerZone(int id) throws IOException {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Location.class, new LocationAdapter());
        Gson gson = builder.setPrettyPrinting().create();;

        JsonReader serverZoneListReader = new JsonReader(new FileReader(this.serverZonesFile));

        Type serverZoneListType = new TypeToken<List<ServerZone>>() {}.getType();
        List<ServerZone> serverZoneList = gson.fromJson(serverZoneListReader, serverZoneListType);

        int indexToDelete = findServerZoneIndexById(serverZoneList, id);
        if (indexToDelete == -1){
            return;
        } else {
            serverZoneList.remove(indexToDelete);
        }

        JsonWriter serverZoneListWriter = new JsonWriter(new FileWriter(this.serverZonesFile));
        gson.toJson(serverZoneList, serverZoneListType, serverZoneListWriter);
    }
}

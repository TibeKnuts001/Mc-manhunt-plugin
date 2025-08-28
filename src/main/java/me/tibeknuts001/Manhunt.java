package me.tibeknuts001;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Level;

public final class Manhunt extends JavaPlugin {

    @Override
    public void onLoad() {
        super.onLoad();
        ManhuntSettings.getInstance().load();
        if (ManhuntSettings.getInstance().config.getBoolean("Reset_world") && ManhuntSettings.getInstance().data.getBoolean("manhunt_ended")) {
            for (String world : Arrays.asList("world", "world_nether", "world_the_end")) {
                Bukkit.getLogger().log(Level.INFO, "Deleting " + world + " folder");
                File WorldFolder = new File(Bukkit.getWorldContainer(), world);
                if (!WorldFolder.exists()) {
                    Bukkit.getLogger().log(Level.WARNING, world + " Folder does not exist");
                    return;
                }
                deleteWorld(WorldFolder);
                Bukkit.getLogger().log(Level.INFO, world + " folder deleted");
            }
            ManhuntSettings.getInstance().data.set("manhunt_ended", false);
            ManhuntSettings.getInstance().save();
        } else {
            Bukkit.getLogger().log(Level.INFO, "Manhunt was not ended, not deleting world");
        }
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        Objects.requireNonNull(getCommand("Manhunt")).setExecutor(new ManhuntCommand());
        Bukkit.getPluginManager().registerEvents(new Compas(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getLogger().info("[Manhunt] Plugin has been disabled!");
    }

    public static Manhunt getInstance() {
        return getPlugin(Manhunt.class);
    }

    public void deleteWorld(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            assert files != null;
            for (File file : files) {
                if (file.isDirectory())
                    deleteWorld(file);
                else if (!file.getName().equals("uid.dat")) {
                    Bukkit.getLogger().info("[World Deleter] Deleting file: " + file.getName());
                    file.delete();
                }
            }
        }
        File playerdata = new File(path, "playerdata");
        playerdata.mkdir();
        path.delete();
    }
}

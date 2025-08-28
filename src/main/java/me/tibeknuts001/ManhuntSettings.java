package me.tibeknuts001;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ManhuntSettings {
    private final static ManhuntSettings instance = new ManhuntSettings();
    private File ConfigFile;
    private File DataFile;
    public YamlConfiguration config;
    public YamlConfiguration data;

    private ManhuntSettings() {

    }

    public void load() {
        ConfigFile = new File(Manhunt.getInstance().getDataFolder(), "config.yml");
        DataFile = new File(Manhunt.getInstance().getDataFolder(), "data.yml");
        if (!ConfigFile.exists()) {
            Manhunt.getInstance().saveResource("config.yml", false);
        }
        if (!DataFile.exists()) {
            Manhunt.getInstance().saveResource("data.yml", false);
        }
        config = new YamlConfiguration();
        config.options().parseComments(true);
        data = new YamlConfiguration();
        data.options().parseComments(true);
        try {
            config.load(ConfigFile);
            data.load(DataFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            config.save(ConfigFile);
            data.save(DataFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ManhuntSettings getInstance() {
        return instance;
    }
}

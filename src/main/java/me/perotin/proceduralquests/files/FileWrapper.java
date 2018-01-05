package me.perotin.proceduralquests.files;

import me.perotin.proceduralquests.QuestsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;

import static me.perotin.proceduralquests.files.FileWrapper.FileType.MESSAGES;
import static me.perotin.proceduralquests.files.FileWrapper.FileType.PLAYERS;
import static me.perotin.proceduralquests.files.FileWrapper.FileType.QUESTS;


public class FileWrapper {

    private File file;
    private FileConfiguration configuration;
    private FileType type;
    private QuestsPlugin plugin;

    public FileWrapper(FileType type, QuestsPlugin plugin) {
        // hard-coding some stuff to make our lives easier, since we'll be using
        // files A LOT so it is important we have a good system set in place to easily retreive these things

        this.plugin = plugin;

        if (type == MESSAGES) {
            file = new File(plugin.getDataFolder(), "messages.yml");
            configuration = YamlConfiguration.loadConfiguration(file);
            this.type = MESSAGES;
        }
        if (type == PLAYERS) {
            file = new File(plugin.getDataFolder(), "players.yml");
            configuration = YamlConfiguration.loadConfiguration(file);
            this.type = PLAYERS;
        }
        if (type == QUESTS) {
            file = new File(plugin.getDataFolder(), "quests.yml");
            configuration = YamlConfiguration.loadConfiguration(file);
            this.type = QUESTS;
        }


    }

    public void save() {
        try {
            configuration.save(file);
        } catch (IOException ex) {
            ex.printStackTrace();
            ;
        }
    }

    // some generic methods to speed up the process

    public FileConfiguration getConfiguration() {
        return configuration;
    }

    public Object get(String path) {
        return configuration.get(path);
    }

    public void set(String path, Object value) {
        configuration.set(path, value);
    }

    public String getString(String path) {
        return ChatColor.translateAlternateColorCodes('&', configuration.getString(path));
    }

    public void load() {

        File lang = null;
        InputStream defLangStream = null;

        switch (type) {
            case MESSAGES:
                lang = new File(plugin.getDataFolder(), "messages.yml");
                defLangStream = plugin.getResource("messages.yml");
                break;
            case PLAYERS:
                lang = new File(plugin.getDataFolder(), "players.yml");
                defLangStream = plugin.getResource("players.yml");
                break;

        }
        OutputStream out = null;
        if (!lang.exists()) {
            try {
                plugin.getDataFolder().mkdir();
                lang.createNewFile();
                if (defLangStream != null) {
                    out = new FileOutputStream(lang);
                    int read;
                    byte[] bytes = new byte[1024];

                    while ((read = defLangStream.read(bytes)) != -1) {
                        out.write(bytes, 0, read);
                    }
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace(); // So they notice
                Bukkit.getLogger().severe("[ProceduralQuests] Couldn't create " + type.toString().toLowerCase() + " file.");
                Bukkit.getLogger().severe("[ProceduralQuests] This is a fatal error. Now disabling");
               plugin.getPluginLoader().disablePlugin(plugin); // Without
                // it
                // loaded,
                // we
                // can't
                // send
                // them
                // messages
            } finally {
                if (defLangStream != null) {
                    try {
                        defLangStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    public static void loadFiles(QuestsPlugin plugin){
        for(FileType type : FileType.values()){
            new FileWrapper(type, plugin).load();
        }
    }


    public enum FileType {

        PLAYERS, MESSAGES, QUESTS

    }
}

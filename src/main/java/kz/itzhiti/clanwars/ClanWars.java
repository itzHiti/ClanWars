package kz.itzhiti.clanwars;

import kz.itzhiti.clanwars.Commands.CWCommand;
import kz.itzhiti.clanwars.Listeners.DeathListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ClanWars extends JavaPlugin {

    private static ClanWars plugin;
    public static FileConfiguration config;
    public static FileConfiguration playerdata;

    public static boolean fight;
    public static boolean pvp;
    public static boolean falldamage;
    public static boolean game;

    @Override
    public void onEnable() {
        // Запуск
        getLogger().warning("Запускаю плагин...");

        // Инициализация
        (plugin = this).saveDefaultConfig();
        ClanWars.config = Configurations.getFile("config.yml");
        ClanWars.playerdata = Configurations.getFile("data.yml");
        fight = false;
        pvp = false;
        falldamage = false;
        game = false;

        // Регистрация слушателей и команд
        if (Bukkit.getPluginManager().getPlugin("TerraformGenerator") != null) {
            Bukkit.getPluginManager().registerEvents(new DeathListener(), plugin);
            this.getCommand("clanwars").setExecutor(new CWCommand());
        } else {
            getLogger().warning("Не могу найти плагин TerraformGenerator! Этот плагин необходим для работы плагина ClanWars.");
            Bukkit.getPluginManager().disablePlugin(this);
        }

        // Завершение
        getLogger().info("§a§lПлагин запущен и полноценно работает.");
    }

    @Override
    public void onDisable() {
        // Отключение
        getLogger().warning("Пробуем отключить плагин...");

        // Завершение
        getLogger().info("§c§lПлагин отключен.");
    }

    public static ClanWars getInstance() {
        return plugin;
    }

    // Метод для получения сообщений и прочих настроек конфига из файла (config.yml)
    public String getCFG (String path) {
        return ChatColor.translateAlternateColorCodes('&', config.getString(path).replace("%new%", "\n"));
    }

    // Метод для получения важной даты игроков из файла (data.yml)
    public String getData (String path) {
        return ChatColor.translateAlternateColorCodes('&', playerdata.getString(path).replace("%new%", "\n"));
    }

    // Метод для цветов
    public String colorMaker (String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    // Метод отгрузки карты
    public static boolean unloadWorld(String worldName) {
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            return false;
        }

        Bukkit.getServer().unloadWorld(world, true);

        File worldFolder = new File(worldName);
        if (worldFolder.exists()) {
            String[] files = worldFolder.list();
            if (files != null) {
                for (String file : files) {
                    File f = new File(worldName + File.separator + file);
                    if (f.isDirectory()) {
                        deleteFolder(f);
                    } else {
                        f.delete();
                    }
                }
            }
            worldFolder.delete();
        }

        return true;
    }

    // Метод удаления карты
    private static void deleteFolder(File folder) {
        if (folder == null || !folder.exists() || !folder.isDirectory()) {
            return;
        }

        String[] files = folder.list();
        if (files != null) {
            for (String file : files) {
                File f = new File(folder.getAbsolutePath() + File.separator + file);
                if (f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }

        folder.delete();
    }
}

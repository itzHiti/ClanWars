package kz.itzhiti.clanwars;

import kz.itzhiti.clanwars.Commands.CWCommand;
import kz.itzhiti.clanwars.Listeners.DeathListener;
import kz.itzhiti.clanwars.Listeners.SafeTP;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
            Bukkit.getPluginManager().registerEvents(new SafeTP(), plugin);
            this.getCommand("clanwars").setExecutor(new CWCommand());
        } else {
            getLogger().warning("Не могу найти плагин TerraformGenerator! Этот плагин необходим для работы плагина ClanWars.");
            Bukkit.getPluginManager().disablePlugin(this);
        }

        // Загрузка команд (teams)
        loadTeams();

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

    // Метод сохранения новых команд (teams) в файле (data.yml)
    public void saveTeams() {
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "data.yml"));

        for (Team team : Bukkit.getScoreboardManager().getMainScoreboard().getTeams()) {
            String teamName = team.getName();
            cfg.set("TEAMS." + teamName + ".COLOR", team.getColor().name());

        List<String> players = new ArrayList<>();
        for (String playerName : team.getEntries()) {
            players.add(playerName);
        }
        cfg.set("TEAMS." + teamName + ".PLAYERS", players);
    }

        try {
            cfg.save(new File(getDataFolder(), "data.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Метод загрузки команд (teams) из файла (data.yml)
    public void loadTeams() {
        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "data.yml"));

        if (config.contains("teams")) {
            ConfigurationSection teamsSection = config.getConfigurationSection("teams");
            for (String teamName : teamsSection.getKeys(false)) {
                Team team = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam(teamName);
                String colorString = teamsSection.getString(teamName + ".COLOR");
                ChatColor color = ChatColor.valueOf(colorString);
                team.setColor(color);

                List<String> players = teamsSection.getStringList(teamName + ".PLAYERS");
                for (String playerName : players) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
                    team.addEntry(offlinePlayer.getName());
                }
            }
        }
    }

}


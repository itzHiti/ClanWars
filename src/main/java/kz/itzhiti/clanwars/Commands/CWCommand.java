package kz.itzhiti.clanwars.Commands;

import kz.itzhiti.clanwars.ClanWars;
import kz.itzhiti.clanwars.Configurations;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Set;

import static kz.itzhiti.clanwars.ClanWars.*;

public class CWCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String l, String[] args) {
        Player p = (Player) sender;
        if (args.length == 0) {
            sender.sendMessage(ClanWars.getInstance().getCFG("MESSAGES.HELPMSG"));
        }
        else {
            String lowercase = args[0].toLowerCase();
            switch (lowercase) {
                case "reload": {
                    if (!(p.hasPermission("clanwars.reload"))) {
                        sender.sendMessage(ClanWars.getInstance().getCFG("MESSAGES.NO_PERMS"));
                        break;
                    }
                    ClanWars.config = Configurations.getFile("config.yml");
                    ClanWars.playerdata = Configurations.getFile("data.yml");
                    sender.sendMessage(ClanWars.getInstance().getCFG("MESSAGES.RELOAD_SUCCESSFUL"));
                    break;
                }
                case "start": {
                    WorldCreator worldCreator = new WorldCreator("game");
                    Bukkit.createWorld(worldCreator);
                    if (!(p.hasPermission("clanwars.start"))) {
                        sender.sendMessage(ClanWars.getInstance().getCFG("MESSAGES.NO_PERMS"));
                        break;
                    }
                    Bukkit.broadcastMessage(ClanWars.getInstance().getCFG("MESSAGES.GAME_START"));
                    for (Player pl : Bukkit.getOnlinePlayers()) {
                        pl.playSound(pl.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 100F, 0.5F);
                    }
                    Bukkit.getScheduler().cancelTasks(ClanWars.getInstance());
                    Bukkit.getScheduler().runTaskLater(ClanWars.getInstance(), () -> {
                        Bukkit.broadcastMessage(ClanWars.getInstance().getCFG("MESSAGES.GAME_START_5_SECONDS"));
                        for (Player pl : Bukkit.getOnlinePlayers()) {
                            pl.playSound(pl.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 100F, 0.5F);
                        }
                    }, 20L * 5);
                    Bukkit.getScheduler().runTaskLater(ClanWars.getInstance(), () -> {
                        WorldBorder border = Bukkit.getWorld("game").getWorldBorder();
                        Bukkit.getScheduler().runTaskLater(ClanWars.getInstance(), () -> {
                            Location location = new Location(Bukkit.getWorld("game"), 0, 0, 0);
                                    for (Team team : Bukkit.getScoreboardManager().getMainScoreboard().getTeams()) {
                                        Set<String> players = team.getEntries();
                                        while (true) {
                                            int x = (int) (Math.random() * 2000) - 1000;
                                            int z = (int) (Math.random() * 2000) - 1000;
                                            int y = 150;
                                            // Получить блок в этой позиции
                                            Block block = Bukkit.getWorld("game").getBlockAt(x, y, z);
                                            if (block.getType() != Material.WATER && block.getType() != Material.CAVE_AIR && block.getType() != Material.STONE) {
                                                location.setX(x + 0.5);
                                                location.setY(y + 1);
                                                location.setZ(z + 0.5);
                                                break; // Успешный спавн
                                            }
                                        }
                                        for (String plyrs : players) {
                                            Player pl = Bukkit.getPlayer(plyrs); // Get the Player object for the player name
                                            if (pl != null && pl.isOnline()) {
                                                pl.setGameMode(GameMode.SURVIVAL);
                                                pl.setBedSpawnLocation(location, true);
                                                pl.teleport(location);
                                                pl.setInvulnerable(true);
                                                pl.sendTitle(getInstance().colorMaker("&c&lИгра началась"), getInstance().colorMaker("&eЦель: успеть развиться за 30 минут."), 10, 20, 10);
                                                pl.playSound(pl.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 100F, 1F);
                                                Bukkit.getScheduler().runTaskLater(ClanWars.getInstance(), () -> {
                                                    pl.setInvulnerable(false);
                                                    falldamage = false;
                                                }, 20L * 20);
                                            }
                                        }
                                    }
                        }, 20L * 10);
                        Bukkit.getScheduler().runTaskLater(ClanWars.getInstance(), () -> {
                            pvp = false;
                            Bukkit.broadcastMessage(ClanWars.getInstance().getCFG("MESSAGES.PVP_ON"));
                        }, 20L * 300);
                        game = true;
                        pvp = true;
                        falldamage = true;
                        Bukkit.getScheduler().runTaskLater(ClanWars.getInstance(), () -> {
                            Bukkit.broadcastMessage(ClanWars.getInstance().getCFG("MESSAGES.DEATHMATCH_TI") + 30 + ClanWars.getInstance().getCFG("MESSAGES.DEATHMATCH_IME"));
                            for (Player pl : Bukkit.getOnlinePlayers()) {
                                pl.playSound(pl.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 100F, 1F);
                            }
                        }, 20L * 10);
                        Bukkit.getScheduler().runTaskLater(ClanWars.getInstance(), () -> {
                            Bukkit.broadcastMessage(ClanWars.getInstance().getCFG("MESSAGES.DEATHMATCH_TI") + 20 + ClanWars.getInstance().getCFG("MESSAGES.DEATHMATCH_IME"));
                            for (Player pl : Bukkit.getOnlinePlayers()) {
                                pl.playSound(pl.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 100F, 1F);
                            }
                        }, 20L * 600);
                        Bukkit.getScheduler().runTaskLater(ClanWars.getInstance(), () -> {
                            Bukkit.broadcastMessage(ClanWars.getInstance().getCFG("MESSAGES.DEATHMATCH_TI") + 10 + ClanWars.getInstance().getCFG("MESSAGES.DEATHMATCH_IME"));
                            for (Player pl : Bukkit.getOnlinePlayers()) {
                                pl.playSound(pl.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 100F, 1F);
                            }
                        }, 20L * 1200);
                        Bukkit.getScheduler().runTaskLater(ClanWars.getInstance(), () -> {
                            Bukkit.broadcastMessage(ClanWars.getInstance().getCFG("MESSAGES.DEATHMATCH_TI") + 5 + ClanWars.getInstance().getCFG("MESSAGES.DEATHMATCH_IME"));
                            for (Player pl : Bukkit.getOnlinePlayers()) {
                                pl.playSound(pl.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 100F, 1F);
                            }
                        }, 20L * 1500);
                        Bukkit.getScheduler().runTaskLater(ClanWars.getInstance(), () -> {
                            Bukkit.broadcastMessage(ClanWars.getInstance().getCFG("MESSAGES.DEATHMATCH_TI") + 1 + ClanWars.getInstance().getCFG("MESSAGES.DEATHMATCH_IME"));
                            for (Player pl : Bukkit.getOnlinePlayers()) {
                                pl.playSound(pl.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 100F, 1F);
                            }
                        }, 20L * 1740);
                        Bukkit.getScheduler().runTaskLater(ClanWars.getInstance(), () -> {
                            fight = true;
                            Location location = new Location(Bukkit.getWorld("game"), 0, 0, 0);
                            for (Team team : Bukkit.getScoreboardManager().getMainScoreboard().getTeams()) {
                                Set<String> players = team.getEntries();
                                while (true) {
                                    int x = (int) (Math.random() * 301) - 150;
                                    int z = (int) (Math.random() * 301) - 150;
                                    int y = 150;
                                    // Получить блок в этой позиции
                                    Block block = Bukkit.getWorld("game").getBlockAt(x, y, z);
                                    if (block.getType() != Material.WATER && block.getType() != Material.CAVE_AIR && block.getType() != Material.STONE) {
                                        location.setX(x + 0.5);
                                        location.setY(y + 1);
                                        location.setZ(z + 0.5);
                                        break; // Успешный спавн
                                    }
                                }
                                for (String plyrs : players) {
                                    Player pl = Bukkit.getPlayer(plyrs); // Get the Player object for the player name
                                    if (pl != null && pl.isOnline()) {
                                        pl.setGameMode(GameMode.SURVIVAL);
                                        pl.setBedSpawnLocation(location, true);
                                        pl.teleport(location);
                                        pl.setInvulnerable(true);
                                        border.setSize(Integer.parseInt(getInstance().getCFG("CONFIG.BORDER_SIZE")));
                                        border.setSize(0, Integer.parseInt(getInstance().getCFG("CONFIG.BORDER_NARROWING_TIME")));
                                        pl.sendTitle(getInstance().colorMaker("&c&lДезматч начался"), getInstance().colorMaker("&eЦель: уничтожить вражескую команду."), 10, 20, 10);
                                        pl.playSound(pl.getLocation(), Sound.ENTITY_ENDER_DRAGON_AMBIENT, 100F, 1F);
                                        Bukkit.getScheduler().runTaskLater(ClanWars.getInstance(), () -> {
                                            pl.setInvulnerable(false);
                                            falldamage = false;
                                        }, 20L * 20);
                                    }
                                }
                            }
                        }, 20L * 1800);
                    }, 20L * 10);
                    break;
                }
                case "stop": {
                    if (!(p.hasPermission("clanwars.stop"))) {
                        sender.sendMessage(ClanWars.getInstance().getCFG("MESSAGES.NO_PERMS"));
                        break;
                    }
                    Bukkit.broadcastMessage(ClanWars.getInstance().getCFG("MESSAGES.STOPPED"));
                    pvp = false;
                    falldamage = false;
                    fight = false;
                    game = false;
                    for (Team team : Bukkit.getScoreboardManager().getMainScoreboard().getTeams()) {
                        team.unregister();
                    }
                    Bukkit.getScheduler().cancelTasks(ClanWars.getInstance());
                    for (Player pls : Bukkit.getOnlinePlayers()) {
                        Location location = new Location(Bukkit.getWorld("world"), 8,127,182);
                        pls.teleport(location);
                        pls.getInventory().clear();
                        pls.setHealth(20.0);
                        pls.setFoodLevel(20);
                        pls.setGameMode(GameMode.ADVENTURE);
                    }
                    aliveTeamCount = 0;
                    aliveTeamName = null;
                    ClanWars.getInstance().unloadWorld("game");
                    break;
                }
                case "forcepvpstart": {
                    if (!(p.hasPermission("clanwars.forcepvpstart"))) {
                        sender.sendMessage(ClanWars.getInstance().getCFG("MESSAGES.NO_PERMS"));
                        break;
                    }
                    if (game == false) {
                        sender.sendMessage(ClanWars.getInstance().getCFG("MESSAGES.GAME_NOT_STARTED"));
                        break;
                    }
                    Bukkit.getScheduler().cancelTasks(getInstance());
                    WorldBorder border = Bukkit.getWorld("game").getWorldBorder();
                    getInstance().fight = true;
                    Location location = new Location(Bukkit.getWorld("game"), 0, 0, 0);
                    for (Team team : Bukkit.getScoreboardManager().getMainScoreboard().getTeams()) {
                        while (true) {
                            int x = (int) (Math.random() * 301) - 150;
                            int z = (int) (Math.random() * 301) - 150;
                            int y = 150;
                            // Получить блок в этой позиции
                            Block block = Bukkit.getWorld("game").getBlockAt(x, y, z);
                            if (block.getType() != Material.WATER && block.getType() != Material.CAVE_AIR && block.getType() != Material.STONE) {
                                location.setX(x + 0.5);
                                location.setY(y + 1);
                                location.setZ(z + 0.5);
                                break; // Успешный спавн
                            }
                        }
                        Set<String> players = team.getEntries();
                        for (String plyrs : players) {
                            Player pl = Bukkit.getPlayer(plyrs); // Get the Player object for the player name
                            if (pl != null && pl.isOnline()) {
                                pl.setGameMode(GameMode.SURVIVAL);
                                pl.setBedSpawnLocation(location, true);
                                pl.teleport(location);
                                pl.setInvulnerable(true);
                                border.setSize(Integer.parseInt(getInstance().getCFG("CONFIG.BORDER_SIZE")));
                                border.setSize(0, Integer.parseInt(getInstance().getCFG("CONFIG.BORDER_NARROWING_TIME")));
                                pl.sendTitle(getInstance().colorMaker("&c&lДезматч начался"), getInstance().colorMaker("&eЦель: уничтожить вражескую команду."), 10, 20, 10);
                                pl.playSound(pl.getLocation(), Sound.ENTITY_ENDER_DRAGON_AMBIENT, 100F, 1F);
                                Bukkit.getScheduler().runTaskLater(ClanWars.getInstance(), () -> {
                                    pl.setInvulnerable(false);
                                    falldamage = false;
                                }, 20L * 20);
                            }
                        }
                    }
                    break;
                }
                case "createteam": {
                    if (args.length < 2) {
                        sender.sendMessage(ClanWars.getInstance().getCFG("MESSAGES.LOWARGS"));
                        break;
                    }
                    if (Bukkit.getScoreboardManager().getMainScoreboard().getTeam(args[1]) != null) {
                        sender.sendMessage(ClanWars.getInstance().getCFG("MESSAGES.TEAM_ALREADY_EXISTS"));
                        break;
                    }
                    ChatColor color = ChatColor.getByChar(ChatColor.translateAlternateColorCodes('&', "&" + args[2]).charAt(1));
                    Team team = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam(args[1]);
                    team.setAllowFriendlyFire(false);
                    team.setColor(color);
                    getInstance().saveTeams();
                    sender.sendMessage(ClanWars.getInstance().getCFG("MESSAGES.TEAMCREATED"));
                    break;
                }
                case "deleteteam": {
                    Scoreboard sb = p.getScoreboard();
                    Team team = sb.getPlayerTeam(p);
                    if (team == null) {
                        sender.sendMessage(ClanWars.getInstance().getCFG("MESSAGES.TEAM_NOT_EXIST"));
                        break;
                    }
                    if (args[1] == "confirm") {
                        sender.sendMessage(ClanWars.getInstance().getCFG("MESSAGES.TEAMDELETED"));
                        team.unregister();
                        getInstance().saveTeams();
                        break;
                    } else {
                        sender.sendMessage(ClanWars.getInstance().getCFG("MESSAGES.TEAMCONFIRM"));
                        break;
                    }
                }
                case "join": {
                    if (args.length < 1) {
                        sender.sendMessage(ClanWars.getInstance().getCFG("MESSAGES.LOWARGS"));
                        break;
                    }
                    Team team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(args[1]);
                    if (team == null) {
                        sender.sendMessage(ClanWars.getInstance().getCFG("MESSAGES.TEAM_NOT_FOUND"));
                        break;
                    }
                    team.addEntry(sender.getName());
                    getInstance().saveTeams();
                    sender.sendMessage(ClanWars.getInstance().getCFG("MESSAGES.JOINEDTEAM"));
                    break;
                }
                default: {
                    sender.sendMessage(ClanWars.getInstance().getCFG("MESSAGES.HELPMSG"));
                    break;
                }
            }
        }
        return false;
    }
}

package kz.itzhiti.clanwars.Commands;

import kz.itzhiti.clanwars.ClanWars;
import kz.itzhiti.clanwars.Configurations;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.json.simple.JSONValue;

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
                    if (!(p.hasPermission("clanwars.start"))) {
                        sender.sendMessage(ClanWars.getInstance().getCFG("MESSAGES.NO_PERMS"));
                        break;
                    }
                    Bukkit.broadcastMessage(ClanWars.getInstance().getCFG("MESSAGES.GAME_START"));
                    Bukkit.getScheduler().cancelTasks(ClanWars.getInstance());
                    WorldCreator worldCreator = new WorldCreator("game");
                    Bukkit.getScheduler().runTaskLater(ClanWars.getInstance(), () -> {
                        Bukkit.broadcastMessage(ClanWars.getInstance().getCFG("MESSAGES.GAME_START_5_SECONDS"));
                        Bukkit.createWorld(worldCreator);
                    }, 20L * 5);
                    Bukkit.getScheduler().runTaskLater(ClanWars.getInstance(), () -> {
                        WorldBorder border = Bukkit.getWorld("game").getWorldBorder();
                        Bukkit.getScheduler().runTaskLater(ClanWars.getInstance(), () -> {
                            for (Player pls : Bukkit.getOnlinePlayers()) {
                                pls.setGameMode(GameMode.SURVIVAL);
                                int x = (int) (Math.random() * 2000) - 1000;
                                int z = (int) (Math.random() * 2000) - 1000;
                                int y = pls.getWorld().getHighestBlockYAt(x, z);
                                Location location = new Location(Bukkit.getWorld("game"), x, y, z);
                                pls.teleport(location);
                                pls.sendTitle(getInstance().colorMaker("&c&lИгра началась"), getInstance().colorMaker("&eЦель: успеть развиться за 30 минут."), 3, 10, 5);
                                Bukkit.getScheduler().runTaskLater(ClanWars.getInstance(), () -> {
                                    falldamage = false;
                                }, 20L * 5);
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

                        }, 20L * 1790);
                        Bukkit.getScheduler().runTaskLater(ClanWars.getInstance(), () -> {
                            fight = true;
                            for (Player pls : Bukkit.getOnlinePlayers()) {
                                int x = (int) (Math.random() * 301) - 150;
                                int z = (int) (Math.random() * 301) - 150;
                                int y = pls.getWorld().getHighestBlockYAt(x, z);
                                Location location = new Location(Bukkit.getWorld("game"), x, y, z);
                                pls.teleport(location);
                                border.setSize(Integer.parseInt(getInstance().getCFG("CONFIG.BORDER_SIZE")));
                                border.setSize(0, Integer.parseInt(getInstance().getCFG("CONFIG.BORDER_NARROWING_TIME")));
                                pls.sendTitle(getInstance().colorMaker("&c&lДезматч начался"), getInstance().colorMaker("&eЦель: уничтожить вражескую команду."), 3, 10, 5);
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
                    Bukkit.getScheduler().cancelTasks(ClanWars.getInstance());
                    for (Player pls : Bukkit.getOnlinePlayers()) {
                        Location location = new Location(Bukkit.getWorld("world"), 26.5, 59.5, 5.5);
                        pls.teleport(location);
                        pls.getInventory().clear();
                        pls.setHealth(20.0);
                        pls.setFoodLevel(20);
                        pls.setGameMode(GameMode.ADVENTURE);
                    }
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
                    WorldBorder border = Bukkit.getWorld("game").getWorldBorder();
                    fight = true;
                    for (Player pls : Bukkit.getOnlinePlayers()) {
                        int x = (int) (Math.random() * 301) - 150;
                        int z = (int) (Math.random() * 301) - 150;
                        int y = pls.getWorld().getHighestBlockYAt(x, z);
                        Location location = new Location(Bukkit.getWorld("game"), x, y, z);
                        pls.teleport(location);
                        border.setSize(Integer.parseInt(getInstance().getCFG("CONFIG.BORDER_SIZE")));
                        border.setSize(0, Integer.parseInt(getInstance().getCFG("CONFIG.BORDER_NARROWING_TIME")));
                        pls.sendTitle(getInstance().colorMaker("&c&lДезматч начался"), getInstance().colorMaker("&eЦель: уничтожить вражескую команду."), 3, 10, 5);
                    }
                    break;
                }
                case "createteam": {
                    if (args.length == 0) {
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
                case "jointeam": {
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

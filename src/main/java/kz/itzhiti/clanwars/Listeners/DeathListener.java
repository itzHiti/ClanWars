package kz.itzhiti.clanwars.Listeners;

import kz.itzhiti.clanwars.ClanWars;
import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;

import java.util.Set;

import static kz.itzhiti.clanwars.ClanWars.*;

public class DeathListener implements Listener {

    Location loc;

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        World world = e.getEntity().getWorld();
        Player pl = e.getEntity().getPlayer();
        if (world.getName() != "world") {
            if (fight) {
                for (Team team : Bukkit.getScoreboardManager().getMainScoreboard().getTeams()) {
                    aliveTeamCount = 0;
                    int count = Bukkit.getScoreboardManager().getMainScoreboard().getObjective("AlivePlayers").getScore(team.getName()).getScore();
                    if (count > 0) {
                        aliveTeamCount++;
                        aliveTeamName = team.getName();
                    }
                }
                if (aliveTeamCount == 1) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (!(p.isDead() && p.getGameMode() == GameMode.SPECTATOR)) {
                            Team team = Bukkit.getScoreboardManager().getNewScoreboard().getEntryTeam(p.getName());
                            if (team != null) {
                                String teamm = team.getName();
                                aliveTeamName = teamm;
                            }
                        }
                        pl.setGameMode(GameMode.SPECTATOR);
                        loc = pl.getLocation();
                        pl.sendMessage(ClanWars.getInstance().getCFG("MESSAGES.DEATH"));
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 100F, 0.5F);
                            pvp = false;
                            falldamage = false;
                            fight = false;
                            game = false;
                        }
                        for (Team allteams : Bukkit.getScoreboardManager().getMainScoreboard().getTeams()) {
                            Set<String> players = allteams.getEntries();
                            String subt = "";
                            for (String plyrs : players) {
                                Player pp = Bukkit.getPlayer(plyrs); // Get the Player object for the player name
                                Firework firework = Bukkit.getServer().getWorld("game").spawn(pp.getLocation(), Firework.class);
                                FireworkMeta meta = firework.getFireworkMeta();
                                meta.setPower(1);
                                meta.addEffect(FireworkEffect.builder()
                                        .withColor(Color.AQUA)
                                        .with(FireworkEffect.Type.BURST)
                                        .build());
                                firework.setFireworkMeta(meta);
                                if (allteams.getName() == aliveTeamName) {
                                    subt = getInstance().colorMaker("&a&lВы победили!");
                                } else {
                                    subt = getInstance().colorMaker("&c&lВы проиграли!");
                                }
                                pp.sendTitle(getInstance().colorMaker("&6&lПобедила команда ") + aliveTeamName, subt, 10, 20, 10);
                            }
                        }
                        Bukkit.getScheduler().cancelTasks(ClanWars.getInstance());
                        Bukkit.getScheduler().runTaskLater(ClanWars.getInstance(), () -> {
                            for (Player pls : Bukkit.getOnlinePlayers()) {
                                Location location = new Location(Bukkit.getWorld("world"), 8, 127, 182);
                                pls.teleport(location);
                                pls.getInventory().clear();
                                pls.setHealth(20.0);
                                pls.setFoodLevel(20);
                                pls.setGameMode(GameMode.ADVENTURE);
                            }
                            ClanWars.getInstance().unloadWorld("game");
                            for (Team tm : Bukkit.getScoreboardManager().getMainScoreboard().getTeams()) {
                                tm.unregister();
                            }
                            aliveTeamCount = 0;
                            aliveTeamName = null;
                        }, 20L * 15);
                    }
                }
                else{
                        pl.setGameMode(GameMode.SPECTATOR);
                        loc = pl.getLocation();
                        pl.sendMessage(ClanWars.getInstance().getCFG("MESSAGES.DEATH"));
                    }
                }

            } else {
                ;
            }
        }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
            Player p = e.getPlayer();
            p.teleport(loc);
            p.setInvulnerable(true);
            falldamage = true;
            Bukkit.getScheduler().runTaskLater(ClanWars.getInstance(), () -> {
                p.setInvulnerable(false);
                falldamage = false;
            }, 20L * 20);
    }
}
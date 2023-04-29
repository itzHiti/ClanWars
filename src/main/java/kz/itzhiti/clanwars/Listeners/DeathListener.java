package kz.itzhiti.clanwars.Listeners;

import kz.itzhiti.clanwars.ClanWars;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import static kz.itzhiti.clanwars.ClanWars.fight;

public class DeathListener implements Listener {

    Location loc;

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        World world = e.getEntity().getWorld();
        Player pl = e.getEntity().getPlayer();
        if (world.getName() != "world") {
            if (fight) {
                pl.setGameMode(GameMode.SPECTATOR);
                loc = pl.getLocation();
                pl.sendMessage(ClanWars.getInstance().getCFG("MESSAGES.DEATH"));
            } else {
                ;
            }
        }
    }
    @EventHandler
    public void onRespawn (PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        p.teleport(loc);
    }
 }
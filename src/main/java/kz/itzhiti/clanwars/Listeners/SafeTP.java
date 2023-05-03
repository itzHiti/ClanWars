package kz.itzhiti.clanwars.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import static kz.itzhiti.clanwars.ClanWars.falldamage;
import static kz.itzhiti.clanwars.ClanWars.pvp;

public class SafeTP implements Listener {
    @EventHandler
    public void onDamage (EntityDamageByEntityEvent edmg) {
        if (pvp == true) {
            if (edmg.getEntity() instanceof Player) {
                if (edmg.getDamager() instanceof Player)
                    edmg.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onFallDamage (EntityDamageEvent e) {
        if (falldamage == true) {
            if (e.getEntity() instanceof Player) {
                    e.setCancelled(true);
            }
        }
    }
}

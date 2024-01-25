package dev.siea.inventorycap.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class ItemPickupListeners {
    public static void onPlayerPickUpItem(EntityPickupItemEvent e){
        if (!(e.getEntity()instanceof Player)) return;
        Player player = (Player) e.getEntity();
    }
}

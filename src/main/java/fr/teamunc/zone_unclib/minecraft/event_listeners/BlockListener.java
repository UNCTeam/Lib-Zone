package fr.teamunc.zone_unclib.minecraft.event_listeners;

import fr.teamunc.zone_unclib.ZoneLib;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class BlockListener implements Listener {

    @EventHandler(priority = org.bukkit.event.EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();

        if (!ZoneLib.isInit()) return;

        if (!ZoneLib.getZoneController().canInteract(player, block.getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = org.bukkit.event.EventPriority.HIGHEST)
    public void onPlaceBlockEvent(BlockPlaceEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();

        if (!ZoneLib.isInit()) return;

        if (!ZoneLib.getZoneController().canInteract(player, block.getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = org.bukkit.event.EventPriority.HIGHEST)
    public void onInteractEvent(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        Player player = event.getPlayer();

        if (!ZoneLib.isInit() || block == null) return;


        if (!ZoneLib.getZoneController().canInteract(player, block.getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = org.bukkit.event.EventPriority.HIGHEST)
    public void onEntityInteractEvent(PlayerInteractEntityEvent event) {
        Location loc = event.getRightClicked().getLocation();
        Player player = event.getPlayer();
        if (!ZoneLib.isInit()) return;

        if (!ZoneLib.getZoneController().canInteract(player, loc)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = org.bukkit.event.EventPriority.HIGHEST)
    public void onEntityInteractEvent(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        Location loc = event.getEntity().getLocation();
        Player player = (Player) event.getDamager();
        if (!ZoneLib.isInit()) return;

        if (!ZoneLib.getZoneController().canInteract(player, loc)) {
            event.setCancelled(true);
        }
    }
}

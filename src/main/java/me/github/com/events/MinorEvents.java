package me.github.com.events;

import me.github.com.IdentityCardPlugin;
import me.github.com.object.IdentityManager;
import me.github.com.object.InventoryManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

public class MinorEvents implements Listener {

    private final IdentityCardPlugin plugin;

    public MinorEvents(IdentityCardPlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        InventoryManager inventory = new InventoryManager(plugin);

        if(IdentityCardPlugin.getIdentityManager().isPlayerRegistered(player)) {
            if(IdentityCardPlugin.getIdentityManager().areAllInfoValid(player)) {
                return;
            }
            IdentityCardPlugin.getIdentityManager().getPlayersInSetup().add(player);
            player.openInventory(inventory.getSetupGUI(player));
            return;
        }
        IdentityCardPlugin.getIdentityManager().getPlayersInSetup().add(player);
        IdentityCardPlugin.getIdentityManager().registerPlayer(player);
        player.openInventory(inventory.getSetupGUI(player));

    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        if(IdentityCardPlugin.getIdentityManager().getPlayersInSetup().contains(event.getPlayer())){
            IdentityCardPlugin.getIdentityManager().getPlayersInSetup().remove(event.getPlayer());
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event){
        if(IdentityCardPlugin.getIdentityManager().getPlayersInSetup().contains(event.getPlayer())){
            Inventory inventory = event.getInventory();

            Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    event.getPlayer().openInventory(inventory);
                }
            },1);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        if(IdentityCardPlugin.getIdentityManager().getPlayersInSetup().contains(event.getPlayer())){
            event.setCancelled(true);
        }
    }

}

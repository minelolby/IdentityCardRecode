package me.github.com.object;

import me.github.com.IdentityCardPlugin;
import net.laxelib.com.utils.ColorsAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatEvent implements Listener {

    private final IdentityCardPlugin plugin;

    public ChatEvent(IdentityCardPlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onMessage(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();

        if(IdentityCardPlugin.getIdentityManager().getNameSetup().contains(player)){
            event.setCancelled(true);

            if(isNameValid(event.getMessage())){
                player.sendMessage(ColorsAPI.color(plugin.getMessagesConfiguration().getString("Messages.too-long")));
            }else {
                IdentityCardPlugin.getIdentityManager().setPlayerName(player, event.getMessage());
                player.sendMessage(ColorsAPI.color(plugin.getMessagesConfiguration().getString("Messages.name-saved").replace("%name%", event.getMessage())));
            }
            IdentityCardPlugin.getIdentityManager().getNameSetup().remove(player);
            InventoryManager inventoryManager = new InventoryManager(plugin);

            Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    player.openInventory(inventoryManager.getSetupGUI(player));
                }
            },1);
            IdentityCardPlugin.getIdentityManager().getNameSetup().remove(player);
            return;
        }

        if(IdentityCardPlugin.getIdentityManager().getSurnameSetup().contains(player)){
            event.setCancelled(true);

            if(isNameValid(event.getMessage())){
                player.sendMessage(ColorsAPI.color(plugin.getMessagesConfiguration().getString("Messages.too-long")));
            }else {
                IdentityCardPlugin.getIdentityManager().setPlayerSurname(player, event.getMessage());
                player.sendMessage(ColorsAPI.color(plugin.getMessagesConfiguration().getString("Messages.surname-saved").replace("%surname%", event.getMessage())));
            }
            IdentityCardPlugin.getIdentityManager().getSurnameSetup().remove(player);
            InventoryManager inventoryManager = new InventoryManager(plugin);

            Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    player.openInventory(inventoryManager.getSetupGUI(player));
                }
            },1);
            IdentityCardPlugin.getIdentityManager().getSurnameSetup().remove(player);
            return;
        }

    }

    private boolean isNameValid(String name) {
        return name.length() > 20 && name.contains(" ");
    }

}

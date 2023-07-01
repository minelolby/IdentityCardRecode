package me.github.com.events;

import me.github.com.IdentityCardPlugin;
import me.github.com.object.InventoryManager;
import me.github.com.utils.GenderType;
import net.laxelib.com.utils.ColorsAPI;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryEvent implements Listener {

    private final IdentityCardPlugin plugin;

    public InventoryEvent(IdentityCardPlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onSetupClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if(event.getView().getTitle().equals(ColorsAPI.color(plugin.getGuiConfiguration().getString("GUI.SetupGUI.title")))) {
            event.setCancelled(true);
            if(event.getCurrentItem() == null || event.getCurrentItem().getItemMeta() == null) return;

            switch (event.getCurrentItem().getItemMeta().getLocalizedName()){
                case "male":
                    if(isSameGender(player, GenderType.MALE)){
                        player.sendMessage(ColorsAPI.color(plugin.getMessagesConfiguration().getString("Messages.gender-already-selected")));
                        return;
                    }
                    IdentityCardPlugin.getIdentityManager().setPlayerGender(player, GenderType.MALE);
                    player.sendMessage(ColorsAPI.color(plugin.getMessagesConfiguration().getString("Messages.gender-selected").replace("%gender%", GenderType.MALE.getDisplayName())));
                    updateInventory(player);
                    break;
                case "female":
                    if(isSameGender(player, GenderType.FEMALE)){
                        player.sendMessage(ColorsAPI.color(plugin.getMessagesConfiguration().getString("Messages.gender-already-selected")));
                        return;
                    }
                    IdentityCardPlugin.getIdentityManager().setPlayerGender(player, GenderType.FEMALE);
                    player.sendMessage(ColorsAPI.color(plugin.getMessagesConfiguration().getString("Messages.gender-selected").replace("%gender%", GenderType.FEMALE.getDisplayName())));
                    updateInventory(player);
                    break;
                case "age":
                    InventoryManager inventoryManager = new InventoryManager(plugin);
                    IdentityCardPlugin.getIdentityManager().getPlayersInSetup().remove(player);
                    player.openInventory(inventoryManager.getAgeGUI(player));
                    IdentityCardPlugin.getIdentityManager().getPlayersInSetup().add(player);
                    break;
                case "name":
                    IdentityCardPlugin.getIdentityManager().getPlayersInSetup().remove(player);
                    player.closeInventory();
                    player.sendMessage(ColorsAPI.color(plugin.getMessagesConfiguration().getString("Messages.type-name")));
                    IdentityCardPlugin.getIdentityManager().getNameSetup().add(player);
                    IdentityCardPlugin.getIdentityManager().getPlayersInSetup().add(player);
                    break;
                case "surname":
                    IdentityCardPlugin.getIdentityManager().getPlayersInSetup().remove(player);
                    player.closeInventory();
                    player.sendMessage(ColorsAPI.color(plugin.getMessagesConfiguration().getString("Messages.type-surname")));
                    IdentityCardPlugin.getIdentityManager().getSurnameSetup().add(player);
                    IdentityCardPlugin.getIdentityManager().getPlayersInSetup().add(player);
                    break;
                case "done":
                    if(!IdentityCardPlugin.getIdentityManager().areAllInfoValid(player)){
                        player.sendMessage(ColorsAPI.color(plugin.getMessagesConfiguration().getString("Messages.save-all-info")));
                        return;
                    }
                    IdentityCardPlugin.getIdentityManager().getPlayersInSetup().remove(player);
                    player.closeInventory();
                    if(plugin.getConfig().getBoolean("Settings.give-ic-item")){
                        player.getInventory().addItem(IdentityCardPlugin.getIdentityManager().getIdentityCardItem(player));
                    }
                    player.sendMessage(ColorsAPI.color(plugin.getMessagesConfiguration().getString("Messages.info-saved")));
                    break;
            }
        }
    }

    private void updateInventory(Player player){
        IdentityCardPlugin.getIdentityManager().getPlayersInSetup().remove(player);
        InventoryManager inventoryManager = new InventoryManager(plugin);
        player.openInventory(inventoryManager.getSetupGUI(player));
        IdentityCardPlugin.getIdentityManager().getPlayersInSetup().add(player);
    }

    @EventHandler
    public void onAgeClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if(event.getView().getTitle().equals(ColorsAPI.color(plugin.getGuiConfiguration().getString("GUI.AgeGUI.title")))) {
            event.setCancelled(true);
            if(event.getCurrentItem() == null || event.getCurrentItem().getItemMeta() == null) return;

            switch (event.getCurrentItem().getItemMeta().getLocalizedName()){
                case "decrease":
                    int value1 = IdentityCardPlugin.getIdentityManager().getPlayerAge(player)-1;
                    if(isValidAge(value1)){
                        IdentityCardPlugin.getIdentityManager().setPlayerAge(player, value1);
                        reopenAgeGUI(player);
                    }else {
                        player.sendMessage(ColorsAPI.color(plugin.getMessagesConfiguration().getString("Messages.remove-other-year")));
                    }
                    break;
                case "done":
                    IdentityCardPlugin.getIdentityManager().getPlayersInSetup().remove(player);
                    InventoryManager inventoryManager = new InventoryManager(plugin);
                    player.openInventory(inventoryManager.getSetupGUI(player));
                    IdentityCardPlugin.getIdentityManager().getPlayersInSetup().add(player);
                    break;
                case "increase":
                    int value2 = IdentityCardPlugin.getIdentityManager().getPlayerAge(player)+1;
                    if(isValidAge(value2)) {
                        IdentityCardPlugin.getIdentityManager().setPlayerAge(player, value2);
                        reopenAgeGUI(player);
                    }else {
                        player.sendMessage(ColorsAPI.color(plugin.getMessagesConfiguration().getString("Messages.add-other-year")));
                    }
                    break;
            }
        }
    }

    private boolean isValidAge(int value){
        if(value <= plugin.getConfig().getInt("Settings.max-age") && value >= plugin.getConfig().getInt("Settings.min-age")){
            return true;
        }
        return false;
    }

    private void reopenAgeGUI(Player player){
        IdentityCardPlugin.getIdentityManager().getPlayersInSetup().remove(player);
        InventoryManager inventoryManager = new InventoryManager(plugin);
        player.openInventory(inventoryManager.getAgeGUI(player));
        IdentityCardPlugin.getIdentityManager().getPlayersInSetup().add(player);
    }

    private boolean isSameGender(Player player, GenderType newGender){
        if(IdentityCardPlugin.getIdentityManager().getPlayerGender(player) == null){
            return false;
        }
        if(IdentityCardPlugin.getIdentityManager().getPlayerGender(player) == newGender){
            return true;
        }
        return false;
    }

}

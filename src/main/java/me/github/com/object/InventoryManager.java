package me.github.com.object;

import me.github.com.IdentityCardPlugin;
import me.github.com.utils.GenderType;
import net.laxelib.com.utils.ColorsAPI;
import net.laxelib.com.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InventoryManager {

    private final IdentityCardPlugin plugin;

    public InventoryManager(IdentityCardPlugin plugin){
        this.plugin = plugin;
    }

    public Inventory getSetupGUI(Player player){
        Inventory inventory = Bukkit.createInventory(null, plugin.getGuiConfiguration().getInt("GUI.SetupGUI.slots"), ColorsAPI.color(plugin.getGuiConfiguration().getString("GUI.SetupGUI.title")));

        for(String s : plugin.getGuiConfiguration().getConfigurationSection("GUI.SetupGUI.Items", false)){
            List<String> lore = ColorsAPI.colorStringList(plugin.getGuiConfiguration().getStringList("GUI.SetupGUI.Items." + s + ".lore"));

            for (int i = 0; i < lore.size(); i++) {
                String notSelected = "§cNot selected";

                if(lore.get(i).contains("%age%")){
                    lore.set(i, lore.get(i).replace("%age%", String.valueOf(IdentityCardPlugin.getIdentityManager().getPlayerAge(player))));
                }
                if(lore.get(i).contains("%gender%")){
                    if(IdentityCardPlugin.getIdentityManager().getPlayerGender(player) == null){
                        lore.set(i, lore.get(i).replace("%gender%", notSelected));
                    }else {
                        lore.set(i, lore.get(i).replace("%gender%", IdentityCardPlugin.getIdentityManager().getPlayerGender(player).getDisplayName()));
                    }
                }
                if(lore.get(i).contains("%name%")){
                    if(IdentityCardPlugin.getIdentityManager().getPlayerName(player) == null){
                        lore.set(i, lore.get(i).replace("%name%", notSelected));
                    }else {
                        lore.set(i, lore.get(i).replace("%name%", IdentityCardPlugin.getIdentityManager().getPlayerName(player)));
                    }
                }
                if(lore.get(i).contains("%surname%")){
                    if(IdentityCardPlugin.getIdentityManager().getPlayerSurname(player) == null){
                        lore.set(i, lore.get(i).replace("%surname%", notSelected));
                    }else {
                        lore.set(i,  lore.get(i).replace("%surname%", IdentityCardPlugin.getIdentityManager().getPlayerSurname(player)));
                    }
                }
            }
            inventory.setItem(
                    plugin.getGuiConfiguration().getInt("GUI.SetupGUI.Items." + s + ".slot"),
                    new ItemBuilder(Material.valueOf(plugin.getGuiConfiguration().getString("GUI.SetupGUI.Items." + s + ".material")))
                            .setName(ColorsAPI.color(plugin.getGuiConfiguration().getString("GUI.SetupGUI.Items." + s + ".name")))
                            .setLore(lore)
                            .setLocalizedName(s)
                            .addGlowing(plugin.getGuiConfiguration().getBoolean("GUI.SetupGUI.Items." + s + ".glowing"))
                            .getItemStack()
            );
        }
        return inventory;
    }

    public Inventory getAgeGUI(Player player){
        Inventory inventory = Bukkit.createInventory(null, 9, ColorsAPI.color(plugin.getGuiConfiguration().getString("GUI.AgeGUI.title")));

        for(String s : plugin.getGuiConfiguration().getConfigurationSection("GUI.AgeGUI.Items", false)){
            List<String> lore = ColorsAPI.colorStringList(plugin.getGuiConfiguration().getStringList("GUI.AgeGUI.Items." + s + ".lore"));

            for (int i = 0; i < lore.size(); i++) {
                lore.set(i, lore.get(i).replace("%age%", String.valueOf(IdentityCardPlugin.getIdentityManager().getPlayerAge(player))));
            }

            inventory.setItem(
                    plugin.getGuiConfiguration().getInt("GUI.AgeGUI.Items." + s + ".slot"),
                    new ItemBuilder(Material.valueOf(plugin.getGuiConfiguration().getString("GUI.AgeGUI.Items." + s + ".material")))
                            .setName(ColorsAPI.color(plugin.getGuiConfiguration().getString("GUI.AgeGUI.Items." + s + ".name")))
                            .setLore(lore)
                            .setLocalizedName(s)
                            .addGlowing(plugin.getGuiConfiguration().getBoolean("GUI.AgeGUI.Items." + s + ".glowing"))
                            .getItemStack()
            );
        }
        return inventory;
    }


}

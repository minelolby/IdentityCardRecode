package me.github.com.commands;

import me.github.com.IdentityCardPlugin;
import me.github.com.object.InventoryManager;
import net.laxelib.com.utils.ColorsAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminCommand implements CommandExecutor {

    private final IdentityCardPlugin plugin;

    public AdminCommand(IdentityCardPlugin plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)){
            return true;
        }
        Player player = (Player) sender;

        if(!player.hasPermission("identitycard.admin")){
            player.sendMessage(ColorsAPI.color(plugin.getMessagesConfiguration().getString("Messages.no-permission")));
            return true;
        }

        if(args.length == 0){
            for(String s : ColorsAPI.colorStringList(plugin.getMessagesConfiguration().getStringList("Messages.Admin-format"))){
                player.sendMessage(s);
            }
            return true;
        }

        if(args[0].toLowerCase().equals("reset")){
            if(args.length == 1){
                player.sendMessage(ColorsAPI.color(plugin.getMessagesConfiguration().getString("Messages.player-missing")));
                return true;
            }
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

            IdentityCardPlugin.getIdentityManager().resetPlayer(target);

            if(Bukkit.getPlayer(target.getName()) != null){
                Player p = Bukkit.getPlayer(target.getName());

                InventoryManager inventoryManager = new InventoryManager(plugin);
                IdentityCardPlugin.getIdentityManager().getPlayersInSetup().add(p);
                IdentityCardPlugin.getIdentityManager().registerPlayer(p);
                p.openInventory(inventoryManager.getSetupGUI(p));
            }

            player.sendMessage(ColorsAPI.color(plugin.getMessagesConfiguration().getString("Messages.identity-reset").replace("%name%", target.getName())));
            return true;
        }
        if(args[0].toLowerCase().equals("reload")){
            plugin.getGuiConfiguration().reload();
            plugin.getMessagesConfiguration().reload();
            plugin.reloadConfig();
            player.sendMessage(ColorsAPI.color(plugin.getMessagesConfiguration().getString("Messages.plugin-reloaded")));
            return true;
        }
        if(args[0].toLowerCase().equals("version")){
            player.sendMessage("§6Plugin version: 1.5-SNAPSHOT");
            return true;
        }

        return false;
    }
}

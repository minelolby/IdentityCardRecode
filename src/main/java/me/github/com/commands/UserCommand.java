package me.github.com.commands;

import me.github.com.IdentityCardPlugin;
import net.laxelib.com.utils.ColorsAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class UserCommand implements CommandExecutor {

    private final IdentityCardPlugin plugin;

    public UserCommand(IdentityCardPlugin plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)){
            return true;
        }
        Player player = (Player) sender;

        if(args.length == 0){
            sendStatsMessage(player);
            return true;
        }
        if(!player.hasPermission("identitycard.see.other")){
            player.sendMessage(ColorsAPI.color(plugin.getMessagesConfiguration().getString("Messages.no-permission-to-see-other-stats")));
            return true;
        }
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

        if(!IdentityCardPlugin.getIdentityManager().isPlayerRegistered(target)){
            player.sendMessage(ColorsAPI.color(plugin.getMessagesConfiguration().getString("Messages.no-identity")));
            return true;
        }
        sendStatsMessage(player);

        return false;
    }

    private void sendStatsMessage(Player player){
        List<String> result = ColorsAPI.colorStringList(plugin.getMessagesConfiguration().getStringList("Messages.View-format"));

        for(int i = 0; i < result.size(); i++){
            if(result.get(i).contains("%player%")){
                result.set(i , result.get(i).replace("%player%", player.getName()));
            }
            if(result.get(i).contains("%name%")){
                result.set(i , result.get(i).replace("%name%", IdentityCardPlugin.getIdentityManager().getPlayerName(player)));
            }
            if(result.get(i).contains("%surname%")){
                result.set(i , result.get(i).replace("%surname%", IdentityCardPlugin.getIdentityManager().getPlayerSurname(player)));
            }
            if(result.get(i).contains("%gender%")){
                result.set(i , result.get(i).replace("%gender%", IdentityCardPlugin.getIdentityManager().getPlayerGender(player).getDisplayName()));
            }
            if(result.get(i).contains("%age%")){
                result.set(i , result.get(i).replace("%age%", String.valueOf(IdentityCardPlugin.getIdentityManager().getPlayerAge(player))));
            }
        }
        for(String s : result){
            player.sendMessage(s);
        }
    }

}

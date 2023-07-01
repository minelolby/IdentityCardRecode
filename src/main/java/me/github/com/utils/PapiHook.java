package me.github.com.utils;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.github.com.IdentityCardPlugin;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PapiHook extends PlaceholderExpansion {

    private final IdentityCardPlugin plugin;

    public PapiHook(IdentityCardPlugin plugin){
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "identitycard";
    }

    @Override
    public @NotNull String getAuthor() {
        return "TairoDev";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.5-SNAPSHOT";
    }

    public String onPlaceholderRequest(Player player, String identifier){
        if(identifier.equals("name")){
            return IdentityCardPlugin.getIdentityManager().getPlayerName(player);
        }else if(identifier.equals("surname")){
            return IdentityCardPlugin.getIdentityManager().getPlayerSurname(player);
        }else if(identifier.equals("age")){
            return String.valueOf(IdentityCardPlugin.getIdentityManager().getPlayerAge(player));
        }else if(identifier.equals("gender")){
            return IdentityCardPlugin.getIdentityManager().getPlayerGender(player).getDisplayName();
        }
        return null;
    }

}

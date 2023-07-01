package me.github.com.object;

import lombok.Getter;
import lombok.SneakyThrows;
import me.github.com.IdentityCardPlugin;
import me.github.com.utils.GenderType;
import net.laxelib.com.utils.ColorsAPI;
import net.laxelib.com.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class IdentityManager {

    private final IdentityCardPlugin plugin;

    @Getter private List<Player> playersInSetup;
    @Getter private List<Player> nameSetup;
    @Getter private List<Player> surnameSetup;

    public IdentityManager(IdentityCardPlugin plugin){
        this.plugin = plugin;
        this.playersInSetup = new ArrayList<>();
        this.nameSetup = new ArrayList<>();
        this.surnameSetup = new ArrayList<>();
    }

    @SneakyThrows
    public void registerPlayer(Player player){
        try(Connection connection = plugin.getMySQL().getHikari().getConnection();
            ResultSet rs = connection.prepareStatement("SELECT * FROM player_data WHERE player_uuid = '" + player.getUniqueId() + "';").executeQuery()){
            if(!rs.next()){
                PreparedStatement ps = connection.prepareStatement("INSERT INTO player_data (player_name,player_uuid,gender,age,name,surname) VALUES (?,?,?,?,?,?);");
                ps.setString(1, player.getName());
                ps.setString(2, player.getUniqueId().toString());
                ps.setString(3, null);
                ps.setString(4, null);
                ps.setString(5, null);
                ps.setString(6, null);
                ps.executeUpdate();
            }
        }
    }

    @SneakyThrows
    public void resetPlayer(OfflinePlayer player){
        try(Connection connection = plugin.getMySQL().getHikari().getConnection();
            ResultSet rs = connection.prepareStatement("SELECT * FROM player_data WHERE player_uuid = '" + player.getUniqueId() + "';").executeQuery()){
            if(rs.next()){
                PreparedStatement ps = connection.prepareStatement("DELETE FROM `player_data` WHERE player_uuid = '" + player.getUniqueId() + "';");
                ps.executeUpdate();
            }
        }
    }

    @SneakyThrows
    public boolean isPlayerRegistered(OfflinePlayer player){
        try(Connection connection = plugin.getMySQL().getHikari().getConnection();
            ResultSet rs = connection.prepareStatement("SELECT * FROM player_data WHERE player_uuid = '" + player.getUniqueId() + "';").executeQuery()){
            if(rs.next()){
                return true;
            }
        }
        return false;
    }

    @SneakyThrows
    public void setPlayerGender(Player player, GenderType genderType){
        try(Connection connection = plugin.getMySQL().getHikari().getConnection();
            ResultSet rs = connection.prepareStatement("SELECT * FROM player_data WHERE player_uuid = '" + player.getUniqueId() + "';").executeQuery()){
            if(rs.next()){
                PreparedStatement ps = connection.prepareStatement("UPDATE player_data SET gender = '" + genderType.name() + "' WHERE player_uuid = '" + player.getUniqueId() + "';");
                ps.executeUpdate();
            }
        }
    }

    @SneakyThrows
    public void setPlayerAge(Player player, int age){
        try(Connection connection = plugin.getMySQL().getHikari().getConnection();
            ResultSet rs = connection.prepareStatement("SELECT * FROM player_data WHERE player_uuid = '" + player.getUniqueId() + "';").executeQuery()){
            if(rs.next()){
                PreparedStatement ps = connection.prepareStatement("UPDATE player_data SET age = '" + age + "' WHERE player_uuid = '" + player.getUniqueId() + "';");
                ps.executeUpdate();
            }
        }
    }

    @SneakyThrows
    public void setPlayerName(Player player, String name){
        try(Connection connection = plugin.getMySQL().getHikari().getConnection();
            ResultSet rs = connection.prepareStatement("SELECT * FROM player_data WHERE player_uuid = '" + player.getUniqueId() + "';").executeQuery()){
            if(rs.next()){
                PreparedStatement ps = connection.prepareStatement("UPDATE player_data SET name = '" + name + "' WHERE player_uuid = '" + player.getUniqueId() + "';");
                ps.executeUpdate();
            }
        }
    }

    @SneakyThrows
    public void setPlayerSurname(Player player, String name){
        try(Connection connection = plugin.getMySQL().getHikari().getConnection();
            ResultSet rs = connection.prepareStatement("SELECT * FROM player_data WHERE player_uuid = '" + player.getUniqueId() + "';").executeQuery()){
            if(rs.next()){
                PreparedStatement ps = connection.prepareStatement("UPDATE player_data SET surname = '" + name + "' WHERE player_uuid = '" + player.getUniqueId() + "';");
                ps.executeUpdate();
            }
        }
    }

    public boolean areAllInfoValid(Player player){
        if(getPlayerGender(player) != null && getPlayerAge(player) != 0 && getPlayerName(player) != null && getPlayerSurname(player) != null){
            return true;
        }
        return false;
    }

    @SneakyThrows
    public GenderType getPlayerGender(Player player){
        try(Connection connection = plugin.getMySQL().getHikari().getConnection();
            ResultSet rs = connection.prepareStatement("SELECT * FROM player_data WHERE player_uuid = '" + player.getUniqueId() + "';").executeQuery()){
            if(rs.next()){
                if(rs.getString("gender") == null){
                    return null;
                }
                return GenderType.valueOf(rs.getString("gender"));
            }
        }
        return null;
    }

    @SneakyThrows
    public int getPlayerAge(Player player){
        try(Connection connection = plugin.getMySQL().getHikari().getConnection();
            ResultSet rs = connection.prepareStatement("SELECT * FROM player_data WHERE player_uuid = '" + player.getUniqueId() + "';").executeQuery()){
            if(rs.next()){
                int age = rs.getInt("age");
                if(age == 0){
                    return 10;
                }
                return rs.getInt("age");
            }
        }
        return 0;
    }

    @SneakyThrows
    public String getPlayerName(Player player){
        try(Connection connection = plugin.getMySQL().getHikari().getConnection();
            ResultSet rs = connection.prepareStatement("SELECT * FROM player_data WHERE player_uuid = '" + player.getUniqueId() + "';").executeQuery()){
            if(rs.next()){
                return rs.getString("name");
            }
        }
        return null;
    }

    @SneakyThrows
    public String getPlayerSurname(Player player){
        try(Connection connection = plugin.getMySQL().getHikari().getConnection();
            ResultSet rs = connection.prepareStatement("SELECT * FROM player_data WHERE player_uuid = '" + player.getUniqueId() + "';").executeQuery()){
            if(rs.next()){
                return rs.getString("surname");
            }
        }
        return null;
    }

    public ItemStack getIdentityCardItem(Player player){
        List<String> lore = ColorsAPI.colorStringList(plugin.getConfig().getStringList("Item.lore"));

        for(int i = 0; i < lore.size(); i++){
            if(lore.get(i).contains("%name%")){
                lore.set(i, lore.get(i).replace("%name%", getPlayerName(player)));
            }
            if(lore.get(i).contains("%surname%")){
                lore.set(i, lore.get(i).replace("%surname%", getPlayerSurname(player)));
            }
            if(lore.get(i).contains("%age%")){
                lore.set(i, lore.get(i).replace("%age%", String.valueOf(getPlayerAge(player))));
            }
            if(lore.get(i).contains("%gender%")){
                lore.set(i, lore.get(i).replace("%gender%", ChatColor.stripColor(getPlayerGender(player).getDisplayName())));
            }
        }
        return new ItemBuilder(Material.valueOf(plugin.getConfig().getString("Item.material")))
                .setName(ColorsAPI.color(plugin.getConfig().getString("Item.name")))
                .setLore(lore)
                .addGlowing(plugin.getConfig().getBoolean("Item.glowing"))
                .getItemStack();

    }

}

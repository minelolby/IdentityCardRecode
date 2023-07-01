package me.github.com;

import lombok.Getter;
import me.github.com.commands.AdminCommand;
import me.github.com.commands.UserCommand;
import me.github.com.events.InventoryEvent;
import me.github.com.events.MinorEvents;
import me.github.com.object.ChatEvent;
import me.github.com.object.IdentityManager;
import me.github.com.utils.MySQL;
import me.github.com.utils.PapiHook;
import net.laxelib.com.LaxeLib;
import net.laxelib.com.configuration.YMLConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class IdentityCardPlugin extends JavaPlugin {

    private LaxeLib laxeLib;

    @Getter private MySQL mySQL;
    private static IdentityManager identityManager;

    @Getter YMLConfiguration guiConfiguration;
    @Getter static YMLConfiguration messagesConfiguration;

    @Override
    public void onEnable() {
        this.laxeLib = new LaxeLib(this);

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        this.mySQL = new MySQL(this);
        mySQL.connect();

        this.identityManager = new IdentityManager(this);

        setupConfigurations();
        registerEvents();
        registerCommands();

        if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")){
            new PapiHook(this).register();
        }
    }

    private void registerCommands(){
        getCommand("aic").setExecutor(new AdminCommand(this));
        getCommand("ic").setExecutor(new UserCommand(this));
    }

    private void registerEvents(){
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new MinorEvents(this), this);
        pm.registerEvents(new InventoryEvent(this), this);
        pm.registerEvents(new ChatEvent(this), this);
    }

    private void setupConfigurations(){
        this.guiConfiguration = new YMLConfiguration("gui", laxeLib);
        this.messagesConfiguration = new YMLConfiguration("messages", laxeLib);
    }

    public static IdentityManager getIdentityManager(){
        return identityManager;
    }

}

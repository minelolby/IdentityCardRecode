package me.github.com.utils;

import com.zaxxer.hikari.HikariDataSource;
import lombok.SneakyThrows;
import me.github.com.IdentityCardPlugin;

import java.sql.PreparedStatement;

public class MySQL {

    private final IdentityCardPlugin plugin;

    public final String host;
    public final int port;
    public final String database;
    public final String username;
    public final String password;

    private HikariDataSource hikari;

    public MySQL(IdentityCardPlugin plugin){
        this.plugin = plugin;
        this.host = plugin.getConfig().getString("Database.hostname");
        this.port = plugin.getConfig().getInt("Database.port");
        this.database =  plugin.getConfig().getString("Database.database");
        this.username = plugin.getConfig().getString("Database.user");
        this.password = plugin.getConfig().getString("Database.password");
    }

    @SneakyThrows
    public void connect(){
        hikari = new HikariDataSource();
        hikari.setDataSourceClassName("com.mysql.cj.jdbc.MysqlDataSource");
        hikari.addDataSourceProperty("serverName", host);
        hikari.addDataSourceProperty("port", port);
        hikari.addDataSourceProperty("databaseName", database);
        hikari.addDataSourceProperty("user", username);
        hikari.addDataSourceProperty("password", password);

        createTables();
    }

    @SneakyThrows
    public void disconnect(){
        if(isConnected()){
            hikari.close();
        }
    }

    @SneakyThrows
    private void createTables(){
        PreparedStatement ps = plugin.getMySQL().getHikari().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS player_data (player_name VARCHAR(20),player_uuid VARCHAR(40),gender VARCHAR(20),age INT(10),name VARCHAR(20),surname VARCHAR(20))");
        ps.executeUpdate();
    }

    public boolean isConnected(){
        return hikari != null;
    }

    public HikariDataSource getHikari(){
        return hikari;
    }
}

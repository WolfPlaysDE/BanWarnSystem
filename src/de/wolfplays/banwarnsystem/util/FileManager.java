package de.wolfplays.banwarnsystem.util;

import java.io.File;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import de.wolfplays.banwarnsystem.BanWarnSystem;

/**
 * Created by WolfPlaysDE
 * On 30.03.2015 at 02:14:26
 */
public class FileManager {
	
	// Config File
	public static File getConfigFile() {
		return new File(BanWarnSystem.getInstance().getDataFolder(), "config.yml");
	}
	private static FileConfiguration getConfigFileConfiguration() {
		return YamlConfiguration.loadConfiguration(getConfigFile());
	}
	
	// MySQL File
	public static File getMySQLFile() {
		return new File(BanWarnSystem.getInstance().getDataFolder(), "mysql.yml");
	}
	private static FileConfiguration getMySQLFileConfiguration() {
		return YamlConfiguration.loadConfiguration(getMySQLFile());
	}
	
	// Config File setup
	private static void setDefaultConfig() {
		FileConfiguration cfg = getConfigFileConfiguration();
		cfg.options().copyDefaults(true);
		cfg.addDefault("prefix", "&8[&6BanWarnSystem&8]");
		cfg.addDefault("maxwarns", "8");
		try {
			cfg.save(getConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static void readConfig() {
		FileConfiguration cfg = getConfigFileConfiguration();
		BanWarnSystem.getInstance().prefix = ChatColor.translateAlternateColorCodes('&', cfg.getString("prefix") + "§r ");
		MySQLWarnManager.maxwarns = Integer.parseInt(cfg.getString("maxwarns"));
	}
	
	//MySQL File setup
	private static void setDefaultMySQL() {
		FileConfiguration cfg = getMySQLFileConfiguration();
		cfg.options().copyDefaults(true);
		cfg.addDefault("username", "root");
		cfg.addDefault("passwort", "");
		cfg.addDefault("host", "localhost");
		cfg.addDefault("port", "3306");
		cfg.addDefault("database",  "minecraft_bansystem");
		try {
			cfg.save(getMySQLFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static void readMySQL() {
		FileConfiguration cfg = getMySQLFileConfiguration();
		MySQL.username = cfg.getString("username");
		MySQL.passwort = cfg.getString("passwort");
		MySQL.host = cfg.getString("host");
		MySQL.port = cfg.getString("port");
		MySQL.database = cfg.getString("database");
	}
	
	// Setup all Configs
	public static void setupConfigs() {
		FileManager.setDefaultConfig();
		FileManager.readConfig();
		FileManager.setDefaultMySQL();
		FileManager.readMySQL();
	}
	
}

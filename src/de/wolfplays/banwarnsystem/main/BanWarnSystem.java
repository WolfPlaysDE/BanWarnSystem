package de.wolfplays.banwarnsystem.main;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import de.wolfplays.banwarnsystem.cmd.Cmd_ban;
import de.wolfplays.banwarnsystem.cmd.Cmd_banhelp;
import de.wolfplays.banwarnsystem.cmd.Cmd_warn;
import de.wolfplays.banwarnsystem.cmd.Cmd_warnhelp;
import de.wolfplays.banwarnsystem.listener.Listener_PlayerJoinEvent;
import de.wolfplays.banwarnsystem.listener.Listener_PlayerLoginEvent;
import de.wolfplays.banwarnsystem.util.FileManager;
import de.wolfplays.banwarnsystem.util.MySQL;
import de.wolfplays.banwarnsystem.util.PluginLogger;
import de.wolfplays.banwarnsystem.util.RegisterManager;

/**
 * Created by WolfPlaysDE
 * On 30.03.2015 at 02:13:28
 */
public class BanWarnSystem extends JavaPlugin {

	public String prefix;
	
	public static BanWarnSystem Instance;
	
	public RegisterManager<BanWarnSystem> register = new RegisterManager<BanWarnSystem>(this);
	
	@Override
	public void onLoad() {
		BanWarnSystem.Instance = this;
	}
	
	@Override
	public void onEnable() {
		FileManager.setupConfigs();
        PluginLogger.loadLog();
		
        MySQL.connect();

        register.registerEvents(Listener_PlayerLoginEvent.class, Listener_PlayerJoinEvent.class);
        
        register.registerCommand("permban", "", new Cmd_ban());
        register.registerCommand("tempban", "", new Cmd_ban());
        register.registerCommand("unban", "", new Cmd_ban());
        register.registerCommand("checkban", "", new Cmd_ban());
        register.registerCommand("banhelp", "", new Cmd_banhelp());
        
        register.registerCommand("addwarn", "", new Cmd_warn());
        register.registerCommand("removewarn", "", new Cmd_warn());
        register.registerCommand("checkwarn", "", new Cmd_warn());
        register.registerCommand("warnhelp", "", new Cmd_warnhelp());

        this.getServer().getConsoleSender().sendMessage(prefix + "Plugin coded by WolfPlaysDE!");
        this.getServer().getConsoleSender().sendMessage(prefix + "Das Plugin ist Copyright geschuetzt weitergeben oder verkaufen ist verboten!");
        this.getServer().getConsoleSender().sendMessage(prefix + "Config Datei: " + (FileManager.getConfigFile().exists() ? "§2erfolgreich geladen!" : "§4nicht gefunden!"));
        this.getServer().getConsoleSender().sendMessage(prefix + "MySQL Datei: " + (FileManager.getMySQLFile().exists() ? "§2erfolgreich geladen!" : "§4nicht gefunden!"));
        this.getServer().getConsoleSender().sendMessage(prefix + "Log Datei: " + (PluginLogger.logfile.exists() ? "§2erfolgreich geladen!" : "§4nicht gefunden!"));

        this.getServer().getConsoleSender().sendMessage(prefix + "MySQL Verbindungsaufgebau: " + (MySQL.con == null ? "§4ist fehlgeschlagen!" : "§2war erfolgreich!"));
        if (MySQL.con == null) {
        	Plugin plugin = getServer().getPluginManager().getPlugin("MySQLBanSystem");
            getServer().getPluginManager().disablePlugin(plugin);
            this.getServer().getConsoleSender().sendMessage(prefix + " §4§lDAS PLUGIN WURDE DEAKTIVIRT, WEIL DIE MYSQL VERBINDUNG FEHLGESCHLAGEN IST!");
        }
    }
		
	@Override
	public void onDisable() {
		MySQL.diconnect();
	}

	public static BanWarnSystem getInstance() {
		return Instance;
	}

}

package de.wolfplays.banwarnsystem;

import org.bukkit.plugin.java.JavaPlugin;

import de.wolfplays.banwarnsystem.commands.CommandBan;
import de.wolfplays.banwarnsystem.commands.CommandBanHelp;
import de.wolfplays.banwarnsystem.commands.CommandWarn;
import de.wolfplays.banwarnsystem.commands.CommandWarnHelp;
import de.wolfplays.banwarnsystem.listener.ListenerPlayerJoinEvent;
import de.wolfplays.banwarnsystem.listener.ListenerPlayerLoginEvent;
import de.wolfplays.banwarnsystem.mysql.MySQL;
import de.wolfplays.banwarnsystem.util.FileManager;
import de.wolfplays.banwarnsystem.util.PluginLogger;
import de.wolfplays.banwarnsystem.util.PluginLogger.LogSettings;
import de.wolfplays.banwarnsystem.util.RegisterManager;

/**
 * Created by WolfPlaysDE On 30.03.2015 at 02:13:28
 */
public class BanWarnSystem extends JavaPlugin {

  public String prefix;

  private static BanWarnSystem instance;

  public RegisterManager<BanWarnSystem> register = new RegisterManager<BanWarnSystem>(this);

  public PluginLogger logger;

  @Override
  public void onLoad() {
    instance = this;
    logger = new PluginLogger(getDataFolder(), "Log.txt");
  }

  @Override
  public void onEnable() {
    FileManager.setupConfigs();

    MySQL.connect();

    register.registerEvents(ListenerPlayerLoginEvent.class, ListenerPlayerJoinEvent.class);

    register.registerCommand("permban", "", new CommandBan());
    register.registerCommand("tempban", "", new CommandBan());
    register.registerCommand("unban", "", new CommandBan());
    register.registerCommand("checkban", "", new CommandBan());
    register.registerCommand("banhelp", "", new CommandBanHelp());

    register.registerCommand("addwarn", "", new CommandWarn());
    register.registerCommand("removewarn", "", new CommandWarn());
    register.registerCommand("checkwarn", "", new CommandWarn());
    register.registerCommand("warnhelp", "", new CommandWarnHelp());

    this.getServer().getConsoleSender().sendMessage(prefix + "Plugin coded by WolfPlaysDE!");
    this.getServer()
        .getConsoleSender()
        .sendMessage(
            prefix + "Das Plugin ist Copyright geschuetzt weitergeben oder verkaufen ist verboten!");
    this.getServer()
        .getConsoleSender()
        .sendMessage(
            prefix
                + "Config Datei: "
                + (FileManager.getConfigFile().exists() ? "§2erfolgreich geladen!"
                    : "§4nicht gefunden!"));
    this.getServer()
        .getConsoleSender()
        .sendMessage(
            prefix
                + "MySQL Datei: "
                + (FileManager.getMySQLFile().exists() ? "§2erfolgreich geladen!"
                    : "§4nicht gefunden!"));
    this.getServer()
        .getConsoleSender()
        .sendMessage(
            prefix + "Log Datei: "
                + (PluginLogger.logfile.exists() ? "§2erfolgreich geladen!" : "§4nicht gefunden!"));

    this.getServer()
        .getConsoleSender()
        .sendMessage(
            prefix + "MySQL Verbindungsaufgebau: "
                + (MySQL.con == null ? "§4ist fehlgeschlagen!" : "§2war erfolgreich!"));
    if (MySQL.con == null) {
      this.getServer()
          .getConsoleSender()
          .sendMessage(
              prefix
                  + " §4§lDAS PLUGIN WURDE DEAKTIVIRT, WEIL DIE MYSQL VERBINDUNG FEHLGESCHLAGEN IST!");
      logger.log(LogSettings.WARN, "Die verbindung zu der MySQL ist fehlgeschlagen!");
      onDisable();
    }
    logger.log(LogSettings.INFO, "Das Plugin wurde Erfolgreich gestartet!");
  }

  @Override
  public void onDisable() {
    MySQL.diconnect();
    logger.log(LogSettings.INFO, "Das Plugin wurde Erfolgreich gestopt!");
  }

  public static BanWarnSystem getInstance() {
    return instance;
  }

}

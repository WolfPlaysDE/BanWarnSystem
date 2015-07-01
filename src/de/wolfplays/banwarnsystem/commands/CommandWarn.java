package de.wolfplays.banwarnsystem.commands;

import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.wolfplays.banwarnsystem.BanWarnSystem;
import de.wolfplays.banwarnsystem.mysql.MySQLBanManager;
import de.wolfplays.banwarnsystem.mysql.MySQLWarnManager;
import de.wolfplays.banwarnsystem.util.PluginLogger.LogSettings;
import de.wolfplays.banwarnsystem.util.UUIDFetcher;

/**
 * Created by WolfPlaysDE On 03.04.2015 at 03:11:51
 */
public class CommandWarn implements CommandExecutor {

  private UUID getUUID(String playername) {
    try {
      return UUIDFetcher.getUUIDOf(playername);
    } catch (Exception e) {
    }
    return null;
  }

  private String prefix = BanWarnSystem.getInstance().prefix;

  @Override
  public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {

    // Add a warn
    if (cmd.getName().equalsIgnoreCase("addwarn")) {
      if (!cs.hasPermission("system.addwarn"))
        return true;
      if (args.length >= 2) {
        String playername = args[0];
        if (getUUID(playername) == null) {
          cs.sendMessage(prefix + "§7Der Spieler §e" + playername + " §7existiert nicht!");
          return true;
        }
        if (MySQLWarnManager.getWarns(getUUID(playername).toString()) == MySQLWarnManager.maxwarns) {
          cs.sendMessage(prefix + "§7Der Spieler §e" + playername
              + " §7hat schon die maximale Anzahl an Warns!");
          return true;
        }
        String reason = "";
        for (int i = 1; i < args.length; i++) {
          reason += args[i] + " ";
        }
        if (!MySQLWarnManager.isInTabel(getUUID(playername).toString())) {
          OfflinePlayer op =
              BanWarnSystem.getInstance().getServer().getOfflinePlayer(getUUID(playername));
          MySQLWarnManager.addPlayer(op.getName(), op.getUniqueId().toString());
        }
        MySQLWarnManager.addWarn(getUUID(playername).toString(), 1);
        BanWarnSystem.getInstance().getServer()
            .broadcastMessage("§7---------[§6§lWarn-Info§r§7]---------");
        BanWarnSystem.getInstance().getServer()
            .broadcastMessage("§e" + playername + " §7hat einen neuen Warn bekommen!");
        BanWarnSystem
            .getInstance()
            .getServer()
            .broadcastMessage(
                "§7Warns: §e" + MySQLWarnManager.getWarns(getUUID(playername).toString()) + "/"
                    + MySQLWarnManager.maxwarns);
        BanWarnSystem.getInstance().getServer().broadcastMessage("§7Grund: §e" + reason);
        BanWarnSystem.getInstance().getServer()
            .broadcastMessage("§7---------[§6§lWarn-Info§r§7]---------");
        BanWarnSystem.getInstance().logger.log(LogSettings.INFO, cs.getName() + " hat "
            + playername + " einen Warn gegeben.");
        if (MySQLWarnManager.getWarns(getUUID(playername).toString()) == MySQLWarnManager.maxwarns) {
          MySQLBanManager.ban(args[0], getUUID(playername).toString(), "Zu Viele Warns!", -1);
          MySQLWarnManager.deletePlayer(getUUID(playername).toString());
          BanWarnSystem.getInstance().logger.log(LogSettings.INFO, playername
              + " wurde wegen maximalen Warns permanet gebannnt");
        }
        return true;
      }
      cs.sendMessage(prefix + "§c/addwarn <Spieler> <Grund>");
      return true;
    }

    // Remove a warn
    if (cmd.getName().equalsIgnoreCase("removewarn")) {
      if (!cs.hasPermission("system.removewarn"))
        return true;
      if (args.length == 1) {
        String playername = args[0];
        if (getUUID(playername) == null) {
          cs.sendMessage(prefix + "§7Der Spieler §e" + playername + " §7existiert nicht!");
          return true;
        }
        if (MySQLWarnManager.getWarns(getUUID(playername).toString()) == 0) {
          cs.sendMessage(prefix + "§7Der Spieler §e" + playername + " §7hat schon 0 Warns!");
          return true;
        }
        MySQLWarnManager.deleteWarns(getUUID(playername).toString(), 1);
        cs.sendMessage(prefix + "§7Du hast §e" + playername + " §71 Warn entfernt!");
        BanWarnSystem.getInstance().logger.log(LogSettings.INFO, cs.getName() + " hat "
            + playername + " einen Warn entfernt.");
        return true;
      }
      cs.sendMessage(prefix + "§c/removewarn <Spieler>");
      return true;
    }

    // Check Warns from a Player
    if (cmd.getName().equalsIgnoreCase("checkwarn")) {
      if (cs.hasPermission("system.checkwarns")) {
        if (args.length == 1) {
          String playername = args[0];
          if (getUUID(playername) == null) {
            cs.sendMessage(prefix + "§7Der Spieler §e" + playername + " §7existiert nicht!");
            return true;
          }
          cs.sendMessage(prefix + "§7---------[§6§lWarn-Infos§r§7]---------");
          cs.sendMessage(prefix + "§7Name: §e" + playername);
          cs.sendMessage(prefix + "§7Warns: §e"
              + MySQLWarnManager.getWarns(getUUID(playername).toString()));
          return true;
        }
        cs.sendMessage(prefix + "§c/checkwarn <Spieler>");
        return true;
      } else {
        if (args.length == 0) {
          if (cs instanceof Player) {
            Player p = (Player) cs;
            cs.sendMessage(prefix + "§7---------[§6§lWarn-Infos§r§7]---------");
            cs.sendMessage(prefix + "§7Name: §e" + p.getName());
            cs.sendMessage(prefix + "§7Warns: §e"
                + MySQLWarnManager.getWarns(p.getUniqueId().toString()));
            return true;
          }
          return true;
        }
        cs.sendMessage(prefix + "§c/checkwarn");
        return true;
      }
    }

    return true;
  }

}

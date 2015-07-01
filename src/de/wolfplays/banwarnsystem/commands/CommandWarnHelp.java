package de.wolfplays.banwarnsystem.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by WolfPlaysDE On 30.03.2015 at 04:49:34
 */
public class CommandWarnHelp implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
    if (cs.hasPermission("system.addwarn"))
      cs.sendMessage("§c/addwarn <Spieler> <Grund>");
    if (cs.hasPermission("system.removewarn"))
      cs.sendMessage("§c/removewarn <Spieler>");
    if (cs.hasPermission("system.checkwarns")) {
      cs.sendMessage("§c/checkwarn <Spieler>");
    } else {
      cs.sendMessage("§c/checkwarn");
    }
    return true;
  }

}

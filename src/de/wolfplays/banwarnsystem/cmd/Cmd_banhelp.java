package de.wolfplays.banwarnsystem.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by WolfPlaysDE
 * On 03.04.2015 at 03:11:51
 */
public class Cmd_banhelp implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if(cs.hasPermission("system.permban")) cs.sendMessage("§c/permban <Spieler> <Grund>");
		if(cs.hasPermission("system.tempban")) cs.sendMessage("§c/tempban <Spieler> <Zahlenwert> <Einheit(sec/min/hour/day/week)> <Grund>");
		if(cs.hasPermission("system.unban")) cs.sendMessage("§c/unban <Spieler>");
		if(cs.hasPermission("system.check")) cs.sendMessage("§c/check <list/Spieler>");
		return true;
	}

}

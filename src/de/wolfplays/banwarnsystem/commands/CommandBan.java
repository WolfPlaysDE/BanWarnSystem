package de.wolfplays.banwarnsystem.commands;

import java.util.List;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import de.wolfplays.banwarnsystem.BanWarnSystem;
import de.wolfplays.banwarnsystem.mysql.MySQLBanManager;
import de.wolfplays.banwarnsystem.util.BanUnit;
import de.wolfplays.banwarnsystem.util.PluginLogger.LogSettings;
import de.wolfplays.banwarnsystem.util.UUIDFetcher;

/**
 * Created by WolfPlaysDE
 * On 30.03.2015 at 04:49:34
 */
public class CommandBan implements CommandExecutor {

	private UUID getUUID(String playername) {
		try {
			return UUIDFetcher.getUUIDOf(playername);
		} catch (Exception e) {}
		return null;
	}
	
	private String prefix = BanWarnSystem.getInstance().prefix;
	
	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		
		// Player permban
		if(cmd.getName().equalsIgnoreCase("permban")) {
			if(!cs.hasPermission("system.permban")) return true;
			if(args.length >= 2) {
				String playername = args[0];
				if(getUUID(playername) == null) {
					cs.sendMessage(prefix + "§7Der Spieler §e" + playername + " §7existiert nicht!");						
					return true;
				}
				if(MySQLBanManager.isBanned(getUUID(playername).toString())) {
					cs.sendMessage(prefix + "§cDieser Spieler ist bereits vom Server gebannt!");						
					return true;
				}
				String reason = "";
				for(int i = 1; i < args.length; i++) {
					reason += args[i] + " ";
				}
				MySQLBanManager.ban(args[0], getUUID(playername).toString(), reason, -1);
				cs.sendMessage(prefix + "§7Du hast §e" + playername + " §4PERMANET §7von dem Server gebant!");
				BanWarnSystem.getInstance().logger.log(LogSettings.INFO, cs.getName() + " hat " + playername + " permanet von dem Server gebannt! Grund:\n" + reason);
				return true;
				}
			cs.sendMessage(prefix + "§c/permban <Spieler> <Grund>");
			return true;
		}
		
		// Player tempban
		if(cmd.getName().equalsIgnoreCase("tempban")) {
			if(!cs.hasPermission("system.tempban")) return true;
			if(args.length >= 4) {
				String playername = args[0];
				if(getUUID(playername) == null) {
					cs.sendMessage(prefix + "§7Der Spieler §e" + playername + " §7existiert nicht!");
					return true;
				}
				if(MySQLBanManager.isBanned(getUUID(playername).toString())) {
					cs.sendMessage(prefix + "§cDieser Spieler ist bereits vom Server gebannt!");
					return true;
				}
				int value;
				try {
					value = Integer.valueOf(args[1]);
				}catch(NumberFormatException e) {
					cs.sendMessage(prefix + "§c<Zahlenwert> muss eine Zahl sein!");
					return true;
				}
				if(value >= 500) {
					cs.sendMessage(prefix + "§cDie Zahl mus unter 500 liegen!");
					return true;
				}
				String unitString = args[2];
				String reason = "";
				for(int i = 3; i < args.length; i++) {
					reason += args[i] + " ";
				}
				List<String> unitsList = BanUnit.getUnitsAsString();
				if(unitsList.contains(unitString.toLowerCase())) {
					BanUnit unit = BanUnit.getUnit(unitString);
					long seconds = value * unit.getToSecond();
					MySQLBanManager.ban(playername, getUUID(playername).toString(), reason, seconds);
					cs.sendMessage("§7Du hast §e" + playername + " §7f§r §c" + value + unit.getName() + " §7vom Server gebannt!");
					BanWarnSystem.getInstance().logger.log(LogSettings.INFO, cs.getName() + " hat " + playername + " temporär von dem Server gebannt! Grund:\n" + reason);
					return true;
				}
				cs.sendMessage(prefix + "§cDiese <Einheit> existiert nicht!(sec/min/hour/day/week)");
				return true;
			}
			cs.sendMessage(prefix + "§c/tempban <Spieler> <Zahlenwert> <Einheit(sec/min/hour/day/week)> <Grund>");
			return true;
		}
		
		// Unban Player
		if(cmd.getName().equalsIgnoreCase("unban")) {
			if(!cs.hasPermission("system.unban")) return true;
			if(args.length == 1) {
				String playername = args[0];
				if(getUUID(playername) == null) {
					cs.sendMessage(prefix + "§7Der Spieler §e" + playername + " §7existiert nicht!");
					return true;
				}
				if(!(MySQLBanManager.isBanned(getUUID(playername).toString()))) {
					cs.sendMessage(prefix + "§cDieser Spieler ist nicht gebannt!");
					return true;
				}
				MySQLBanManager.unban(getUUID(playername).toString());
				cs.sendMessage(prefix + "§7Du hast §e" + playername + " §7entbannt!");
				BanWarnSystem.getInstance().logger.log(LogSettings.INFO, cs.getName() + " hat " + playername + " entbannt!");
				return true;
			}
			cs.sendMessage(prefix + "§c/unban <Spieler>");
			return true;
		}
		
		// Check Player banned
		if(cmd.getName().equalsIgnoreCase("checkban")) {
			if(!cs.hasPermission("system.check")) return true;
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("list")) {
					if(MySQLBanManager.getBannedPlayers().isEmpty()) {
						cs.sendMessage(prefix + "§cMomentan sind keine Spieler vom Server gebannt!");
						return true;
					}
					cs.sendMessage("§7---------[§6§lBan-Liste§r§7]---------");
					for(String allBanned : MySQLBanManager.getBannedPlayers()) {
						cs.sendMessage(prefix + "§e" + allBanned + " §7(Grund: §r" + MySQLBanManager.getReason(getUUID(allBanned).toString()) + "§7)");
					}
					return true;
				}
				String playername = args[0];
				if(getUUID(playername) == null) {
					cs.sendMessage(prefix + "§7Der Spieler §e" + playername + " §7existiert nicht!");
					return true;
				}
				cs.sendMessage(prefix + "§7---------[§6§lBan-Infos§r§7]---------");
				cs.sendMessage(prefix + "§eName: §r" + playername);
				cs.sendMessage(prefix + "§eGebannt: §r" + (MySQLBanManager.isBanned(getUUID(playername).toString()) ? "§aJa!" : "§aNein!"));
				if(!MySQLBanManager.isBanned(getUUID(playername).toString())) return true;
				cs.sendMessage(prefix + "§aGrund: §r" + MySQLBanManager.getReason(getUUID(playername).toString()));
				cs.sendMessage(prefix + "§aVerbleiben Zeit: §r" + MySQLBanManager.getRemaningTime(getUUID(playername).toString()));
				return true;
			}
			cs.sendMessage(prefix + "§c/check <list/Spieler>");
			return true;
		}
		
		return false;
	}

}

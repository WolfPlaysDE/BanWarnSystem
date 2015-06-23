package de.wolfplays.banwarnsystem.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.bukkit.Bukkit;

import de.wolfplays.banwarnsystem.BanWarnSystem;

/**
 * Created by WolfPlaysDE
 * On 30.03.2015 at 03:16:00
 */
public class MySQLBanManager {
	
	private static ExecutorService executor;
	
	static {
		executor = Executors.newCachedThreadPool();
	}
	
	// Get the Remaningtime
	public static String getRemaningTime(String uuid) {
		long current = System.currentTimeMillis();
		long end = getEnd(uuid);
		if(end == -1) {
			return "§4PERMANET";
		}
		long millis = end - current;
		
		long seconds = 0;
		long minutes = 0;
		long hours = 0;
		long days = 0;
		long weeks = 0;
		while(millis > 1000) {
			millis-=1000;
			seconds++;
		}
		while(seconds > 60) {
			seconds-=60;
			minutes++;
		}
		while(minutes > 60) {
			minutes-=60;
			hours++;
		}
		while(hours > 24) {
			hours-=24;
			days++;
		}
		while(days > 7) {
			days-=7;
			weeks++;
		}
		return "§e" + weeks + " Woche(n) " + days + " Tag(e) " + hours + " Stunde(n) " + minutes + " Minute(n) " + seconds + "Sekunde(n)";
	}
	
	// Ban a Player
	public static void ban(String playername, String uuid, String reason, long seconds) {
		long end = 0;
		if(seconds == -1) {
			end = -1;
		}else {
			long current = System.currentTimeMillis();
			long millis = seconds * 1000;
			end = current + millis;	
		}
		
		MySQLBan(playername, uuid, end, reason);
		
		BanWarnSystem.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(BanWarnSystem.getInstance(), new Runnable() {
			@Override
			public void run() {
				if(Bukkit.getPlayer(playername) != null) {
					Bukkit.getPlayer(playername).kickPlayer(
							"§cDu wurdes von dem Server ausgeschlossen!" +
							"\n" +
							"\n" +
							"§3Grund: §e" + getReason(uuid) +
							"\n" +
							"\n" +
							"§3Verbleibende Zeit: §e" + getRemaningTime(uuid));
				}
			}
		}, 10);
	}
	
	// MySQL Ban
	private static void MySQLBan(String playername, String uuid, long end, String reason) {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				String qry = "INSERT INTO " + MySQL.TABEL_BANNEDPLAYERS + " (playername, uuid, end, reason) VALUES (?, ?, ?, ?)";
				try {
					PreparedStatement stmt = MySQL.con.prepareStatement(qry);
					stmt.setString(1, playername);
					stmt.setString(2, uuid);
					stmt.setLong(3, end);
					stmt.setString(4, reason);
					stmt.executeUpdate();
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	// Unban a Player
	public static void unban(String uuid) {
		executor.execute(new Runnable() {			
			@Override
			public void run() {
				String qry = "DELETE FROM " + MySQL.TABEL_BANNEDPLAYERS + " WHERE uuid = ?";
				try {
					PreparedStatement stmt = MySQL.con.prepareStatement(qry);
					stmt.setString(1, uuid);
					stmt.executeUpdate();
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	// Check Player is banned
	public static boolean isBanned(String uuid) {
		boolean returnStatement = false;
		try {
			String qry = "SELECT * FROM " + MySQL.TABEL_BANNEDPLAYERS + " WHERE uuid = ?";
			PreparedStatement stmt = MySQL.con.prepareStatement(qry);
			stmt.setString(1, uuid);
			ResultSet rs = stmt.executeQuery();
			if(rs.first()) returnStatement = true;
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return returnStatement;
	}
	
	// Check the reason for the ban
	public static String getReason(String uuid) {
		try {
			String qry = "SELECT * FROM " + MySQL.TABEL_BANNEDPLAYERS + " WHERE uuid = ?";
			PreparedStatement stmt = MySQL.con.prepareStatement(qry);
			stmt.setString(1, uuid);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				return rs.getString("reason");
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// Check the end of the ban
	public static Long getEnd(String uuid) {
		try {
			String qry = "SELECT * FROM " + MySQL.TABEL_BANNEDPLAYERS + " WHERE uuid = ?";
			PreparedStatement stmt = MySQL.con.prepareStatement(qry);
			stmt.setString(1, uuid);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				return rs.getLong("end");
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// Check all banned Player
	public static List<String> getBannedPlayers() {
		List<String> list = new ArrayList<String>();
		try {
			String qry = "SELECT * FROM " + MySQL.TABEL_BANNEDPLAYERS;
			PreparedStatement stmt = MySQL.con.prepareStatement(qry);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				list.add(rs.getString("playername"));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
}

package de.wolfplays.banwarnsystem.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import de.wolfplays.banwarnsystem.util.MySQLBanManager;

/**
 * Created by WolfPlaysDE
 * On 30.03.2015 at 06:32:58
 */
public class Listener_PlayerJoinEvent implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		if(MySQLBanManager.isBanned(p.getUniqueId().toString())) {
			MySQLBanManager.unban(p.getUniqueId().toString());
		}
	}
	
}

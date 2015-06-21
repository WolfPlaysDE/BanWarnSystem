package de.wolfplays.banwarnsystem.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import de.wolfplays.banwarnsystem.util.MySQLBanManager;

/**
 * Created by WolfPlaysDE
 * On 30.03.2015 at 06:33:26
 */
public class ListenerPlayerLoginEvent implements Listener {

	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event) {
		Player p = event.getPlayer();
		if(MySQLBanManager.isBanned(p.getUniqueId().toString())) {
			long current = System.currentTimeMillis();
			long end = MySQLBanManager.getEnd(p.getUniqueId().toString());
			if(current < end | end == -1) {
				event.disallow(Result.KICK_BANNED, 
						"§cDu wurdes von dem Server gebannt!" +
						"\n" +
						"\n" +
						"§3Grund: §e" + MySQLBanManager.getReason(p.getUniqueId().toString()) +
						"\n" +
						"\n" +
						"§3Verbleibende Zeit: §e" + MySQLBanManager.getRemaningTime(p.getUniqueId().toString()));
			}
		}
 	}
	
}

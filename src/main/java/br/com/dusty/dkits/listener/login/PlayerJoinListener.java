package br.com.dusty.dkits.listener.login;

import br.com.dusty.dkits.Main;
import br.com.dusty.dkits.gamer.EnumRank;
import br.com.dusty.dkits.gamer.Gamer;
import br.com.dusty.dkits.util.bossbar.BossBarUtils;
import br.com.dusty.dkits.util.scoreboard.ScoreboardUtils;
import br.com.dusty.dkits.util.text.Text;
import br.com.dusty.dkits.util.text.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitScheduler;

public class PlayerJoinListener implements Listener {
	
	private static final String JOIN_MESSAGE_PREFIX = Text.of("[")
	                                                      .color(TextColor.GRAY)
	                                                      .append("+")
	                                                      .color(TextColor.GREEN)
	                                                      .append("] ")
	                                                      .color(TextColor.GRAY)
	                                                      .toString();
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		
		Gamer gamer = Gamer.of(player);
		
		ScoreboardUtils.create(player);
		ScoreboardUtils.updateAll();
		
		BossBarUtils.MAIN.send(player);
		
		if(gamer.getRank().isBelow(EnumRank.MOD))
			e.setJoinMessage(JOIN_MESSAGE_PREFIX + player.getName());
		else
			e.setJoinMessage(null);
	}
}

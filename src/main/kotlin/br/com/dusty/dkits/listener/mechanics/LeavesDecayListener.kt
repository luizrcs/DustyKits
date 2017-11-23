package br.com.dusty.dkits.listener.mechanics

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.LeavesDecayEvent

object LeavesDecayListener: Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	fun onLeavesDecay(event: LeavesDecayEvent) {
		event.isCancelled = true
	}
}

package br.com.dusty.dkits.listener.mechanics

import br.com.dusty.dkits.gamer.Gamer
import br.com.dusty.dkits.gamer.gamer
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.util.Vector

object PlayerMoveListener: Listener {

	@EventHandler
	fun onPlayerMove(event: PlayerMoveEvent) {
		val player = event.player

		val gamer = player.gamer()

		val from = event.from
		val to = event.to

		if (gamer.isFrozen && isWalk(from, to)) event.isCancelled = true
		else when (to.clone().add(0.0, -0.5, 0.0).block.type) {
			Material.SPONGE -> boostUp(gamer, to)
		}
	}

	private fun isWalk(from: Location, to: Location): Boolean = from.x != to.x || from.y != to.y || from.z != to.z

	private fun boostUp(gamer: Gamer, location: Location) {
		val player = gamer.player

		player.velocity = Vector(0.0, 1.5, 0.0)

		gamer.isNoFall = true
	}
}

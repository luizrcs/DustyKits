package br.com.dusty.dkits.ability

import br.com.dusty.dkits.kit.Kits
import br.com.dusty.dkits.util.gamer.gamer
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerInteractEvent

object ThorAbility: Ability() {

	@EventHandler
	fun onEntityDamage(event: EntityDamageEvent) {
		if (event.cause == EntityDamageEvent.DamageCause.LIGHTNING && event.entity is Player) {
			val player = event.entity as Player
			val gamer = player.gamer()

			if (hasAbility(gamer) && canUse(gamer)) event.isCancelled = true
		}
	}

	@EventHandler
	fun onPlayerInteract(event: PlayerInteractEvent) {
		if (event.action == Action.LEFT_CLICK_BLOCK || event.action == Action.RIGHT_CLICK_BLOCK) {
			val item = event.item

			if (item != null && item.type == Material.WOOD_AXE && item == Kits.THOR.items[0]) {
				val player = event.player
				val gamer = player.gamer()

				if (gamer.isOnKitCooldown()) {
					sendKitCooldownMessage(gamer)
				} else if (hasAbility(gamer) && canUse(gamer)) {
					val location = event.clickedBlock.location
					location.world.strikeLightning(location)

					gamer.kitCooldown = 10000L
				}
			}
		}
	}
}
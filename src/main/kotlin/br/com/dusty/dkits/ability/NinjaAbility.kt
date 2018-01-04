package br.com.dusty.dkits.ability

import br.com.dusty.dkits.gamer.Gamer
import br.com.dusty.dkits.util.entity.gamer
import br.com.dusty.dkits.util.stdlib.clearFormatting
import br.com.dusty.dkits.util.text.Text
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerToggleSneakEvent

object NinjaAbility: Ability() {

	val GAMER_BY_NINJA = hashMapOf<Gamer, Gamer>()

	@EventHandler
	fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
		if (event.entity is Player && event.damager is Player) {
			val gamer = (event.entity as Player).gamer()

			if (hasAbility(gamer)) {
				val damager = (event.damager as Player).gamer()

				if (damager.canUse(gamer)) GAMER_BY_NINJA.put(damager, gamer)
			}
		}
	}

	@EventHandler
	fun onPlayerToggleSneak(event: PlayerToggleSneakEvent) {
		if (event.isSneaking) {
			val player = event.player
			val gamer = player.gamer()

			if (hasAbility(gamer) && gamer.isCombatTagged()) {
				if (gamer.isOnKitCooldown()) sendKitCooldownMessage(gamer)
				else {
					val otherGamer = GAMER_BY_NINJA[gamer] ?: return

					if (gamer.combatPartner == otherGamer && gamer.canUse(otherGamer)) {
						val otherPlayer = otherGamer.player

						player.teleport(otherPlayer)

						otherPlayer.sendMessage(Text.negativePrefix().basic("O ninja ").negative(player.displayName.clearFormatting()).basic(" se teleportou até ").negative("você").basic("!").toString())

						gamer.kitCooldown = 10000L
					} else {
						GAMER_BY_NINJA.remove(gamer)
					}
				}
			}
		}
	}
}

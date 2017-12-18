package br.com.dusty.dkits.ability

import br.com.dusty.dkits.gamer.GamerRegistry
import br.com.dusty.dkits.kit.Kits
import br.com.dusty.dkits.util.Tasks
import br.com.dusty.dkits.util.clearFormatting
import br.com.dusty.dkits.util.gamer.gamer
import br.com.dusty.dkits.util.rename
import br.com.dusty.dkits.util.text.Text
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector

object BubbleAbility: Ability() {

	val MAGMA_CREAM = ItemStack(Material.MAGMA_CREAM).rename("Bolha Usada")

	/*@EventHandler
	fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
		if (event.entity is Player && event.damager is Player) {
			val player = event.entity as Player

			if (player.exhaustion == 10F) player.sendMessage(Text.positivePrefix().positive("POP!").toString())
		}
	}*/

	@EventHandler
	fun onPlayerInteract(event: PlayerInteractEvent) {
		if (event.action == Action.RIGHT_CLICK_BLOCK || event.action == Action.RIGHT_CLICK_AIR) {
			val item = event.item

			if (item != null && (item.type == Material.SLIME_BALL || item.type == Material.MAGMA_CREAM)) {
				val player = event.player
				val gamer = player.gamer()

				if (hasAbility(gamer) && canUse(gamer)) {
					if (gamer.isOnKitCooldown()) {
						sendKitCooldownMessage(gamer)
					} else if (item == Kits.BUBBLE.items[0]) {
						val players = GamerRegistry.onlineGamers().filter { canUse(gamer, it) && player.location.distance(it.player.location) < 5.0 }.map { it.player }

						Tasks.sync(object: BukkitRunnable() {
							var i = 100
							var velocityY = 0.0075

							override fun run() {
								when (i--) {
									99   -> {
										val message = Text.neutralPrefix().basic("Você foi ").negative("preso").basic(" em uma bolha do jogador ").negative(player.displayName.clearFormatting()).basic(
												"!").toString()

										players.forEach { it.sendMessage(message) }
									}
									0    -> {
										cancel()

										players.forEach { it.world.playSound(it.location, Sound.ENTITY_ITEM_PICKUP, 1F, 1F) }
									}
									else -> {
										players.forEach {
											it.velocity = Vector(0.0, velocityY, 0.0)
											it.spawnParticle(Particle.SLIME, it.location.add(0.0, 1.0, 0.0), 1)
										}
									}
								}

								velocityY += 0.0025
							}
						}, 0L, 1L)

						player.inventory.itemInMainHand = MAGMA_CREAM

						Tasks.sync(Runnable {
							val index = player.inventory.indexOfFirst { it != null && it.type == Material.MAGMA_CREAM && it == MAGMA_CREAM }

							if (index != -1 && gamer.kit == Kits.BUBBLE) player.inventory.setItem(index, Kits.BUBBLE.items[0])
						}, 600L)

						gamer.kitCooldown = 30000L
					}
				}
			}
		}
	}
}
package br.com.dusty.dkits.ability

import br.com.dusty.dkits.gamer.GamerRegistry
import br.com.dusty.dkits.kit.Kits
import br.com.dusty.dkits.util.Tasks
import br.com.dusty.dkits.util.entity.gamer
import br.com.dusty.dkits.util.rename
import br.com.dusty.dkits.util.stdlib.clearFormatting
import br.com.dusty.dkits.util.text.Text
import org.bukkit.Effect
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector

object BubbleAbility: Ability() {

	val MAGMA_CREAM = ItemStack(Material.MAGMA_CREAM).rename("Bolha Usada")

	val FLUCTUATING = arrayListOf<Player>()

	@EventHandler
	fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
		if (event.entity is Player && event.damager is Player) {
			val player = event.entity as Player

			if (player in FLUCTUATING) {
				player.sendMessage(Text.positivePrefix().positive("POP!").toString())

				FLUCTUATING.remove(player)
			}
		}
	}

	@EventHandler
	fun onPlayerDeath(event: PlayerDeathEvent) {
		val player = event.entity as Player

		if (player in FLUCTUATING) FLUCTUATING.remove(player)
	}

	@EventHandler
	fun onPlayerQuit(event: PlayerQuitEvent) {
		val player = event.player as Player

		if (player in FLUCTUATING) FLUCTUATING.remove(player)
	}

	@EventHandler
	fun onPlayerInteract(event: PlayerInteractEvent) {
		if (event.action == Action.RIGHT_CLICK_BLOCK || event.action == Action.RIGHT_CLICK_AIR) {
			val player = event.player

			val item = player.itemInHand

			if (item != null && (item.type == Material.SLIME_BALL || item.type == Material.MAGMA_CREAM)) {
				val gamer = player.gamer()

				if (hasAbility(gamer) && gamer.canUse()) {
					if (gamer.isOnKitCooldown()) {
						sendKitCooldownMessage(gamer)
					} else if (item == Kits.BUBBLE.items[0]) {
						val players = GamerRegistry.onlineGamers().filter { gamer.canUse(it) && player.world == it.player.world && player.location.distance(it.player.location) < 5.0 }.map { it.player }.asSequence()

						FLUCTUATING.addAll(players)

						val message = Text.neutralPrefix().basic("Você foi ").negative("preso").basic(" em uma bolha do jogador ").negative(player.displayName.clearFormatting()).basic("!").toString()
						players.forEach { it.sendMessage(message) }

						Tasks.sync(object: BukkitRunnable() {
							var i = 100
							var velocityY = 0.0075

							override fun run() {
								when (i--) {
									0    -> {
										cancel()

										players.filter { it in FLUCTUATING }.forEach { it.world.playSound(it.location, Sound.ITEM_PICKUP, 1F, 1F) }

										FLUCTUATING.removeAll(players)
									}
									else -> {
										players.filter { it in FLUCTUATING }.forEach {
											it.velocity = Vector(0.0, velocityY, 0.0)
											it.playEffect(it.location.add(0.0, 1.0, 0.0), Effect.SLIME, 0)
										}
									}
								}

								velocityY += 0.0025
							}
						}, 0L, 1L)

						player.itemInHand = MAGMA_CREAM

						Tasks.sync(Runnable {
							val index = player.inventory.indexOfFirst { it != null && it.type == Material.MAGMA_CREAM && it == MAGMA_CREAM }

							if (index != -1 && gamer.kit == Kits.BUBBLE) player.inventory.setItem(index, Kits.BUBBLE.items[0])
						}, 600L)

						gamer.kitCooldown = 20000L
					}
				}
			}
		}
	}
}

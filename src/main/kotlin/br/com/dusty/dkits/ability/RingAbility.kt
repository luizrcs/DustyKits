package br.com.dusty.dkits.ability

import br.com.dusty.dkits.kit.Kits
import br.com.dusty.dkits.util.Tasks
import br.com.dusty.dkits.util.entity.gamer
import br.com.dusty.dkits.util.rename
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object RingAbility: Ability() {

	val LEATHER_BOOTS = ItemStack(Material.LEATHER_BOOTS).rename("Botas Comuns")

	@EventHandler(priority = EventPriority.HIGH)
	fun onInventoryClick(event: InventoryClickEvent) {
		val currentItem = event.currentItem ?: return

		if ((currentItem.type == Material.LEATHER_BOOTS || currentItem.type == Material.GOLD_BOOTS) && !event.isCancelled && (event.isShiftClick || event.slotType == InventoryType.SlotType.ARMOR)) {
			val player = event.whoClicked as Player
			val gamer = player.gamer()

			if (hasAbility(gamer)) event.isCancelled = true
		}
	}

	@EventHandler
	fun onPlayerInteract(event: PlayerInteractEvent) {
		if (event.action == Action.RIGHT_CLICK_BLOCK || event.action == Action.RIGHT_CLICK_AIR) {
			val player = event.player

			val item = player.itemInHand

			if (item != null && (item.type == Material.LEATHER_BOOTS || item.type == Material.GOLD_BOOTS)) {
				val gamer = player.gamer()

				if (hasAbility(gamer) && gamer.canUse()) {
					if (gamer.isOnKitCooldown()) {
						sendKitCooldownMessage(gamer)
					} else {
						player.addPotionEffect(PotionEffect(PotionEffectType.INVISIBILITY, 200, 0), true)
						player.addPotionEffect(PotionEffect(PotionEffectType.INCREASE_DAMAGE, 200, 0), true)

						val inventory = player.inventory
						inventory.itemInHand = LEATHER_BOOTS

						val chestplate = inventory.chestplate

						inventory.chestplate = null

						Tasks.sync(Runnable {
							if (gamer.kit == Kits.RING) {
								val index = player.inventory.indexOfFirst { it != null && it.type == Material.LEATHER_BOOTS }

								if (index != -1) inventory.setItem(index, Kits.RING.items[0])

								inventory.chestplate = chestplate
							}
						}, 600L)

						gamer.kitCooldown = 30000L
					}
				}
			}
		}
	}
}

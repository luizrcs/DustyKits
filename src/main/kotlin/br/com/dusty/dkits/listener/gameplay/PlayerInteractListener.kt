package br.com.dusty.dkits.listener.gameplay

import br.com.dusty.dkits.Config
import br.com.dusty.dkits.command.staff.BuyCommand
import br.com.dusty.dkits.inventory.KitMenu
import br.com.dusty.dkits.inventory.ShopMenu
import br.com.dusty.dkits.inventory.WarpMenu
import br.com.dusty.dkits.util.Inventories
import br.com.dusty.dkits.util.Tasks
import br.com.dusty.dkits.util.block.interact
import br.com.dusty.dkits.util.block.isSpecial
import br.com.dusty.dkits.util.entity.gamer
import br.com.dusty.dkits.warp.Warp
import org.bukkit.GameMode
import org.bukkit.Material.*
import org.bukkit.block.Sign
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action.*
import org.bukkit.event.player.PlayerInteractEvent

object PlayerInteractListener: Listener {

	val ALLOWED_ITEMS = arrayOf(WOOD_SWORD, GOLD_SWORD, STONE_SWORD, IRON_SWORD, DIAMOND_SWORD, BOW, FISHING_ROD, POTION, WRITTEN_BOOK)
	val ALLOWED_BLOCKS = arrayOf(ACACIA_DOOR, BIRCH_DOOR, DARK_OAK_DOOR, JUNGLE_DOOR, SPRUCE_DOOR, WOOD_DOOR, WOOD_BUTTON, STONE_BUTTON, TRAP_DOOR, ENCHANTMENT_TABLE)

	@EventHandler
	fun onPlayerInteract(event: PlayerInteractEvent) {
		if (event.action == PHYSICAL) return

		val player = event.player
		val gamer = player.gamer()

		if (gamer.warp.overrides(event)) return

		if (player.gameMode != GameMode.CREATIVE) event.isCancelled = true

		if (event.action == RIGHT_CLICK_BLOCK) {
			val block = event.clickedBlock

			if (block.type in ALLOWED_BLOCKS) event.isCancelled = false

			when (block.type) {
				WALL_SIGN, SIGN_POST -> {
					val sign = block.state as Sign

					if (sign.isSpecial()) sign.interact(player)
				}
			}
		}

		val item = player.itemInHand

		item?.run {
			if (event.action == RIGHT_CLICK_AIR && type in ALLOWED_ITEMS) event.isCancelled = false

			when (type) {
				CHEST         -> if (this == Warp.GAME_WARP_KIT.items[0]) player.openInventory(KitMenu.menuKitOwned(player))
				EMERALD       -> if (this == Warp.GAME_WARP_KIT.items[4]) player.openInventory(ShopMenu.menuShopMain(player))
				EMPTY_MAP     -> if (this == Warp.GAME_WARP_KIT.items[8]) player.openInventory(WarpMenu.menuWarpMain(player))
				GOLD_INGOT    -> if (this == Inventories.STORE) player.spigot().sendMessage(*BuyCommand.STORE_LINK)
				MUSHROOM_SOUP -> {
					if (event.action == RIGHT_CLICK_AIR || event.action == RIGHT_CLICK_BLOCK) {
						val maxHealth = player.maxHealth

						if (player.health < maxHealth) {
							if (player.health < maxHealth - 7) player.health += 7.0 else player.health = maxHealth

							val slot = player.inventory.heldItemSlot

							Tasks.sync(Runnable { player.inventory.setItem(slot, Inventories.BOWL) })

							Config.data.soups++
						}
					}
				}
			}
		}
	}
}

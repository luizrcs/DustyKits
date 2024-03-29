package br.com.dusty.dkits.listener.mechanics

import br.com.dusty.dkits.gamer.EnumMode
import br.com.dusty.dkits.inventory.KitMenu
import br.com.dusty.dkits.inventory.ShopMenu
import br.com.dusty.dkits.inventory.WarpMenu
import br.com.dusty.dkits.kit.Kit
import br.com.dusty.dkits.kit.Kits
import br.com.dusty.dkits.store.Store
import br.com.dusty.dkits.util.Inventories
import br.com.dusty.dkits.util.Inventories.BUTTON_BACK
import br.com.dusty.dkits.util.entity.gamer
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.web.WebAPI
import br.com.dusty.dkits.warp.Warps
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import org.bukkit.Material.*
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.inventory.InventoryType.ENCHANTING
import org.bukkit.event.inventory.InventoryType.SlotType

object InventoryClickListener: Listener {

	val BUY_KIT_FAIL = Text.negativePrefix().negative("Não").basic(" foi possível ").negative("comprar").basic(" esse kit!").toString()

	val DELIBERATELY_ALLOWED_ITEMS = arrayOf(MUSHROOM_SOUP, RED_MUSHROOM, BROWN_MUSHROOM, BOWL)
	val PROHIBITED_INVENTORIES = arrayOf(InventoryType.CHEST,
	                                     InventoryType.ENDER_CHEST,
	                                     InventoryType.WORKBENCH,
	                                     InventoryType.ANVIL,
	                                     InventoryType.ENCHANTING,
	                                     InventoryType.DROPPER,
	                                     InventoryType.DISPENSER)

	@EventHandler
	fun onInventoryClick(event: InventoryClickEvent) {
		val currentItem = event.currentItem ?: return

		when (currentItem.type) {
			in DELIBERATELY_ALLOWED_ITEMS -> return
			INK_SACK                      -> if (event.inventory.type == ENCHANTING) event.isCancelled = true
		}

		val player = event.whoClicked as Player
		val gamer = player.gamer()

		if (player.name == "MrPingu_") player.sendMessage("clickedInventory.type: " + event.clickedInventory.type + (if (event.view.bottomInventory != null) "; event.view.bottomInventory.type: " + event.view.bottomInventory.type.name else "") + (if (event.view.topInventory != null) "; event.view.topInventory.type: " + event.view.topInventory.type.name else ""))

		val topInventory = event.view.topInventory

		if (((topInventory != null && topInventory.type in PROHIBITED_INVENTORIES && currentItem in gamer.kit.items) || gamer.kit.isDummy) && gamer.mode != EnumMode.ADMIN) event.isCancelled = true
	}

	@EventHandler
	fun onMenuInventoryClick(event: InventoryClickEvent) {
		if (event.slotType == SlotType.CONTAINER) {
			val itemStack = event.currentItem

			if (itemStack == Inventories.BACKGROUND) {
				event.isCancelled = true
			} else {
				val player = event.whoClicked as Player
				val gamer = player.gamer()

				val inventory = event.clickedInventory

				when (inventory.title) {
					KitMenu.TITLE_OWNED                       -> {
						event.isCancelled = true

						when (itemStack) {
							KitMenu.BUTTON_OTHER -> player.openInventory(KitMenu.menuKitOther(player))
							else                 -> {
								player.closeInventory()

								val kit = Kits[itemStack]

								if (gamer.warp.isAllowed(kit, gamer, true)) gamer.setKitAndApply(kit, true)
							}
						}
					}
					KitMenu.TITLE_OTHER                       -> {
						event.isCancelled = true

						when (itemStack) {
							KitMenu.BUTTON_OWNED -> player.openInventory(KitMenu.menuKitOwned(player))
							else                 -> {
								player.sendMessage(Text.negativePrefix().basic("Você ").negative("não").basic(" possui o kit ").negative(Kits[itemStack].name).basic("!").toString())
							}
						}
					}
					ShopMenu.TITLE_MAIN                       -> {
						event.isCancelled = true

						when (itemStack) {
							ShopMenu.BUTTON_KITS  -> player.openInventory(ShopMenu.menuShopKit(player))
							ShopMenu.BUTTON_ARMOR -> player.openInventory(ShopMenu.menuShopArmor(player))
						}
					}
					ShopMenu.TITLE_KITS                       -> {
						event.isCancelled = true

						when (itemStack) {
							BUTTON_BACK       -> player.openInventory(ShopMenu.menuShopMain(player))
							ShopMenu.ALL_KITS -> {
								player.sendMessage(Text.negativePrefix().basic("Essa flor ").negative("não").basic(" está a ").negative("venda").basic("!").toString())
							}
							else              -> {
								val kit = Kits[itemStack]

								if (gamer.money < kit.data.price) {
									player.sendMessage(Text.negativePrefix().basic("Você ").negative("não").basic(" possui créditos ").negative("suficientes").basic(" para comprar o kit ").negative(
											kit.name).basic("!").toString())
								} else {
									//TODO: Confirmation screen
									val onNext = Consumer<Kit> {
										WebAPI.addPurchase(Store.PseudoPurchase(0, player.uniqueId.toString(), 1, Store.ID_BY_KIT[kit] ?: -1, -1))

										gamer.removeMoney(kit.data.price.toDouble())

										gamer.kits.add(kit)

										player.sendMessage(Text.positivePrefix().basic("Você ").positive("comprou").basic(" o kit ").positive(kit.name).basic("!").toString())
									}

									val onError = Consumer<Throwable> {
										player.sendMessage(BUY_KIT_FAIL)
									}

									Observable.just(kit).subscribeOn(Schedulers.io()).subscribe(onNext, onError)
								}
							}
						}
					}
					ShopMenu.TITLE_ARMOR                      -> {
						event.isCancelled = true

						when (itemStack) {
							BUTTON_BACK -> player.openInventory(ShopMenu.menuShopMain(player))
							else        -> {
								val price = ShopMenu.ARMOR[itemStack] ?: 0

								when {
									itemStack in player.inventory.armorContents -> player.sendMessage(Text.negativePrefix().basic("Você ").negative("já").basic(" está usando essa peça de ").negative(
											"armadura").basic("!").toString())
									gamer.money < price                         -> player.sendMessage(Text.negativePrefix().basic("Você ").negative("não").basic(" possui créditos ").negative("suficientes").basic(
											" para comprar essa peça de ").negative("armadura").basic("!").toString())
									else                                        -> {
										//TODO: Confirmation screen
										gamer.removeMoney(price.toDouble())

										when (itemStack.type.name.split("_").last()) {
											"HELMET"     -> player.inventory.helmet = itemStack
											"CHESTPLATE" -> player.inventory.chestplate = itemStack
											"LEGGINGS"   -> player.inventory.leggings = itemStack
											"BOOTS"      -> player.inventory.boots = itemStack
										}

										player.sendMessage(Text.positivePrefix().basic("Você ").positive("comprou").basic(" uma peça de ").positive("armadura").basic("!").toString())
									}
								}
							}
						}
					}
					WarpMenu.TITLE_MAIN                       -> {
						event.isCancelled = true

						when (itemStack) {
							WarpMenu.BUTTON_GAME  -> player.openInventory(WarpMenu.menuWarpGame(player))
							WarpMenu.BUTTON_EVENT -> player.openInventory(WarpMenu.menuWarpEvent(player))
						}
					}
					WarpMenu.TITLE_GAME, WarpMenu.TITLE_EVENT -> {
						event.isCancelled = true

						when (itemStack) {
							BUTTON_BACK -> player.openInventory(WarpMenu.menuWarpMain(player))
							else        -> {
								player.closeInventory()

								gamer.sendToWarp(Warps[itemStack], false, true)
							}
						}
					}
				}
			}
		}
	}
}

package br.com.dusty.dkits.command.gameplay

import br.com.dusty.dkits.command.PlayerCustomCommand
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.inventory.ShopMenu
import br.com.dusty.dkits.kit.Kits
import br.com.dusty.dkits.util.entity.gamer
import br.com.dusty.dkits.util.text.Text
import org.bukkit.entity.Player

object ShopCommand: PlayerCustomCommand(EnumRank.DEFAULT, "shop") {

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		val gamer = sender.gamer()

		if (args.isEmpty() || sender.gamer().rank.isLowerThan(EnumRank.ADMIN)) {
			if (gamer.warp.hasShop) sender.openInventory(ShopMenu.menuShopMain(sender))
			else sender.sendMessage(Text.negativePrefix().basic("Esta warp ").negative("não").basic(" permite o uso do ").negative("shop").basic("!").toString())
		} else {
			if (args[0] == "set" && args.size > 2) {
				val kit = Kits[args[1]]

				if (kit == Kits.NONE || !kit.data.isEnabled) {
					sender.sendMessage(Text.negativePrefix().negative("Não").basic(" há um kit com o nome \"").negative(args[1]).basic("\"!").toString())
				} else {
					val price = args[2].toIntOrNull()

					if (price != null) {
						if (kit.setPrice(price)) sender.sendMessage(Text.positivePrefix().basic("O preço do kit ").positive(kit.name).basic(" foi ").positive("alterado").basic("!").toString())
						else sender.sendMessage(Text.positivePrefix().basic("O preço do kit ").negative(kit.name).basic(" já era ").negative(price).basic(" créditos!").toString())
					} else {
						sender.sendMessage(Text.negativePrefix().basic("O valor ").negative("\"" + args[2] + "\"").basic(" não é um ").negative("número inteiro e positivo").basic("!").toString())
					}
				}
			} else {
				sender.sendMessage(Text.negativePrefix().basic("Uso: /shop ").negative("set <kit> <preço>").toString())
			}
		}

		return false
	}
}

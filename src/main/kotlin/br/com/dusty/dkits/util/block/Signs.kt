package br.com.dusty.dkits.util.block

import br.com.dusty.dkits.gamer.gamer
import br.com.dusty.dkits.util.clearFormatting
import br.com.dusty.dkits.util.inventory.Inventories
import br.com.dusty.dkits.util.millisToSeconds
import br.com.dusty.dkits.util.text.Text
import org.bukkit.block.Sign
import org.bukkit.entity.Player

fun Sign.interact(player: Player) {
	val gamer = player.gamer()

	when (getLine(1).clearFormatting()) {
		"[Grátis]" -> if (gamer.isOnSignCooldown()) player.sendMessage(Text.negativePrefix().basic("Você ainda deve ").negative("esperar").basic(" mais ").negative(gamer.signCooldown.millisToSeconds().toInt()).basic(
				" segundo(s) para usar essa placa novamente!").toString())
		else when (getLine(2).clearFormatting()) {
			"Sopa"    -> {
				player.openInventory(Inventories.soups(player))

				gamer.signCooldown = 10000
			}
			"Recraft" -> {
				player.openInventory(Inventories.recraft(player))

				gamer.signCooldown = 10000
			}
		}
		"Créditos" -> {
			//TODO: Send player back to where they came from
			val amount = getLine(2).substring(3).toIntOrNull() ?: 0

			gamer.addMoney(amount.toDouble())
			gamer.sendToWarp(gamer.warp, true, false)

			player.sendMessage(Text.positivePrefix().basic("Você ").positive("ganhou " + amount).basic(" créditos!").toString())
		}
		"XP"       -> {
			//TODO: Send player back to where they came from
			val amount = getLine(2).substring(3).toIntOrNull() ?: 0

			gamer.addXp(amount.toDouble())
			gamer.sendToWarp(gamer.warp, true, false)

			player.sendMessage(Text.positivePrefix().basic("Você ").positive("ganhou " + amount).basic(" XP!").toString())
		}
	}
}

fun Sign.isSpecial(): Boolean = getLine(0).endsWith("=")

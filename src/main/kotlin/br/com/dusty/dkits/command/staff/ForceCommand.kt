package br.com.dusty.dkits.command.staff

import br.com.dusty.dkits.command.PlayerCustomCommand
import br.com.dusty.dkits.gamer.EnumMode
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.gamer.gamer
import br.com.dusty.dkits.kit.Kits
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.warp.Warps
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object ForceCommand: PlayerCustomCommand(EnumRank.MODPLUS, "force") {

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		when {
			sender.gamer().mode != EnumMode.ADMIN -> sender.sendMessage(Text.negativePrefix().basic("Você ").negative("não").basic(" está no modo ").negative("ADMIN").basic("!").toString())
			args.size < 3                         -> sender.sendMessage(Text.negativePrefix().negative("Uso:").basic(" /force ").negative("<kit>/<warp> <jogador> <nomeDoKit>/<nomeDaWarp>").toString())
			else                                  -> when (args[0]) {
				"kit"  -> {
					val player = Bukkit.getPlayerExact(args[1])
					val kit = Kits[args[2]]

					when {
						player == null   -> sender.sendMessage(Text.negativePrefix().negative("Não").basic(" há um jogador online chamado ").negative("\"" + args[1] + "\"").basic("!").toString())
						kit == Kits.NONE -> sender.sendMessage(Text.negativePrefix().negative("Não").basic(" há um kit com o nome ").negative("\"" + args[2] + "\"").basic("!").toString())
						else             -> {
							kit.setAndApply(player.gamer(), false)

							sender.sendMessage(Text.positivePrefix().basic("Você ").positive("aplicou").basic(" o kit ").positive(kit.name).basic(" no jogador ").positive(player.name).basic("!").toString())
						}
					}
				}
				"warp" -> {
					val player = Bukkit.getPlayerExact(args[1])
					val warp = Warps[args[2]]

					when {
						player == null     -> sender.sendMessage(Text.negativePrefix().negative("Não").basic(" há um jogador online chamado ").negative("\"" + args[1] + "\"").basic("!").toString())
						warp == Warps.NONE -> sender.sendMessage(Text.negativePrefix().negative("Não").basic(" há uma warp chamada ").negative("\"" + args[2] + "\"").basic("!").toString())
						else               -> {
							val gamer = player.gamer()

							gamer.removeCombatTag()
							gamer.sendToWarp(warp, false)

							sender.sendMessage(Text.positivePrefix().basic("Você ").positive("enviou").basic(" o jogador ").positive(player.name).basic(" para a warp ").positive(warp.name).basic(
									"!").toString())
						}
					}
				}
			}
		}

		return false
	}
}
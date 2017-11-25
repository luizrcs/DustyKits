package br.com.dusty.dkits.command.staff

import br.com.dusty.dkits.command.PlayerCustomCommand
import br.com.dusty.dkits.gamer.EnumRank
import br.com.dusty.dkits.gamer.GamerRegistry
import br.com.dusty.dkits.gamer.gamer
import br.com.dusty.dkits.listener.mechanics.AsyncPlayerChatListener
import br.com.dusty.dkits.util.text.Text
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object ChatCommand: PlayerCustomCommand(EnumRank.MODPLUS, "chat") {

	val CHAT_CLEAR_MESSAGE: String

	init {
		val stringBuilder = StringBuilder()

		for (i in 1 .. 64) stringBuilder.append("\n")

		CHAT_CLEAR_MESSAGE = stringBuilder.toString()
	}

	override fun execute(sender: Player, alias: String, args: Array<String>): Boolean {
		val gamer = sender.gamer()

		if (args.isEmpty()) {
			sender.sendMessage(Text.negativePrefix().basic("Uso: /chat ").negative("<clear>/<restrict>").basic(" [rank]").toString())
		} else {
			val rank: EnumRank

			if (args.size > 1) {
				rank = EnumRank[args[1]]

				if (rank == EnumRank.NONE) {
					sender.sendMessage(Text.negativePrefix().negative("Não").basic(" há um rank com o nome \"").negative(args[0]).basic("\"!").toString())
					return false
				}
			} else {
				rank = gamer.rank
			}

			when (args[0]) {
				"clear"    -> {
					GamerRegistry.onlineGamers().forEach { if (it.rank.isLowerThanOrEquals(rank)) it.player.sendMessage(CHAT_CLEAR_MESSAGE) }

					sender.sendMessage(Text.neutralPrefix().basic("O ").neutral("chat").basic(" foi ").neutral("limpo").basic("!").toString())
				}
				"restrict" -> {
					AsyncPlayerChatListener.rank = rank

					if (rank == EnumRank.DEFAULT) Bukkit.broadcastMessage(Text.positivePrefix().basic("O chat agora está ").positive("liberado").basic(" para ").positive("todos").basic(" os jogadores!").toString())
					else Bukkit.broadcastMessage(Text.negativePrefix().basic("O chat agora está ").negative("restrito").basic(" apenas a ").negative(rank.string).basic(" e acima!").toString())
				}
			}
		}

		return false
	}
}
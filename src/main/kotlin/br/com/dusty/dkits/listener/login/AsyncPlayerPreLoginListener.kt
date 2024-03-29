package br.com.dusty.dkits.listener.login

import br.com.dusty.dkits.clan.ClanRegistry
import br.com.dusty.dkits.gamer.GamerRegistry
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.web.WebAPI
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent

object AsyncPlayerPreLoginListener: Listener {

	private val KICK_NO_PROFILE = Text.negativeOf("Não foi possível carregar seu perfil!\n\nVolte em alguns instantes...").toString()

	@EventHandler
	fun onAsyncPlayerPreLogin(event: AsyncPlayerPreLoginEvent) {
		val uuid = event.uniqueId

		val primitiveGamer = GamerRegistry.primitiveGamerFromJson(WebAPI.loadProfile(uuid), uuid)

		if (primitiveGamer == null) {
			event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, KICK_NO_PROFILE)
		} else {
			GamerRegistry.PRIMITIVE_GAMER_BY_UUID.put(uuid, primitiveGamer)

			val clanUuid = primitiveGamer.clan

			if (clanUuid != "" && !ClanRegistry.PRIMITIVE_CLAN_BY_STRING.containsKey(clanUuid)) ClanRegistry.primitiveClanFromJson(WebAPI.loadClan(clanUuid))?.run {
				ClanRegistry.PRIMITIVE_CLAN_BY_STRING.put(clanUuid, this)
			}
		}
	}
}

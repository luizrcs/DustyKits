package br.com.dusty.dkits.warp

import br.com.dusty.dkits.Main
import org.bukkit.Bukkit
import org.bukkit.inventory.ItemStack

object Warps {

	val NONE = NoneWarp
	val ANVIL = AnvilWarp
	val ARENA = ArenaWarp
	val FAKECXC = FakeCXC
	val FEAST = FeastWarp
	val GLADIATOR = GladiatorWarp
	val FPS = FpsWarp
	val HG = HGWarp
	val LAVA_CHALLENGE = LavaChallengeWarp
	val LOBBY = LobbyWarp
	val MDR = MDRWarp
	val ONE_VS_ONE = OneVsOneWarp
	val RDM = RDMWarp
	val VOLCANO = VolcanoWarp

	val WARPS = arrayListOf<Warp>()

	lateinit var enabledWarpsNames: Array<String>

	fun registerAll() {
		//Usage: WARPS.add(FOO);

		//Game
		WARPS.add(ARENA)
		WARPS.add(GLADIATOR)
		WARPS.add(FEAST)
		WARPS.add(VOLCANO)
		WARPS.add(ONE_VS_ONE)
		WARPS.add(FPS)
		WARPS.add(LAVA_CHALLENGE)
		WARPS.add(LOBBY)

		//Event
		WARPS.add(HG)
		WARPS.add(FAKECXC)
		WARPS.add(ANVIL)
		WARPS.add(MDR)
		WARPS.add(RDM)

		enabledWarpsNames = Warps.WARPS.filter { it.data.isEnabled }.map { it.name.toLowerCase().replace(" ", "") }.toTypedArray()

		WARPS.forEach { warp -> Bukkit.getPluginManager().registerEvents(warp, Main.INSTANCE) }
	}

	operator fun get(name: String) = WARPS.firstOrNull { it.name.toLowerCase().replace(" ", "") == name.toLowerCase() } ?: NONE

	operator fun get(icon: ItemStack) = WARPS.firstOrNull { it.icon == icon } ?: NONE
}

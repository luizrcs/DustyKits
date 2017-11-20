package br.com.dusty.dkits.kit

import br.com.dusty.dkits.Main
import br.com.dusty.dkits.ability.Ability
import br.com.dusty.dkits.gamer.EnumMode
import br.com.dusty.dkits.gamer.Gamer
import br.com.dusty.dkits.util.Tasks
import br.com.dusty.dkits.util.inventory.addItemStacks
import br.com.dusty.dkits.util.inventory.setArmor
import br.com.dusty.dkits.util.text.Text
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.io.*

open class Kit {

	var name = "None"
	var description = ""
	var icon = ItemStack(Material.STONE_SWORD)

	var weapon: ItemStack? = null
	var armor = arrayOf<ItemStack?>(null, null, null, null)
	var items = arrayOf<ItemStack?>()

	var ability = Ability()

	var isDummy = true
	var isBroadcast = false

	var data = Data()

	fun apply(gamer: Gamer) {
		gamer.clear()

		val player = gamer.player

		player.inventory.setItem(0, weapon)

		player.setArmor(armor)
		player.inventory.addItemStacks(items)
	}

	fun setAndApplyIfAllowed(gamer: Gamer) {
		val player = gamer.player

		//TODO: If not on MiniHG
		when {
			gamer.mode != EnumMode.ADMIN && !gamer.kit.isDummy                     -> {
				player.sendMessage(Text.negativePrefix().basic("Você ").negative("já").basic(" está ").negative("usando").basic(" um kit!").toString())
			}
			gamer.mode != EnumMode.ADMIN && !gamer.warp.enabledKits.contains(this) -> {
				player.sendMessage(Text.negativePrefix().basic("Você ").negative("não pode").basic(" usar o kit ").negative(name).basic(" nesta warp!").toString())
			}
			gamer.mode != EnumMode.ADMIN && !gamer.hasKit(this)                    -> {
				player.sendMessage(Text.negativePrefix().basic("Você ").negative("não").basic(" possui o kit ").negative(name).basic("!").toString())
			}
			else                                                                   -> {
				gamer.kit = this
				apply(gamer)

				gamer.updateScoreboard()

				player.sendMessage(Text.positivePrefix().basic("Agora você está ").positive("usando").basic(" o kit ").positive(name).basic("!").toString())
				//TODO: Titles/subtitles for 1.8+ players
			}
		}
	}

	fun enable(enabled: Boolean): Boolean {
		if (data.isEnabled == enabled) return false

		data.isEnabled = enabled

		Tasks.async(Runnable { this.saveData() })

		return true
	}

	fun loadData() {
		val dir = File(Main.CONFIG_DIR, "kit")
		val file = File(dir, name.toLowerCase() + ".json")

		if (file.exists()) try {
			data = Main.GSON.fromJson(FileReader(file), Kit.Data::class.java)
		} catch (e: FileNotFoundException) {
			e.printStackTrace()
		}
		else saveData()
	}

	fun saveData() {
		val dir = File(Main.CONFIG_DIR, "kit")
		val file = File(dir, name.toLowerCase() + ".json")

		var printWriter: PrintWriter? = null

		try {
			dir.mkdirs()
			file.createNewFile()

			printWriter = PrintWriter(file)
			printWriter.println(Main.GSON.toJson(data))
		} catch (e: IOException) {
			e.printStackTrace()
		} finally {
			if (printWriter != null) printWriter.close()
		}
	}

	data class Data(var price: Int = -1, var isEnabled: Boolean = false)
}

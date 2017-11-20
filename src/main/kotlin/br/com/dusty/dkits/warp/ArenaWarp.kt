package br.com.dusty.dkits.warp

import br.com.dusty.dkits.util.rename
import br.com.dusty.dkits.util.setDescription
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object ArenaWarp: Warp() {

	init {
		name = "Arena"
		icon = ItemStack(Material.STONE_SWORD)

		icon.rename(Text.of(name).color(TextColor.GOLD).toString())
		icon.setDescription(description)

		entryKit = Warp.GAME_WARP_KIT

		data.spreadRange = 4f

		loadData()
	}
}
package br.com.dusty.dkits.warp

import br.com.dusty.dkits.util.description
import br.com.dusty.dkits.util.rename
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object LavaChallengeWarp: Warp() {

	init {
		name = "Lava Challenge"

		icon = ItemStack(Material.LAVA_BUCKET)
		icon.rename(Text.of(name).color(TextColor.GOLD).toString())
		icon.description(description, true)

		entryKit = EMPTY_NOT_DUMMY_KIT

		loadData()
	}
}

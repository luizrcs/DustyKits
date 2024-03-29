package br.com.dusty.dkits.kit

import br.com.dusty.dkits.util.color
import br.com.dusty.dkits.util.description
import br.com.dusty.dkits.util.enchant
import br.com.dusty.dkits.util.rename
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object GrandpaKit: Kit() {

	init {
		name = "Grandpa"

		icon = ItemStack(Material.STICK).enchant(Enchantment.KNOCKBACK to 2, Enchantment.PROTECTION_FALL to 1)
		icon.rename(Text.of(name).color(TextColor.GOLD).toString())
		icon.description(description, true)

		weapon = ItemStack(Material.STONE_SWORD)
		armor = arrayOf(null, null, ItemStack(Material.LEATHER_CHESTPLATE).color(0x928C85), null)
		items = arrayOf(ItemStack(Material.STICK).enchant(Enchantment.KNOCKBACK to 2, Enchantment.PROTECTION_FALL to 1).rename("Bengala"))

		isDummy = false
		isBroadcast = true

		loadData()
	}
}

package br.com.dusty.dkits.util.graphics

import org.bukkit.Effect
import org.bukkit.Location

fun Location.playEffect(effect: Effect, data: Int, radius: Int) {
	world.playEffect(this, effect, data, radius)
}

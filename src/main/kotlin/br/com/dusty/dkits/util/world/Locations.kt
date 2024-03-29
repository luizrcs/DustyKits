package br.com.dusty.dkits.util.world

import br.com.dusty.dkits.Main
import br.com.dusty.dkits.util.stdlib.round
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World

fun Location.toSimpleLocation() = SimpleLocation(x, y, z, yaw, pitch)

fun Location.normalize() = Location(world, Math.floor(x) + 0.5, Math.floor(y) + 0.5, Math.floor(z) + 0.5, yaw.toInt().round(90.0).toFloat(), pitch.toInt().round(90.0).toFloat())

fun Location.spread(radius: Double): Location {
	if (radius == 0.0) return this

	val distance = Main.RANDOM.nextDouble() * radius
	val theta = Main.RANDOM.nextDouble() * 2 * Math.PI

	return this.clone().add(distance * Math.cos(theta), 0.0, distance * Math.sin(theta))
}

fun Location.generateGlassArena(width: Int, height: Int, length: Int, randomColors: Boolean, highestBlock: Boolean): Triple<Location, Location, Location> {
	val material: Material
	var data: Byte

	if (randomColors) {
		data = Main.RANDOM.nextInt(17).toByte()

		if (data == 16.toByte()) {
			material = Material.GLASS
			data = 0
		} else {
			material = Material.STAINED_GLASS
		}
	} else {
		material = Material.GLASS
		data = 0
	}

	val startLocation = clone()

	if (highestBlock) {
		var highestBlockY = 0

		for (x in 0 until width) {
			for (z in 0 until length) {
				val location = clone()
				location.x += x
				location.z += z

				val y = location.world.getHighestBlockYAt(location)

				if (y > highestBlockY) highestBlockY = y
			}
		}

		startLocation.y = highestBlockY + 48.0
	}

	for (x in 0 until width) {
		val floor = startLocation.clone()
		floor.x += x.toDouble()

		val ceiling = floor.clone()
		ceiling.y += height - 1

		for (z in 0 until length) {
			if (z != 0) {
				floor.z++
				ceiling.z++
			}

			val blockFloor = floor.block
			blockFloor.type = material
			blockFloor.data = data

			val blockCeiling = ceiling.block
			blockCeiling.type = material
			blockCeiling.data = data
		}
	}

	for (x in 0 until width) {
		val wall1 = startLocation.clone()
		wall1.x += x.toDouble()

		val wall2 = startLocation.clone()
		wall2.x += x.toDouble()
		wall2.z += length - 1

		for (y in 0 until height - 1) {
			if (y != 0) {
				wall1.y++

				val block1 = wall1.block
				block1.type = material
				block1.data = data

				wall2.y++

				val block2 = wall2.block
				block2.type = material
				block2.data = data
			}
		}
	}

	for (z in 0 until width) {
		val wall3 = startLocation.clone()
		wall3.z += z.toDouble()

		val wall4 = startLocation.clone()
		wall4.z += z.toDouble()
		wall4.x += width - 1

		for (y in 0 until height - 1) {
			if (y != 0) {
				wall3.y++

				val block3 = wall3.block
				block3.type = material
				block3.data = data

				wall4.y++

				val block4 = wall4.block
				block4.type = material
				block4.data = data
			}
		}
	}

	val firstPosition = startLocation.clone().add(1.5, 1.5, 1.5)
	firstPosition.yaw = 315F
	firstPosition.pitch = 0F

	val secondPosition = startLocation.clone().add(width - 2.5, 1.5, length - 2.5)
	secondPosition.yaw = 135F
	secondPosition.pitch = 0F

	return Triple(startLocation, firstPosition, secondPosition)
}

fun Location.destroyArena(width: Int, height: Int, length: Int) {
	for (x in 0 until width) for (y in 0 until height) for (z in 0 until length) clone().add(x.toDouble(), y.toDouble(), z.toDouble()).block.type = Material.AIR
}

object Locations {

	val GENERIC = Location(Worlds.WORLD, 0.0, 0.0, 0.0)
}

data class SimpleLocation(var x: Double = 0.0, var y: Double = 0.0, var z: Double = 0.0, var yaw: Float = 0F, var pitch: Float = 0F) {

	fun toLocation(world: World) = Location(world, x, y, z, yaw, pitch)
}

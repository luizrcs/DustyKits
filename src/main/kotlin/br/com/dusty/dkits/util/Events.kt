package br.com.dusty.dkits.util

import org.bukkit.event.player.PlayerMoveEvent

fun PlayerMoveEvent.isWalk() = from.x != to.x || from.z != to.z

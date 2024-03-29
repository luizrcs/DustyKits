package br.com.dusty.dkits.util.protocol

import br.com.dusty.dkits.util.text.TextColor

enum class EnumProtocolSafety constructor(val color: TextColor) {

	EXCELLENT(TextColor.DARK_GREEN),
	GOOD(TextColor.GREEN),
	REGULAR(TextColor.YELLOW),
	BAD(TextColor.RED),
	TERRIBLE(TextColor.DARK_RED)
}

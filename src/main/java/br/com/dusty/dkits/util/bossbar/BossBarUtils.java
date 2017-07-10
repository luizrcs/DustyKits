package br.com.dusty.dkits.util.bossbar;

import br.com.dusty.dkits.util.text.Text;
import br.com.dusty.dkits.util.text.TextColor;

public class BossBarUtils {
	
	public static final BossBar MAIN = BossBar.create(Text.of("Dusty")
	                                                      .color(TextColor.RED)
	                                                      .append(" - ")
	                                                      .color(TextColor.WHITE)
	                                                      .append("dusty.com.br")
	                                                      .color(TextColor.GOLD)
	                                                      .toString(),
	                                                  1,
	                                                  BossBar.EnumBarColor.PINK,
	                                                  BossBar.EnumBarStyle.PROGRESS,
	                                                  BossBar.EnumFlags.NONE);
}
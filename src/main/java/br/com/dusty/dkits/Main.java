package br.com.dusty.dkits;

import br.com.dusty.dkits.command.Commands;
import br.com.dusty.dkits.kit.Kits;
import br.com.dusty.dkits.listener.Listeners;
import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Random;

public class Main extends JavaPlugin {
	
	//TODO: public static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
	
	public static final Random RANDOM = new Random();
	public static final Gson GSON = new Gson();
	
	public static final File ROOT = new File(Bukkit.getWorldContainer(), "config");
	
	public static final int MAX_PLAYERS = 150;
	
	public static EnumServerStatus serverStatus = EnumServerStatus.OFFLINE;
	
	/**
	 * Instância deste {@link JavaPlugin}.
	 */
	public static Main INSTANCE;
	
	public Main() {
		INSTANCE = this;
		
		if(!ROOT.exists())
			ROOT.mkdirs();
	}
	
	@Override
	public void onLoad() {
	
	}
	
	@Override
	public void onEnable() {
		Commands.registerAll();
		Listeners.registerAll();
		Kits.registerAll();
		
		serverStatus = EnumServerStatus.ONLINE;
	}
	
	@Override
	public void onDisable() {
	
	}
}

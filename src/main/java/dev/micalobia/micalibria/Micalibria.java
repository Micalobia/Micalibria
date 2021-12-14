package dev.micalobia.micalibria;

import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Micalibria {
	public static final String MODID = "micalibria";
	public static final Logger LOGGER = LogManager.getLogger(MODID);

	public static Identifier id(String name) {
		return new Identifier(MODID, name);
	}
}

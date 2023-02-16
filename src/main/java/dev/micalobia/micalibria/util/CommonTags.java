package dev.micalobia.micalibria.util;

import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;


public class CommonTags {
	public static class Block {
		public static final TagKey<net.minecraft.block.Block> IMMOVABLE = TagKey.of(RegistryKeys.BLOCK, new Identifier("c", "immovable"));
	}
}

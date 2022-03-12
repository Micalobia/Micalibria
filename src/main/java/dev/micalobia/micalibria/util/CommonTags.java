package dev.micalobia.micalibria.util;

import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


public class CommonTags {
	public static class Block {
		public static final TagKey<net.minecraft.block.Block> IMMOVABLE = TagKey.of(Registry.BLOCK_KEY, new Identifier("c", "immovable"));
	}
}

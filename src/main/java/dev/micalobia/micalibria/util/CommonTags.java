package dev.micalobia.micalibria.util;

import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;


public class CommonTags {
	public static class Block {
		public static final Tag<net.minecraft.block.Block> IMMOVABLE = TagFactory.BLOCK.create(new Identifier("c", "immovable"));
	}
}

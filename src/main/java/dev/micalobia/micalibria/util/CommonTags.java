package dev.micalobia.micalibria.util;

import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.block.Block;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class CommonTags {
	public static final Tag<Block> IMMOVABLE = TagFactory.BLOCK.create(new Identifier("c", "immovable"));
}

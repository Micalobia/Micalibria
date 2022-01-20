package dev.micalobia.micalibria.block;

import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.block.Block;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class MicalibriaBlockTags {
	public static final Tag<Block> IMMOVABLE = TagFactory.BLOCK.create(new Identifier("c", "immovable"));
}

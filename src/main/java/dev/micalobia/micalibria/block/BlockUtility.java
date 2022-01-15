package dev.micalobia.micalibria.block;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


public class BlockUtility {
	@SuppressWarnings("unchecked")
	public static <E extends BlockEntity, B extends Block & BlockEntityProvider> BlockEntityType<E> register(B block) {
		return register(block, (p, s) -> (E) block.createBlockEntity(p, s));
	}

	public static <E extends BlockEntity> BlockEntityType<E> register(Block block, FabricBlockEntityTypeBuilder.Factory<E> factory) {
		return Registry.register(
				Registry.BLOCK_ENTITY_TYPE,
				Registry.BLOCK.getId(block),
				FabricBlockEntityTypeBuilder.create(factory, block).build()
		);
	}

	public static <B extends Block> B register(String id, B block) {
		return Registry.register(Registry.BLOCK, id, block);
	}

	public static <B extends Block> B register(Identifier id, B block) {
		return Registry.register(Registry.BLOCK, id, block);
	}
}

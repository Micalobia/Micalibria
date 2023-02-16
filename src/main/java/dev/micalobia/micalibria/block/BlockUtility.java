package dev.micalobia.micalibria.block;

import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;
import virtuoel.statement.api.StateRefresher;


public class BlockUtility {
	@SuppressWarnings("unchecked")
	public static <E extends BlockEntity, B extends Block & BlockEntityProvider> BlockEntityType<E> register(B block) {
		return register(block, (p, s) -> (E) block.createBlockEntity(p, s));
	}

	public static <E extends BlockEntity> BlockEntityType<E> register(Block block, FabricBlockEntityTypeBuilder.Factory<E> factory) {
		return Registry.register(
				Registries.BLOCK_ENTITY_TYPE,
				Registries.BLOCK.getId(block),
				FabricBlockEntityTypeBuilder.create(factory, block).build()
		);
	}

	public static <B extends Block> B register(String id, B block) {
		return Registry.register(Registries.BLOCK, id, block);
	}

	public static <B extends Block> B register(Identifier id, B block) {
		return Registry.register(Registries.BLOCK, id, block);
	}

	public static <B extends Block, P extends Comparable<P>> void injectBlockstateProperty(Class<B> klass, Property<P> property, P defaultValue) {
		injectBlockstateProperty(klass, property, defaultValue, true);
	}

	public static <B extends Block, P extends Comparable<P>> void injectBlockstateProperty(Class<B> klass, Property<P> property, P defaultValue, boolean includeFutureBlocks) {
		Registries.BLOCK.forEach(block -> {
			if(klass.isAssignableFrom(block.getClass()))
				StateRefresher.INSTANCE.addBlockProperty(block, property, defaultValue);
		});
		if(includeFutureBlocks) RegistryEntryAddedCallback.event(Registries.BLOCK).register((rawId, id, block) -> {
			if(klass.isAssignableFrom(block.getClass()))
				StateRefresher.INSTANCE.addBlockProperty(block, property, defaultValue);
		});
		StateRefresher.INSTANCE.reorderBlockStates();
	}

	public static <P extends Comparable<P>> void injectBlockstateProperty(Block block, Property<P> property, P defaultValue) {
		StateRefresher.INSTANCE.addBlockProperty(block, property, defaultValue);
	}
}

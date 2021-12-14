package dev.micalobia.micalibria.item;

import dev.micalobia.micalibria.mixin.item.ItemAccessor;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ItemUtility {
	public static <I extends Item> I register(String id, I value) {
		return Registry.register(Registry.ITEM, id, value);
	}

	public static <I extends Item> I register(Identifier id, I value) {
		return Registry.register(Registry.ITEM, id, value);
	}

	public static BlockItem register(Block block, ItemGroup group) {
		return register(new BlockItem(block, new FabricItemSettings().group(group)));
	}

	public static BlockItem register(Block block, Item.Settings settings) {
		return register(new BlockItem(block, settings));
	}

	public static BlockItem register(Block block, BlockItem item) {
		if(Registry.ITEM.getEntries().stream().map(Entry::getValue).noneMatch(x -> x == item))
			Registry.register(Registry.ITEM, Registry.BLOCK.getId(block), item);
		Item.BLOCK_ITEMS.put(block, item);
		return item;
	}

	public static BlockItem register(BlockItem item) {
		return register(Registry.BLOCK.getId(item.getBlock()), item);
	}

	public static int setMaxStackSize(Item item, int size) {
		return setTemplate(size, item::getMaxCount, ((ItemAccessor) item)::setMaxCount);
	}

	public static int setMaxDamage(Item item, int damage) {
		return setTemplate(damage, item::getMaxDamage, ((ItemAccessor) item)::setMaxDamage);
	}

	public static boolean setFireproof(Item item, boolean fireproof) {
		return setTemplate(fireproof, item::isFireproof, ((ItemAccessor) item)::setFireproof);
	}

	public static void setMaxStackSize(Class<? extends Item> klass, int size) {
		Registry.ITEM.stream().filter(x -> klass.isAssignableFrom(x.getClass())).forEach(x -> setMaxStackSize(x, size));
		RegistryEntryAddedCallback.event(Registry.ITEM).register(((rawId, id, object) -> {
			if(klass.isAssignableFrom(object.getClass()))
				setMaxStackSize(object, size);
		}));
	}

	public static void setMaxDamage(Class<? extends Item> klass, int damage) {
		Registry.ITEM.stream().filter(x -> klass.isAssignableFrom(x.getClass())).forEach(x -> setMaxDamage(x, damage));
		RegistryEntryAddedCallback.event(Registry.ITEM).register(((rawId, id, object) -> {
			if(klass.isAssignableFrom(object.getClass()))
				setMaxDamage(object, damage);
		}));
	}

	public static void setFireproof(Class<? extends Item> klass, boolean fireproof) {
		Registry.ITEM.stream().filter(x -> klass.isAssignableFrom(x.getClass())).forEach(x -> setFireproof(x, fireproof));
		RegistryEntryAddedCallback.event(Registry.ITEM).register(((rawId, id, object) -> {
			if(klass.isAssignableFrom(object.getClass()))
				setFireproof(object, fireproof);
		}));
	}

	private static <T> T setTemplate(T value, Supplier<T> supplier, Consumer<T> consumer) {
		T old = supplier.get();
		consumer.accept(value);
		return old;
	}
}

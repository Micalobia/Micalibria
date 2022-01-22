package dev.micalobia.micalibria.util.nbt;

import net.minecraft.nbt.NbtElement;

public interface NbtSerializationContext {
	<T> NbtElement serialize(T source);
}

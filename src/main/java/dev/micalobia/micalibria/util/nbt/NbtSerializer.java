package dev.micalobia.micalibria.util.nbt;

import net.minecraft.nbt.NbtCompound;

public interface NbtSerializer<T> {
	NbtCompound serialize(T value);
}

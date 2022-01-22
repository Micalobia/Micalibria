package dev.micalobia.micalibria.util.nbt;

import net.minecraft.nbt.NbtElement;

public interface NbtSerializer<T> {
	NbtElement serialize(T value);
}

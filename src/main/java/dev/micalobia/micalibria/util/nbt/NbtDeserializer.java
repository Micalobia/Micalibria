package dev.micalobia.micalibria.util.nbt;

import net.minecraft.nbt.NbtCompound;

public interface NbtDeserializer<T> {
	T deserialize(NbtCompound nbt);

	default T deserialize() {
		return deserialize(new NbtCompound());
	}
}

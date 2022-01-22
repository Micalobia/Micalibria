package dev.micalobia.micalibria.util.nbt;

import net.minecraft.nbt.NbtElement;

public interface NbtDeserializationContext {
	<T> T deserialize(NbtElement element, Class<T> klass);
}

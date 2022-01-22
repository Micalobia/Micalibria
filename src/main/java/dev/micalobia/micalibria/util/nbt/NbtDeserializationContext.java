package dev.micalobia.micalibria.util.nbt;

import net.minecraft.nbt.NbtElement;

import java.lang.reflect.Type;

public interface NbtDeserializationContext {
	<T> T deserialize(NbtElement element, Type klass);
}

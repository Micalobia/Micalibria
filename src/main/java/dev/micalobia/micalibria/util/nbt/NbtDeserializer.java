package dev.micalobia.micalibria.util.nbt;

import dev.micalobia.micalibria.util.nbt.exceptions.NbtParseException;
import net.minecraft.nbt.NbtElement;

public interface NbtDeserializer<T> {
	T deserialize(NbtElement nbt, NbtDeserializationContext context) throws NbtParseException;
}

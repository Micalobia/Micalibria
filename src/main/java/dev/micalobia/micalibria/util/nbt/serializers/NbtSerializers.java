package dev.micalobia.micalibria.util.nbt.serializers;

import dev.micalobia.micalibria.util.nbt.NbtSerializer;
import net.minecraft.nbt.*;

public class NbtSerializers {
	public static final NbtSerializer<Byte> BYTE = (value, context) -> NbtByte.of(value);
	public static final NbtSerializer<Short> SHORT = (value, context) -> NbtShort.of(value);
	public static final NbtSerializer<Integer> INT = (value, context) -> NbtInt.of(value);
	public static final NbtSerializer<Long> LONG = (value, context) -> NbtLong.of(value);
	public static final NbtSerializer<Float> FLOAT = (value, context) -> NbtFloat.of(value);
	public static final NbtSerializer<Double> DOUBLE = (value, context) -> NbtDouble.of(value);
	public static final NbtSerializer<String> STRING = (value, context) -> NbtString.of(value);
	public static final NbtSerializer<byte[]> BYTE_ARRAY = (value, context) -> new NbtByteArray(value);
	public static final NbtSerializer<int[]> INT_ARRAY = (value, context) -> new NbtIntArray(value);
	public static final NbtSerializer<long[]> LONG_ARRAY = (value, context) -> new NbtLongArray(value);
	public static final NbtSerializer<Boolean> BOOLEAN = (value, context) -> NbtByte.of(value);
}

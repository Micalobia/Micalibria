package dev.micalobia.micalibria.util.nbt;

import net.minecraft.nbt.*;

public class NbtSerializers {
	public static final NbtSerializer<Byte> BYTE = NbtByte::of;
	public static final NbtSerializer<Short> SHORT = NbtShort::of;
	public static final NbtSerializer<Integer> INT = NbtInt::of;
	public static final NbtSerializer<Long> LONG = NbtLong::of;
	public static final NbtSerializer<Float> FLOAT = NbtFloat::of;
	public static final NbtSerializer<Double> DOUBLE = NbtDouble::of;
	public static final NbtSerializer<String> STRING = NbtString::of;
	public static final NbtSerializer<byte[]> BYTE_ARRAY = NbtByteArray::new;
	public static final NbtSerializer<int[]> INT_ARRAY = NbtIntArray::new;
	public static final NbtSerializer<long[]> LONG_ARRAY = NbtLongArray::new;
	public static final NbtSerializer<Boolean> BOOLEAN = NbtByte::of;
}

package dev.micalobia.micalibria.util.nbt.serializers;

import com.google.gson.*;
import dev.micalobia.micalibria.util.nbt.NbtDeserializer;
import dev.micalobia.micalibria.util.nbt.exceptions.NbtParseException;
import net.minecraft.nbt.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class NbtDeserializers {
	public static final NbtDeserializer<Byte> BYTE = (nbt, context) -> ((NbtByte) validate(nbt, NbtElement.BYTE_TYPE, "Not a byte!")).byteValue();
	public static final NbtDeserializer<Short> SHORT = (nbt, context) -> ((NbtShort) validate(nbt, NbtElement.SHORT_TYPE, "Not a short!")).shortValue();
	public static final NbtDeserializer<Integer> INT = (nbt, context) -> ((NbtInt) validate(nbt, NbtElement.INT_TYPE, "Not an int!")).intValue();
	public static final NbtDeserializer<Long> LONG = (nbt, context) -> ((NbtLong) validate(nbt, NbtElement.LONG_TYPE, "Not a long!")).longValue();
	public static final NbtDeserializer<Float> FLOAT = (nbt, context) -> ((NbtFloat) validate(nbt, NbtElement.FLOAT_TYPE, "Not a float!")).floatValue();
	public static final NbtDeserializer<Double> DOUBLE = (nbt, context) -> ((NbtDouble) validate(nbt, NbtElement.DOUBLE_TYPE, "Not a double!")).doubleValue();
	public static final NbtDeserializer<String> STRING = (nbt, context) -> validate(nbt, NbtElement.STRING_TYPE, "Not a string!").asString();
	public static final NbtDeserializer<byte[]> BYTE_ARRAY = (nbt, context) -> ((NbtByteArray) validate(nbt, NbtElement.BYTE_ARRAY_TYPE, "Not a byte array")).getByteArray();
	public static final NbtDeserializer<int[]> INT_ARRAY = (nbt, context) -> ((NbtIntArray) validate(nbt, NbtElement.INT_ARRAY_TYPE, "Not an int array")).getIntArray();
	public static final NbtDeserializer<long[]> LONG_ARRAY = (nbt, context) -> ((NbtLongArray) validate(nbt, NbtElement.LONG_ARRAY_TYPE, "Not a long array")).getLongArray();
	public static final NbtDeserializer<Boolean> BOOLEAN = (nbt, context) -> ((NbtByte) validate(nbt, NbtElement.BYTE_TYPE, "Not a byte!")).byteValue() != 0;
	@SuppressWarnings("unchecked")
	public static final NbtDeserializer<JsonElement> JSON_ELEMENT = (nbt, context) -> {
		switch(nbt.getType()) {
			case NbtElement.END_TYPE:
				return JsonNull.INSTANCE;
			case NbtElement.BYTE_TYPE:
			case NbtElement.SHORT_TYPE:
			case NbtElement.INT_TYPE:
			case NbtElement.LONG_TYPE:
			case NbtElement.FLOAT_TYPE:
			case NbtElement.DOUBLE_TYPE:
			case NbtElement.NUMBER_TYPE:
				return new JsonPrimitive(((AbstractNbtNumber) nbt).numberValue());
			case NbtElement.STRING_TYPE:
				return new JsonPrimitive(nbt.asString());
			case NbtElement.LIST_TYPE:
				var nbtList = (AbstractNbtList<?>) nbt;
				var list = new JsonArray(nbtList.size());
				for(var element : nbtList) list.add(context.deserialize(element, JsonElement.class));
				return list;
			case NbtElement.COMPOUND_TYPE:
				var nbtCompound = (NbtCompound) nbt;
				var obj = new JsonObject();
				for(var key : nbtCompound.getKeys())
					obj.add(key, context.deserialize(nbtCompound.get(key), JsonElement.class));
				return obj;
			case NbtElement.BYTE_ARRAY_TYPE:
			case NbtElement.INT_ARRAY_TYPE:
			case NbtElement.LONG_ARRAY_TYPE:
				var nbtArr = (AbstractNbtList<? extends AbstractNbtNumber>) nbt;
				var arr = new JsonArray(nbtArr.size());
				for(var element : nbtArr) arr.add(element.numberValue());
				return arr;
			default:
				throw new AssertionError();
		}
	};

	@Contract("_, _, _ -> param1")
	private static @NotNull NbtElement validate(NbtElement self, byte type, String message) {
		if(self.getType() != type) throw new NbtParseException(message);
		return self;
	}
}

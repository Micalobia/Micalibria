package dev.micalobia.micalibria.util.nbt;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

@SuppressWarnings("unchecked")
public class NbtSerialization {
	private static final Map<Class<?>, NbtDeserializer<?>> deserializerCache = new HashMap<>();
	private static final Map<Class<?>, NbtSerializer<?>> serializerCache = new HashMap<>();

	@NotNull
	public static <T> NbtDeserializer<T> deserializer(@NotNull Class<T> klass) {
		if(deserializerCache.containsKey(klass)) return (NbtDeserializer<T>) deserializerCache.get(klass);
		if(!klass.isAnnotationPresent(NbtDeserializable.class))
			throw new RuntimeException(String.format("Missing %s annotation on %s", NbtDeserializable.class.getSimpleName(), klass.getName()));
		var deserializerClassMaybe = Arrays.stream(klass.getDeclaredClasses()).filter(klass::isAssignableFrom).findFirst();
		if(deserializerClassMaybe.isEmpty())
			throw new RuntimeException(String.format("Class %s marked with @%s has no inner %s", klass.getName(), NbtDeserializable.class.getSimpleName(), NbtDeserializer.class.getSimpleName()));
		try {
			var deserializer = (NbtDeserializer<T>) deserializerClassMaybe.get().getConstructor().newInstance();
			if(klass.getAnnotation(NbtDeserializable.class).cacheable())
				deserializerCache.put(klass, deserializer);
			return deserializer;
		} catch(NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@NotNull
	public static <T> NbtSerializer<T> serializer(@NotNull Class<T> klass) {
		if(serializerCache.containsKey(klass)) return (NbtSerializer<T>) serializerCache.get(klass);
		if(!klass.isAnnotationPresent(NbtSerializable.class))
			throw new RuntimeException(String.format("Missing %s annotation on %s", NbtSerializable.class.getSimpleName(), klass.getName()));
		var serializerClassMaybe = Arrays.stream(klass.getDeclaredClasses()).filter(klass::isAssignableFrom).findFirst();
		if(serializerClassMaybe.isEmpty())
			throw new RuntimeException(String.format("Class %s marked with @%s has no inner %s", klass.getName(), NbtSerializable.class.getSimpleName(), NbtSerializer.class.getSimpleName()));
		try {
			var serializer = (NbtSerializer<T>) serializerClassMaybe.get().getConstructor().newInstance();
			if(klass.getAnnotation(NbtSerializable.class).cacheable())
				serializerCache.put(klass, serializer);
			return serializer;
		} catch(NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> NbtCompound serialize(T value) {
		return serializer(((Class<T>) value.getClass())).serialize(value);
	}

	public static <T> T deserialize(NbtCompound nbt, Class<T> klass) {
		return deserializer(klass).deserialize(nbt);
	}

	public static <T> NbtList serialize(Iterable<T> values) {
		var ret = new NbtList();
		Iterator<T> iterator = values.iterator();
		if(!iterator.hasNext()) return ret;
		var first = iterator.next();
		var serializer = serializer((Class<T>) first.getClass());
		ret.add(serializer.serialize(first));
		while(iterator.hasNext())
			ret.add(serializer.serialize(iterator.next()));
		return ret;
	}

	public static <T> NbtList serialize(T... values) {
		var ret = new NbtList();
		if(values.length == 0) return ret;
		var first = values[0];
		var serializer = serializer((Class<T>) first.getClass());
		ret.add(serializer.serialize(first));
		for(int i = 1; i < values.length; ++i)
			ret.add(serializer.serialize(values[i]));
		return ret;
	}

	public static <T> List<T> deserialize(NbtList list, Class<T> klass) {
		if(list.isEmpty()) return List.of();
		if(list.getHeldType() != NbtCompound.COMPOUND_TYPE)
			throw new RuntimeException("NbtList isn't of type NbtCompound");
		var deserializer = deserializer(klass);
		return list.stream().map(x -> deserializer.deserialize((NbtCompound) x)).toList();
	}
}

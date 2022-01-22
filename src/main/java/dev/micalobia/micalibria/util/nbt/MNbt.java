package dev.micalobia.micalibria.util.nbt;

import dev.micalobia.micalibria.util.nbt.exceptions.MissingNbtDeserializerException;
import dev.micalobia.micalibria.util.nbt.exceptions.MissingNbtSerializerException;
import dev.micalobia.micalibria.util.nbt.exceptions.NbtParseException;
import dev.micalobia.micalibria.util.nbt.serializers.NbtDeserializers;
import net.minecraft.nbt.NbtElement;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("rawtypes")
public class MNbt {
	private final Map<Class<?>, NbtDeserializer> deserializers;
	private final Map<Class<?>, NbtSerializer> serializers;
	private final Map<Class<?>, NbtDeserializerSupplier> uncachedDeserializers;
	private final Map<Class<?>, NbtSerializerSupplier> uncachedSerializers;
	private final Map<Class<?>, Boolean> registeredDeserializers;
	private final Map<Class<?>, Boolean> registeredSerializers;

	private MNbt(
			Map<Class<?>, NbtDeserializer> deserializers,
			Map<Class<?>, NbtSerializer> serializers,
			Map<Class<?>, NbtDeserializerSupplier> uncachedDeserializers,
			Map<Class<?>, NbtSerializerSupplier> uncachedSerializers,
			Map<Class<?>, Boolean> registeredDeserializers,
			Map<Class<?>, Boolean> registeredSerializers
	) {
		this.deserializers = deserializers;
		this.serializers = serializers;
		this.uncachedDeserializers = uncachedDeserializers;
		this.uncachedSerializers = uncachedSerializers;
		this.registeredDeserializers = registeredDeserializers;
		this.registeredSerializers = registeredSerializers;
	}

	@SuppressWarnings("unchecked")
	public <T> NbtElement serialize(T value) throws NbtParseException {
		Class<T> klass = (Class<T>) value.getClass();
		if(!registeredSerializers.containsKey(klass))
			throw new NbtParseException("Could not find serializer for " + klass.getName());
		NbtSerializer<T> serializer;
		if(registeredSerializers.get(klass)) serializer = serializers.get(klass);
		else serializer = uncachedSerializers.get(klass).get();
		return serializer.serialize(value);
	}

	@SuppressWarnings("unchecked")
	public <T> T deserialize(NbtElement element, Class<T> klass) {
		if(!registeredDeserializers.containsKey(klass))
			throw new NbtParseException("Could not find deserializer for " + klass.getName());
		NbtDeserializer<T> deserializer;
		if(registeredDeserializers.get(klass)) deserializer = deserializers.get(klass);
		else deserializer = uncachedDeserializers.get(klass).get();
		return deserializer.deserialize(element);
	}

	@FunctionalInterface
	public interface NbtDeserializerSupplier<T> {
		NbtDeserializer<T> get();
	}

	@FunctionalInterface
	public interface NbtSerializerSupplier<T> {
		NbtSerializer<T> get();
	}

	public static final class Builder {
		private final Map<Class<?>, NbtDeserializer> deserializers = new HashMap<>();
		private final Map<Class<?>, NbtSerializer> serializers = new HashMap<>();
		private final Map<Class<?>, NbtDeserializerSupplier> uncachedDeserializers = new HashMap<>();
		private final Map<Class<?>, NbtSerializerSupplier> uncachedSerializers = new HashMap<>();
		private final Map<Class<?>, Boolean> registeredDeserializers = new HashMap<>();
		private final Map<Class<?>, Boolean> registeredSerializers = new HashMap<>();

		private Builder() {
			put(Byte.class, NbtDeserializers.BYTE);
			put(Short.class, NbtDeserializers.SHORT);
			put(Integer.class, NbtDeserializers.INT);
			put(Long.class, NbtDeserializers.LONG);
			put(Float.class, NbtDeserializers.FLOAT);
			put(Double.class, NbtDeserializers.DOUBLE);
			put(String.class, NbtDeserializers.STRING);
			put(byte[].class, NbtDeserializers.BYTE_ARRAY);
			put(int[].class, NbtDeserializers.INT_ARRAY);
			put(long[].class, NbtDeserializers.LONG_ARRAY);
			put(Boolean.class, NbtDeserializers.BOOLEAN);
			put(Byte.class, NbtSerializers.BYTE);
			put(Short.class, NbtSerializers.SHORT);
			put(Integer.class, NbtSerializers.INT);
			put(Long.class, NbtSerializers.LONG);
			put(Float.class, NbtSerializers.FLOAT);
			put(Double.class, NbtSerializers.DOUBLE);
			put(String.class, NbtSerializers.STRING);
			put(byte[].class, NbtSerializers.BYTE_ARRAY);
			put(int[].class, NbtSerializers.INT_ARRAY);
			put(long[].class, NbtSerializers.LONG_ARRAY);
			put(Boolean.class, NbtSerializers.BOOLEAN);
		}

		@Contract(" -> new")
		public static @NotNull Builder start() {
			return new Builder();
		}

		@SuppressWarnings("unchecked")
		@Contract("_ -> this")
		public <T> Builder put(@NotNull Class<T> klass) throws MissingNbtDeserializerException, MissingNbtSerializerException {
			Objects.requireNonNull(klass);
			if(klass.isAnnotationPresent(NbtDeserializable.class)) {
				final var annotation = klass.getAnnotation(NbtDeserializable.class);
				var tryDeserializerKlass = annotation.deserializer();
				if(tryDeserializerKlass == NbtDeserializer.class) {
					var optionalDeserializerKlass = Arrays
							.stream(klass.getDeclaredClasses())
							.filter(NbtDeserializer.class::isAssignableFrom)
							.findFirst();
					tryDeserializerKlass = (Class<? extends NbtDeserializer<?>>) optionalDeserializerKlass.orElseThrow(() -> new MissingNbtDeserializerException(klass.getSimpleName()));
				}
				final var deserializerKlass = tryDeserializerKlass;
				try {
					var constructor = deserializerKlass.getConstructor();
					if(annotation.cacheable()) {
						var instance = constructor.newInstance();
						return put(klass, instance);
					} else {
						return put(klass, () -> {
							try {
								return constructor.newInstance();
							} catch(InvocationTargetException | InstantiationException | IllegalAccessException e) {
								throw new MissingNbtDeserializerException("Could not instantiate %s for deserialization".formatted(deserializerKlass.getName()), e);
							}
						});
					}
				} catch(NoSuchMethodException e) {
					throw new MissingNbtDeserializerException("%s missing parameterless constructor".formatted(deserializerKlass.getName()), e);
				} catch(InvocationTargetException | InstantiationException | IllegalAccessException e) {
					throw new MissingNbtDeserializerException("Could not instantiate %s for deserialization cache".formatted(deserializerKlass.getName()), e);
				}
			}
			if(klass.isAnnotationPresent(NbtSerializable.class)) {
				final var annotation = klass.getAnnotation(NbtSerializable.class);
				var trySerializerKlass = annotation.serializer();
				if(trySerializerKlass == NbtSerializer.class) {
					var optionalSerializerKlass = Arrays
							.stream(klass.getDeclaredClasses())
							.filter(NbtSerializer.class::isAssignableFrom)
							.findFirst();
					trySerializerKlass = (Class<? extends NbtSerializer<?>>) optionalSerializerKlass.orElseThrow(() -> new MissingNbtSerializerException(klass.getSimpleName()));
				}
				final var serializerKlass = trySerializerKlass;
				try {
					var constructor = serializerKlass.getConstructor();
					if(annotation.cacheable()) {
						var instance = constructor.newInstance();
						return put(klass, instance);
					} else {
						return put(klass, () -> {
							try {
								return constructor.newInstance();
							} catch(InvocationTargetException | InstantiationException | IllegalAccessException e) {
								throw new MissingNbtSerializerException("Could not instantiate %s for serialization".formatted(serializerKlass.getName()), e);
							}
						});
					}
				} catch(NoSuchMethodException e) {
					throw new MissingNbtSerializerException("%s missing parameterless constructor".formatted(serializerKlass.getName()), e);
				} catch(InvocationTargetException | InstantiationException | IllegalAccessException e) {
					throw new MissingNbtSerializerException("Could not instantiate %s for serialization cache".formatted(serializerKlass.getName()), e);
				}
			}
			return this;
		}

		@Contract("_, _ -> this")
		public <T> Builder put(@NotNull Class<T> klass, @NotNull NbtDeserializer<T> deserializer) {
			Objects.requireNonNull(klass);
			Objects.requireNonNull(deserializer);
			deserializers.put(klass, deserializer);
			registeredDeserializers.put(klass, true);
			return this;
		}

		@Contract("_, _ -> this")
		public <T> Builder put(@NotNull Class<T> klass, @NotNull NbtDeserializerSupplier<T> supplier) {
			Objects.requireNonNull(klass);
			Objects.requireNonNull(supplier);
			uncachedDeserializers.put(klass, supplier);
			registeredDeserializers.put(klass, false);
			return this;
		}

		@Contract("_, _ -> this")
		public <T> Builder put(@NotNull Class<T> klass, @NotNull NbtSerializer<T> serializer) {
			Objects.requireNonNull(klass);
			Objects.requireNonNull(serializer);
			serializers.put(klass, serializer);
			registeredSerializers.put(klass, true);
			return this;
		}

		@Contract("_, _ -> this")
		public <T> Builder put(@NotNull Class<T> klass, @NotNull NbtSerializerSupplier<T> supplier) {
			Objects.requireNonNull(klass);
			Objects.requireNonNull(supplier);
			uncachedSerializers.put(klass, supplier);
			registeredSerializers.put(klass, false);
			return this;
		}

		@Contract(value = "-> new", pure = true)
		public @NotNull MNbt build() {
			return new MNbt(deserializers, serializers, uncachedDeserializers, uncachedSerializers, registeredDeserializers, registeredSerializers);
		}
	}
}

package dev.micalobia.micalibria.util.nbt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface NbtDeserializable {
	boolean cacheable() default true;

	Class<? extends NbtDeserializer> deserializer() default NbtDeserializer.class;
}

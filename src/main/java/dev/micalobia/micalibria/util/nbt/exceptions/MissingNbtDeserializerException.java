package dev.micalobia.micalibria.util.nbt.exceptions;

public class MissingNbtDeserializerException extends RuntimeException {
	public MissingNbtDeserializerException(String msg) {
		super(msg);
	}

	public MissingNbtDeserializerException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public MissingNbtDeserializerException(Throwable cause) {
		super(cause);
	}
}

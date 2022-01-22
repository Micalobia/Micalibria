package dev.micalobia.micalibria.util.nbt.exceptions;

public class MissingNbtSerializerException extends RuntimeException {
	public MissingNbtSerializerException(String msg) {
		super(msg);
	}

	public MissingNbtSerializerException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public MissingNbtSerializerException(Throwable cause) {
		super(cause);
	}
}


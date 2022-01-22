package dev.micalobia.micalibria.util.nbt.exceptions;

public class NbtParseException extends RuntimeException {
	public NbtParseException(String msg) {
		super(msg);
	}

	public NbtParseException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public NbtParseException(Throwable cause) {
		super(cause);
	}
}

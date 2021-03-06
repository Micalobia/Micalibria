package dev.micalobia.micalibria.event.enums;


public class TypedEventReaction<T> {
	private final EventReaction reaction;
	private final T value;

	private TypedEventReaction(EventReaction reaction, T value) {
		this.reaction = reaction;
		this.value = value;
	}

	public static <T> TypedEventReaction<T> complete(T value) {
		return new TypedEventReaction<>(EventReaction.COMPLETE, value);
	}

	public static <T> TypedEventReaction<T> processed(T value) {
		return new TypedEventReaction<>(EventReaction.PROCESS, value);
	}

	public static <T> TypedEventReaction<T> ignore(T value) {
		return new TypedEventReaction<>(EventReaction.IGNORE, value);
	}

	public static <T> TypedEventReaction<T> ignore() {
		return ignore(null);
	}

	public static <T> TypedEventReaction<T> cancel(T value) {
		return new TypedEventReaction<>(EventReaction.CANCEL, value);
	}

	public static <T> TypedEventReaction<T> cancel() {
		return cancel(null);
	}

	/**
	 * Returns <code>IGNORE(null)</code> if the old and new are the same (using <code>==</code>), and <code>COMPLETE(newState)</code> if they aren't
	 */
	public static <T> TypedEventReaction<T> completeIfChanged(T oldState, T newState) {
		return oldState == newState ? TypedEventReaction.ignore() : TypedEventReaction.complete(newState);
	}

	public EventReaction getReaction() {
		return reaction;
	}

	public T getValue() {
		return value;
	}

	public String toString() {
		return String.format("%s(%s)", reaction, value);
	}
}

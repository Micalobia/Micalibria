package dev.micalobia.micalibria.event.enums;

public class PairedEventReaction<T, K> {
	private final EventReaction reaction;
	private final T left;
	private final K right;

	private PairedEventReaction(EventReaction reaction, T left, K right) {
		this.reaction = reaction;
		this.left = left;
		this.right = right;
	}

	public static <T, K> PairedEventReaction<T, K> complete(T left, K right) {
		return new PairedEventReaction<>(EventReaction.COMPLETE, left, right);
	}

	public static <T, K> PairedEventReaction<T, K> processed(T left, K right) {
		return new PairedEventReaction<>(EventReaction.PROCESS, left, right);
	}

	public static <T, K> PairedEventReaction<T, K> ignore(T left, K right) {
		return new PairedEventReaction<>(EventReaction.IGNORE, left, right);
	}

	public static <T, K> PairedEventReaction<T, K> ignore() {
		return ignore(null, null);
	}

	public static <T, K> PairedEventReaction<T, K> cancel(T left, K right) {
		return new PairedEventReaction<>(EventReaction.CANCEL, left, right);
	}

	public static <T, K> PairedEventReaction<T, K> cancel() {
		return cancel(null, null);
	}

	public EventReaction getReaction() {
		return reaction;
	}

	public T getLeft() {
		return left;
	}

	public K getRight() {
		return right;
	}
}

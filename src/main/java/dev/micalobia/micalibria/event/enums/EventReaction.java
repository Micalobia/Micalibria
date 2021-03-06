package dev.micalobia.micalibria.event.enums;

public enum EventReaction {
	COMPLETE,
	PROCESS,
	IGNORE,
	CANCEL;

	public boolean continueProcessing() {
		switch(this) {
			case COMPLETE:
			case CANCEL:
				return false;
			default:
				return true;
		}
	}
}

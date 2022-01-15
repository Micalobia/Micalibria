package dev.micalobia.micalibria.util;

import java.util.Iterator;

public interface EnumFlag<T extends Enum<T> & EnumFlag<T>> {
	static <T extends Enum<T> & EnumFlag<T>> int getFlag(Iterable<T> iterable) {
		Iterator<T> iterator = iterable.iterator();
		int ret = 0;
		while(iterator.hasNext())
			ret |= iterator.next().getFlag();
		return ret;
	}

	int getFlag();

	T fromFlag(int flag);
}

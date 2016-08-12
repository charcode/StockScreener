package com.oak.external.utils.collections.api;

import java.util.Collection;

public interface CollectionUtils {

	<I, O> Collection<O> transpose(Collection<I>collection, Transposer<I, O>transformer);

	public interface Transposer<I,O>{
		O transpose(I i);
	}

}

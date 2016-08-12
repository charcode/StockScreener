package com.oak.external.utils.collections.api.apache.impl;

import java.util.ArrayList;
import java.util.Collection;

import com.oak.external.utils.collections.api.CollectionUtils;

public abstract class CollectionUtilsApacheCommonsImpl implements
		CollectionUtils {

	@Override
	public <I, O> Collection<O> transpose(final Collection<I> collection,
			Transposer<I, O> transposer) {
		Collection<O> ret = null;
		if (collection != null) {
			ret = new ArrayList<O>(collection.size());

			for (I i : collection) {
				ret.add(transposer.transpose(i));
			}
		}
		return ret;
	}
}

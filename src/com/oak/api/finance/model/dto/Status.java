package com.oak.api.finance.model.dto;

import java.util.Set;

import com.google.common.collect.Sets;

public enum Status {
	SUCCESS,
	FAIL,
	SUCCESS_WITH_ISSUES,
	IN_PROGRESS,
	IN_TEST,
	UNKNOWN;
	
	public static Set<Status>successStatuses(){
		return Sets.newHashSet(SUCCESS);
	}
}

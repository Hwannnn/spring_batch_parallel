package com.batch.partitioning.exam.configration.common;

public enum TestService {
	MY,
	YOUR;

	public String getKey() {
		return this.name();
	}
}

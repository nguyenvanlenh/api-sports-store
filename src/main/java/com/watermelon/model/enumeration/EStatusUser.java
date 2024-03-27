package com.watermelon.model.enumeration;

public enum EStatusUser {
	ACTIVITY(1), NON_ACTIVITY(0);
	private final int code;

	private EStatusUser(int code) {
		this.code = code;
	}
	public int getCode() {
		return code;
	}
}

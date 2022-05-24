package com.way.stock.rewards.enums;

public enum TimeInForce {
	gtc("gtc"), day("day"), ioc("ioc"), fok("fok");

	String type;

	TimeInForce(String type) {
		this.type = type;
	}

	public String getValue() {
		return type;
	}

	public static TimeInForce getOrderType(String type) {
		for (TimeInForce timeInForceType : TimeInForce.values()) {
			if (timeInForceType.name().equalsIgnoreCase(type)) {
				return timeInForceType;
			}
		}
		return null;
	}
}

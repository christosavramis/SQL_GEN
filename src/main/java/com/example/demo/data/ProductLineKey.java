package com.example.demo.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductLineKey {
	MOTOR("Auto"), BTM("BTM"), SMART_DRIVE("SMART"), PROPERTY("HOME");
	private final String strategyDisplayKey;
}

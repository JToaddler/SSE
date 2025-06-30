package com.madlabs.live.model;

import java.util.Map;

public class SseEvent {

	private String uuid;
	private SseEventType type;
	private Map<String, Object> data;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public SseEventType getType() {
		return type;
	}

	public void setType(SseEventType type) {
		this.type = type;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public SseEvent(String uuid, SseEventType type, Map<String, Object> data) {
		super();
		this.uuid = uuid;
		this.type = type;
		this.data = data;
	}

	public SseEvent(SseEventType type, Map<String, Object> data) {
		super();
		this.type = type;
		this.data = data;
	}

}
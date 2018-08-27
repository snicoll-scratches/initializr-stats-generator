package com.example.initializr.stats.generator.web;

import java.util.List;

import com.example.initializr.stats.generator.Event;

class EventsDescriptor {

	private final List<Event> events;

	EventsDescriptor(List<Event> events) {
		this.events = events;
	}

	public List<Event> getEvents() {
		return this.events;
	}

}

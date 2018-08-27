package com.example.initializr.stats.generator;

import java.time.LocalDate;

import org.springframework.util.MultiValueMap;

public class GenerationStatistics {

	private final DateRange range;

	private final MultiValueMap<String, Entry> entries;

	GenerationStatistics(DateRange range, MultiValueMap<String, Entry> entries) {
		this.range = range;
		this.entries = entries;
	}

	public DateRange getRange() {
		return this.range;
	}

	public MultiValueMap<String, Entry> getEntries() {
		return this.entries;
	}

	public static class Entry {

		private final LocalDate date;

		private final int projectsCount;

		public Entry(LocalDate date, int projectsCount) {
			this.date = date;
			this.projectsCount = projectsCount;
		}

		public LocalDate getDate() {
			return this.date;
		}

		public int getProjectsCount() {
			return this.projectsCount;
		}

	}

}

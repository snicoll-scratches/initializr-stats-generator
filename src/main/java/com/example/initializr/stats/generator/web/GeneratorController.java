package com.example.initializr.stats.generator.web;

import java.time.LocalDate;

import com.example.initializr.stats.generator.DateRange;
import com.example.initializr.stats.generator.GenerationStatistics;
import com.example.initializr.stats.generator.Generator;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GeneratorController {

	private final Generator generator;

	public GeneratorController(Generator generator) {
		this.generator = generator;
	}

	@GetMapping("/events/{from}/{to}")
	public EventsDescriptor events(@PathVariable LocalDate from,
			@PathVariable LocalDate to) {
		DateRange range = new DateRange(from, to);
		return new EventsDescriptor(this.generator.getEvents(range));
	}

	@GetMapping("/statistics/{from}/{to}")
	public GenerationStatistics statistics(@PathVariable LocalDate from,
			@PathVariable LocalDate to) {
		DateRange range = new DateRange(from, to);
		return this.generator.generateStatistics(range);
	}

}

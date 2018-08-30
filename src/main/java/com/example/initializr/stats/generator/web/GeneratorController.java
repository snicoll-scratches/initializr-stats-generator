package com.example.initializr.stats.generator.web;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Random;

import com.example.initializr.stats.generator.DateRange;
import com.example.initializr.stats.generator.GenerationStatistics;
import com.example.initializr.stats.generator.Generator;
import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GeneratorController {

	private final Generator generator;

	public GeneratorController(Generator generator) {
		this.generator = generator;
	}

	@GetMapping("/statistics/{from}/{to}")
	public GenerationStatistics statistics(@PathVariable LocalDate from,
			@PathVariable LocalDate to) {
		DateRange range = new DateRange(from, to);
		return this.generator.generateStatistics(range);
	}

	@GetMapping("/events/{from}/{to}")
	public EventsDescriptor events(@PathVariable LocalDate from,
			@PathVariable LocalDate to) {
		DateRange range = new DateRange(from, to);
		return new EventsDescriptor(this.generator.getEvents(range));
	}

	@GetMapping("/top-ips/{from}/{to}")
	public TopIpsDescriptor topIps(@PathVariable LocalDate from,
			@PathVariable LocalDate to) {
		DateRange range = new DateRange(from, to);
		return new TopIpsDescriptor(this.generator.getTopIps(range));
	}

	@GetMapping("/reverse-lookup/costly/{ip}")
	public ReverseLookupDescriptor costlyReverseLookup(@PathVariable String ip) {
		return new ReverseLookupDescriptor(ip, ip + ".example.com");
	}

	@GetMapping("/reverse-lookup/free/{ip}")
	public Mono<ReverseLookupDescriptor> freeReverseLookup(@PathVariable String ip) {
		return Mono.just(costlyReverseLookup(ip))
				.delayElement(this.generator.getLatency().randomLatency());
	}

}

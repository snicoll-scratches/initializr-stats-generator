package com.example.initializr.stats.generator.web;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import com.example.initializr.stats.generator.DateRange;
import com.example.initializr.stats.generator.Event;
import com.example.initializr.stats.generator.GenerationStatistics;
import com.example.initializr.stats.generator.GenerationStatisticsItem;
import com.example.initializr.stats.generator.Generator;
import com.example.initializr.stats.generator.GeneratorClient;
import io.github.bucket4j.Bucket;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GeneratorController {

	private final Generator generator;

	private final Flux<GenerationStatisticsItem> liveStatistics;

	public GeneratorController(Generator generator) {
		this.generator = generator;
		this.liveStatistics = generator.generateLiveStatistics(Duration.ofSeconds(10));
	}

	@GetMapping("/statistics/{from}/{to}")
	public GenerationStatistics statistics(@PathVariable LocalDate from,
			@PathVariable LocalDate to) {
		DateRange range = new DateRange(from, to);
		return this.generator.generateStatistics(range);
	}

	@GetMapping(path = "/live-statistics", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Flux<GenerationStatisticsItem> liveStatistics() {
		return this.liveStatistics;
	}

	@GetMapping("/events/{from}/{to}")
	public List<Event> events(@PathVariable LocalDate from,
			@PathVariable LocalDate to) {
		DateRange range = new DateRange(from, to);
		return this.generator.getEvents(range);
	}

	@GetMapping("/top-ips/{from}/{to}")
	public List<GeneratorClient> topIps(@PathVariable LocalDate from,
			@PathVariable LocalDate to) {
		DateRange range = new DateRange(from, to);
		return this.generator.getTopIps(range);
	}

	@GetMapping("/reverse-lookup/costly/{ip}")
	public ReverseLookupDescriptor costlyReverseLookup(@PathVariable String ip) {
		return new ReverseLookupDescriptor(ip, ip + ".example.com");
	}

	@GetMapping("/reverse-lookup/free/{ip}")
	public ResponseEntity<Mono<ReverseLookupDescriptor>> freeReverseLookup(
			@RequestAttribute("RateLimiterBucket") Bucket bucket,
			@PathVariable String ip) {

		if (bucket.tryConsume(1)) {
			return ResponseEntity.ok()
					.header("X-RateLimit-Remaining", String.valueOf(bucket.getAvailableTokens()))
					.body(Mono.just(costlyReverseLookup(ip))
							.delayElement(this.generator.getLatency().randomLatency()));
		}
		return ResponseEntity.status(HttpStatus.FORBIDDEN)
				.header("X-RateLimit-Remaining", "0")
				.build();
	}

}

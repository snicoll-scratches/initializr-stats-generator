package com.example.initializr.stats.generator.web;

import java.time.Duration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;

import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class RateLimiterHandlerInterceptor extends HandlerInterceptorAdapter {

	public static String SESSION_BUCKET_ATTRIBUTE = "RateLimiterBucket";

	private static final ConcurrentMapCache buckets = new ConcurrentMapCache("buckets");

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		String remoteAddr = request.getRemoteAddr();
		Bucket bucket = buckets.get(remoteAddr, Bucket.class);
		if (bucket == null) {
			buckets.put(remoteAddr, createBucket());
		}
		request.setAttribute(SESSION_BUCKET_ATTRIBUTE, bucket);
		return true;
	}

	private Bucket createBucket() {
		Bandwidth limit = Bandwidth.simple(10, Duration.ofMinutes(1));
		return Bucket4j.builder().addLimit(limit).build();
	}
}

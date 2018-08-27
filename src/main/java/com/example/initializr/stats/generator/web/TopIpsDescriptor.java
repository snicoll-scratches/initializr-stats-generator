package com.example.initializr.stats.generator.web;

import java.util.List;

/**
 *
 * @author Stephane Nicoll
 */
class TopIpsDescriptor {

	private final List<String> ips;

	TopIpsDescriptor(List<String> ips) {
		this.ips = ips;
	}

	public List<String> getIps() {
		return this.ips;
	}

}

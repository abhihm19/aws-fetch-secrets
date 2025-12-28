package com.silly.fetch_secrets.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import com.silly.fetch_secrets.config.JwtProperties;

@Component
public class JwtService implements ApplicationRunner {

	private static final Logger log = LoggerFactory.getLogger(JwtService.class);

	@Autowired
	private JwtProperties jwtProperties;

	@Value("${poc.print-secrets:false}")
	private boolean printSecrets;

	@Override
	public void run(ApplicationArguments args) {
		// POC verification: print values exactly as loaded when enabled.
		if (printSecrets) {
			log.warn("POC ONLY: printing key material to console (poc.print-secrets=true).");
			System.out.println("JWT Private Key = " + jwtProperties.privateKey());
			System.out.println("JWT Public Key  = " + jwtProperties.publicKey());
			return;
		}

		// Default: keep logs clean.
		log.info("JWT keys loaded (poc.print-secrets=false).");
	}

}

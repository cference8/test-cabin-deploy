package com.cf.cabinescaperoom;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import com.ulisesbocchio.jasyptspringboot.environment.StandardEncryptableEnvironment;	
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
@EnableEncryptableProperties
public class CabinEscapeRoomApplication {

	public static void main(String[] args) {
		System.setProperty("jasypt.encryptor.password", "cafe21");
		System.setProperty("jasypt.encryptor.proxyPropertySources", "true");
		new SpringApplicationBuilder()
				.environment(new StandardEncryptableEnvironment())
				.sources(CabinEscapeRoomApplication.class)
				.run(args);
	}

}

package com.carrental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.carrental.utils.FileUploadProperties;

@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties({
    FileUploadProperties.class
})
public class CarRentalBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarRentalBackendApplication.class, args);
	}

}

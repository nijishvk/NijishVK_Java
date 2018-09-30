package com.ubs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ubs.service.EodService;

@SpringBootApplication
public class PositionCalculationProcess implements CommandLineRunner {

	@Autowired
	EodService eodService;	
	
	public static void main(String args[]) {
		SpringApplication app = new SpringApplication(PositionCalculationProcess.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);
	}

	public void run(String... arg0) throws Exception {
		eodService.process();
	}

}

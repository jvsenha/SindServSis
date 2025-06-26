package br.com.sindservferpa.SindServ;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("br.com.sindservferpa.SindServ")
public class SindServApplication {

	public static void main(String[] args) {
		SpringApplication.run(SindServApplication.class, args);
	}

}

package online.thinhtran.psyconnect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PsyConnectApplication {

	public static void main(String[] args) {
		SpringApplication.run(PsyConnectApplication.class, args);
	}

}

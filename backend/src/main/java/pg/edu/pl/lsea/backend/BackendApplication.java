package pg.edu.pl.lsea.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pg.edu.pl.lsea.backend.udp.UdpServer;

/**
 * BackendApplication - running the backend of the analyzer.
 * tests are available in test.java.pg.edu.pl.lsea.backend.BackendApplicationTests
 */
@SpringBootApplication
public class BackendApplication {
	public static void main(String[] args) {

		SpringApplication.run(BackendApplication.class, args);
	}
}

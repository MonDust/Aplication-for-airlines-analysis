package pg.edu.pl.lsea.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pg.edu.pl.lsea.backend.entities.analysisresult.Operator;
import pg.edu.pl.lsea.backend.udp.UdpServer;

@SpringBootApplication
public class BackendApplication {
	public static void main(String[] args) {

		SpringApplication.run(BackendApplication.class, args);



	}
}

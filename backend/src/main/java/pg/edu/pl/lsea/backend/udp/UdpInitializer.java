package pg.edu.pl.lsea.backend.udp;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

@Component
public class UdpInitializer {

    @PostConstruct
    public void startUdpServer() {
        System.out.println("Starting UDP server...");
        UdpServer.receiveClientAddress();
    }

    @PreDestroy
    public void stopUdpServer() {
        System.out.println("Stopping UDP server...");
        UdpServer.closeServer();
    }
}

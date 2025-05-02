package pg.edu.pl.lsea.backend.udp;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

/**
 * Class initializing Udp: post construct starts Udp Server, and pre destroy stops it.
 */
@Component
public class UdpInitializer {

    /**
     * Post construct start Udp server.
     */
    @PostConstruct
    public void startUdpServer() {
        System.out.println("Starting UDP server...");
        UdpServer.receiveClientAddress();
    }

    /**
     * Pre destroy stop Udp server.
     */
    @PreDestroy
    public void stopUdpServer() {
        System.out.println("Stopping UDP server...");
        UdpServer.closeServer();
    }
}

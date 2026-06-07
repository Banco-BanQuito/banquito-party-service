package ec.edu.espe.banquito.party.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class PartyGrpcServer {

    private final int port;
    private final PartyGrpcService partyGrpcService;
    private Server server;

    public PartyGrpcServer(
            @Value("${party.grpc.port:9093}") int port,
            PartyGrpcService partyGrpcService) {
        this.port = port;
        this.partyGrpcService = partyGrpcService;
    }

    @PostConstruct
    public void start() throws IOException {
        this.server = ServerBuilder
                .forPort(this.port)
                .addService(this.partyGrpcService.bindService())
                .build()
                .start();

        System.out.println("Party gRPC server started on port " + this.port);
    }

    @PreDestroy
    public void stop() {
        if (this.server != null) {
            this.server.shutdown();
        }
    }
}
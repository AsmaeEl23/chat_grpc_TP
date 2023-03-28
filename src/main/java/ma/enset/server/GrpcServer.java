package ma.enset.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import ma.enset.services.ChatServerImp;

public class GrpcServer {
    public static void main(String[] args) throws Exception{
        Server server = ServerBuilder.forPort(5555)
                .addService(new ChatServerImp())
                .build();
        server.start();
        server.awaitTermination();
    }
}

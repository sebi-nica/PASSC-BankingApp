package grpc.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;


public class GrpcServerMain {
    public static void main(String[] args) {
        try {
            int port = 50051;

            // start the server
            Server server = ServerBuilder.forPort(port)
                    .addService(new GrpcBankServiceImpl())
                    .build()
                    .start();

            System.out.println("gRPC Bank Server started, listening on " + port);

            // keep the thread alive
            server.awaitTermination();

        } catch (Exception e) {
            System.err.println("gRPC Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
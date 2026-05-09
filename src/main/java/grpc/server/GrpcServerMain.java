package grpc.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

/**
 * Bootstraps the gRPC Server on a specific port.
 */
public class GrpcServerMain {
    public static void main(String[] args) {
        try {
            int port = 50051;

            // Build and start the gRPC server
            Server server = ServerBuilder.forPort(port)
                    .addService(new GrpcBankServiceImpl())
                    .build()
                    .start();

            System.out.println("gRPC Bank Server started, listening on " + port);

            // Keep the main thread alive so the server doesn't shut down immediately
            server.awaitTermination();

        } catch (Exception e) {
            System.err.println("gRPC Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
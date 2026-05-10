package grpc.client;

import grpc.generated.AmountRequest;
import grpc.generated.AccountRequest;
import grpc.generated.BalanceResponse;
import grpc.generated.TransferRequest;
import grpc.generated.BankServiceGrpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class GrpcClientMain {
    public static void main(String[] args) {
        // create a channel to the server
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        try {
            // create the remote proxy
            BankServiceGrpc.BankServiceBlockingStub bankProxy = BankServiceGrpc.newBlockingStub(channel);

            System.out.println("--- Connected to gRPC Bank Server ---");

            // 3. Execute banking operations.

            // Add funds
            BalanceResponse res1 = bankProxy.addAmount(AmountRequest.newBuilder()
                    .setAccountId("ACC1")
                    .setAmount(500.0)
                    .build());
            System.out.println("Added funds. State: ACC1=" + res1.getBalance());

            // Withdraw funds
            BalanceResponse res2 = bankProxy.withdrawAmount(AmountRequest.newBuilder()
                    .setAccountId("ACC1")
                    .setAmount(150.0)
                    .build());
            System.out.println("Withdrew funds. State: ACC1=" + res2.getBalance());

            // Transfer funds
            BalanceResponse res3 = bankProxy.transferAmount(TransferRequest.newBuilder()
                    .setFromAccountId("ACC1")
                    .setToAccountId("ACC2")
                    .setAmount(200.0)
                    .build());
            System.out.println("Transferred to ACC2. ACC1 State: ACC1=" + res3.getBalance());

            // Query balance
            BalanceResponse res4 = bankProxy.queryBalance(AccountRequest.newBuilder()
                    .setAccountId("ACC2")
                    .build());
            System.out.println("Queried ACC2. State: ACC2=" + res4.getBalance());

            // should trigger error
            bankProxy.withdrawAmount(AmountRequest.newBuilder()
                    .setAccountId("ACC2")
                    .setAmount(9999.0)
                    .build());

        } catch (StatusRuntimeException e) {
            System.err.println("RPC failed: " + e.getStatus().getDescription());
        } finally {
            // shut down the channel no matter what happened
            System.out.println("\nShutting down channel...");
            channel.shutdown();
        }
    }
}
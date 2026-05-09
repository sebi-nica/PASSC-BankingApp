package grpc.server;

// Import the generated message classes directly
import grpc.generated.AmountRequest;
import grpc.generated.AccountRequest;
import grpc.generated.BalanceResponse;
import grpc.generated.TransferRequest;
import grpc.generated.BankServiceGrpc;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The gRPC Server Implementation.
 * Extends the auto-generated base class. Notice how gRPC uses asynchronous
 * StreamObservers to send responses back to the client.
 */
public class GrpcBankServiceImpl extends BankServiceGrpc.BankServiceImplBase {

    // Thread-safe in-memory database
    private final ConcurrentHashMap<String, Double> accounts = new ConcurrentHashMap<>();

    @Override
    public void addAmount(AmountRequest request, StreamObserver<BalanceResponse> responseObserver) {
        String accountId = request.getAccountId();
        double newBalance = accounts.merge(accountId, request.getAmount(), Double::sum);

        // Build the auto-generated response object
        BalanceResponse response = BalanceResponse.newBuilder()
                .setAccountId(accountId)
                .setBalance(newBalance)
                .build();

        // Send the response and close the stream
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void withdrawAmount(AmountRequest request, StreamObserver<BalanceResponse> responseObserver) {
        String accountId = request.getAccountId();
        double amount = request.getAmount();
        double currentBalance = accounts.getOrDefault(accountId, 0.0);

        if (currentBalance < amount) {
            // gRPC way of throwing exceptions across the network
            responseObserver.onError(Status.FAILED_PRECONDITION
                    .withDescription("Insufficient funds for account: " + accountId)
                    .asRuntimeException());
            return;
        }

        accounts.put(accountId, currentBalance - amount);

        BalanceResponse response = BalanceResponse.newBuilder()
                .setAccountId(accountId)
                .setBalance(accounts.get(accountId))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void transferAmount(TransferRequest request, StreamObserver<BalanceResponse> responseObserver) {
        String fromAcc = request.getFromAccountId();
        String toAcc = request.getToAccountId();
        double amount = request.getAmount();

        double fromBalance = accounts.getOrDefault(fromAcc, 0.0);

        if (fromBalance < amount) {
            responseObserver.onError(Status.FAILED_PRECONDITION
                    .withDescription("Insufficient funds for transfer")
                    .asRuntimeException());
            return;
        }

        accounts.put(fromAcc, fromBalance - amount);
        accounts.merge(toAcc, amount, Double::sum);

        BalanceResponse response = BalanceResponse.newBuilder()
                .setAccountId(fromAcc)
                .setBalance(accounts.get(fromAcc))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void queryBalance(AccountRequest request, StreamObserver<BalanceResponse> responseObserver) {
        String accountId = request.getAccountId();
        double balance = accounts.getOrDefault(accountId, 0.0);

        BalanceResponse response = BalanceResponse.newBuilder()
                .setAccountId(accountId)
                .setBalance(balance)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
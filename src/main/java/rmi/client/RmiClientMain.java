package rmi.client;

import rmi.shared.AccountState;
import rmi.shared.IBankService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * The RMI Client.
 * Demonstrates the Remote Proxy Pattern by interacting with the Server
 * entirely through a local stub interface.
 */
public class RmiClientMain {
    public static void main(String[] args) {
        try {
            // 1. Connect to the local RMI registry on port 1099
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);

            // 2. Lookup the remote proxy (Stub) using the registered name
            IBankService bankProxy = (IBankService) registry.lookup("BankingService");

            System.out.println("--- Connected to RMI Bank Server ---");

            // 3. Execute banking operations.
            // Notice how network complexity is 100% hidden from the developer.

            // Add funds
            AccountState acc1 = bankProxy.addAmount("ACC1", 500.0);
            System.out.println("Added funds. State: " + acc1);

            // Withdraw funds
            AccountState acc2 = bankProxy.withdrawAmount("ACC1", 150.0);
            System.out.println("Withdrew funds. State: " + acc2);

            // Transfer funds to a new account
            AccountState acc3 = bankProxy.transferAmount("ACC1", "ACC2", 200.0);
            System.out.println("Transferred to ACC2. ACC1 State: " + acc3);

            // Query the balance of the second account
            AccountState acc4 = bankProxy.queryBalance("ACC2");
            System.out.println("Queried ACC2. State: " + acc4);

        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
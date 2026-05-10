package rmi.client;

import rmi.shared.AccountState;
import rmi.shared.IBankService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class RmiClientMain {
    public static void main(String[] args) {
        try {
            // connect to local RMI registry
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);

            IBankService bankProxy = (IBankService) registry.lookup("BankingService");

            System.out.println("--- Connected to RMI Bank Server ---");

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
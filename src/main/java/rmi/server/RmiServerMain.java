package rmi.server;

import rmi.shared.IBankService;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Bootstraps the RMI Server and registers the remote object.
 */
public class RmiServerMain {
    public static void main(String[] args) {
        try {
            // Instantiate the renamed service
            IBankService bankService = new RmiBankServiceImpl();

            // Create the RMI registry locally on the default port 1099
            Registry registry = LocateRegistry.createRegistry(1099);

            // Bind the remote object stub in the registry
            registry.rebind("BankingService", bankService);

            System.out.println("RMI Bank Server is running...");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
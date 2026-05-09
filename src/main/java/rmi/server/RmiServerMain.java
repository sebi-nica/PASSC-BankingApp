package rmi.server;

import rmi.shared.IBankService;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RmiServerMain {
    public static void main(String[] args) {
        try {
            IBankService bankService = new RmiBankServiceImpl();

            Registry registry = LocateRegistry.createRegistry(1099);

            registry.rebind("BankingService", bankService);

            System.out.println("RMI Bank Server is running...");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
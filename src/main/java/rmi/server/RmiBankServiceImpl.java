package rmi.server;

import rmi.shared.AccountState;
import rmi.shared.IBankService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The actual server-side implementation of the banking logic.
 * Extends UnicastRemoteObject to automatically export it to the RMI runtime.
 */
public class RmiBankServiceImpl extends UnicastRemoteObject implements IBankService {

    // Thread-safe in-memory database simulating a real datastore
    private final ConcurrentHashMap<String, Double> accounts;

    public RmiBankServiceImpl() throws RemoteException {
        super();
        this.accounts = new ConcurrentHashMap<>();
    }

    @Override
    public AccountState addAmount(String accountId, double amount) throws RemoteException {
        accounts.merge(accountId, amount, Double::sum);
        return new AccountState(accountId, accounts.get(accountId));
    }

    @Override
    public AccountState withdrawAmount(String accountId, double amount) throws RemoteException {
        double currentBalance = accounts.getOrDefault(accountId, 0.0);
        if (currentBalance < amount) {
            throw new RemoteException("Insufficient funds for account: " + accountId);
        }
        accounts.put(accountId, currentBalance - amount);
        return new AccountState(accountId, accounts.get(accountId));
    }

    @Override
    public AccountState transferAmount(String fromAccountId, String toAccountId, double amount) throws RemoteException {
        // Reuses existing methods to ensure consistent state
        withdrawAmount(fromAccountId, amount);
        addAmount(toAccountId, amount);
        return new AccountState(fromAccountId, accounts.get(fromAccountId));
    }

    @Override
    public AccountState queryBalance(String accountId) throws RemoteException {
        return new AccountState(accountId, accounts.getOrDefault(accountId, 0.0));
    }
}
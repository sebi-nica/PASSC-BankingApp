package rmi.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface IBankService extends Remote {

    AccountState addAmount(String accountId, double amount) throws RemoteException;

    AccountState withdrawAmount(String accountId, double amount) throws RemoteException;

    AccountState transferAmount(String fromAccountId, String toAccountId, double amount) throws RemoteException;

    AccountState queryBalance(String accountId) throws RemoteException;
}
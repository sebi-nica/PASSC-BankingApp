package rmi.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The RMI Remote Interface.
 * This defines the contract between the RMI Client and RMI Server.
 * Must extend java.rmi.Remote and all methods must throw RemoteException.
 */
public interface IBankService extends Remote {

    AccountState addAmount(String accountId, double amount) throws RemoteException;

    AccountState withdrawAmount(String accountId, double amount) throws RemoteException;

    AccountState transferAmount(String fromAccountId, String toAccountId, double amount) throws RemoteException;

    AccountState queryBalance(String accountId) throws RemoteException;
}
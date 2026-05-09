package rmi.shared;

import java.io.Serializable;

public record AccountState(String accountId, double balance) implements Serializable {}
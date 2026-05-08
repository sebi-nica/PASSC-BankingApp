package rmi.shared;

import java.io.Serializable;

/**
 * Immutable Data Transfer Object (DTO) for RMI.
 * Must implement Serializable to be converted to a byte stream across the network.
 */
public record AccountState(String accountId, double balance) implements Serializable {}
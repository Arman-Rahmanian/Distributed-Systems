import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Remote interface for the Calculator service.
 * Defines the methods that can be invoked remotely by clients.
 */
public interface Calculator extends Remote {
    /**
     * Pushes a value onto the stack of the specified client.
     * @param clientId The unique identifier for the client.
     * @param val The integer value to push onto the client's stack.
     * @throws RemoteException if an RMI-related error occurs.
     */
    void pushValue(String clientId, int val) throws RemoteException;

    /**
     * Pushes an operation onto the stack of the specified client.
     * The operation is applied to all values currently on the client's stack.
     * @param clientId The unique identifier for the client.
     * @param operator The operation to apply ("min", "max", "lcm", "gcd").
     * @throws RemoteException if an RMI-related error occurs or if the stack is empty.
     * @throws IllegalArgumentException if an invalid operator is provided.
     */
    void pushOperation(String clientId, String operator) throws RemoteException;

    /**
     * Pops the top value from the stack of the specified client and returns it.
     * @param clientId The unique identifier for the client.
     * @return The integer value popped from the client's stack.
     * @throws RemoteException if an RMI-related error occurs or if the stack is empty.
     */
    int pop(String clientId) throws RemoteException;

    /**
     * Checks if the stack of the specified client is empty.
     * @param clientId The unique identifier for the client.
     * @return True if the client's stack is empty, false otherwise.
     * @throws RemoteException if an RMI-related error occurs.
     */
    boolean isEmpty(String clientId) throws RemoteException;

    /**
     * Pops the top value from the stack of the specified client after a delay.
     * @param clientId The unique identifier for the client.
     * @param millis The delay in milliseconds before the pop operation is performed.
     * @return The integer value popped from the client's stack after the delay.
     * @throws RemoteException if an RMI-related error occurs or if the stack is empty.
     */
    int delayPop(String clientId, int millis) throws RemoteException;
}

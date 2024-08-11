import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Stack;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Implementation of the Calculator interface.
 * This class provides the logic for a distributed stack-based calculator.
 * Each client is identified by a unique clientId, and has its own stack on the server.
 */
public class CalculatorImplementation extends UnicastRemoteObject implements Calculator {
    // A HashMap to store a stack for each client identified by their unique clientId.
    private HashMap<String, Stack<Integer>> clientStacks;

    /**
     * Constructor for CalculatorImplementation.
     * Initializes the clientStacks HashMap.
     * @throws RemoteException if an RMI-related error occurs.
     */
    protected CalculatorImplementation() throws RemoteException {
        clientStacks = new HashMap<>();
    }

    /**
     * Retrieves the stack associated with a specific client.
     * If the client does not have a stack yet, a new one is created.
     * @param clientId The unique identifier for the client.
     * @return The stack associated with the client.
     */
    private Stack<Integer> getClientStack(String clientId) {
        return clientStacks.computeIfAbsent(clientId, k -> new Stack<>());
    }

    /**
     * Pushes a value onto the stack of the specified client.
     * @param clientId The unique identifier for the client.
     * @param val The integer value to push onto the client's stack.
     * @throws RemoteException if an RMI-related error occurs.
     */
    @Override
    public synchronized void pushValue(String clientId, int val) throws RemoteException {
        Stack<Integer> stack = getClientStack(clientId);
        stack.push(val);
    }

    /**
     * Pushes an operation onto the stack of the specified client.
     * The operation is applied to all values currently on the client's stack.
     * Valid operations are "min", "max", "lcm", and "gcd".
     * After applying the operation, the result is pushed back onto the stack.
     * @param clientId The unique identifier for the client.
     * @param operator The operation to apply ("min", "max", "lcm", "gcd").
     * @throws RemoteException if an RMI-related error occurs or if the stack is empty.
     * @throws IllegalArgumentException if an invalid operator is provided.
     */
    @Override
    public synchronized void pushOperation(String clientId, String operator) throws RemoteException {
        Stack<Integer> stack = getClientStack(clientId);
        if (stack.isEmpty()) {
            throw new RemoteException("Stack is empty. Cannot perform operation.");
        }

        // Pop all values and perform the specified operation
        ArrayList<Integer> values = new ArrayList<>();
        while (!stack.isEmpty()) {
            values.add(stack.pop());
        }

        int result = 0;
        switch (operator) {
            case "min":
                result = values.stream().min(Integer::compare).get();
                break;
            case "max":
                result = values.stream().max(Integer::compare).get();
                break;
            case "lcm":
                result = lcm(values);
                break;
            case "gcd":
                result = gcd(values);
                break;
            default:
                throw new IllegalArgumentException("Invalid operator.");
        }

        stack.push(result);
    }

    /**
     * Pops the top value from the stack of the specified client and returns it.
     * @param clientId The unique identifier for the client.
     * @return The integer value popped from the client's stack.
     * @throws RemoteException if an RMI-related error occurs or if the stack is empty.
     */
    @Override
    public synchronized int pop(String clientId) throws RemoteException {
        Stack<Integer> stack = getClientStack(clientId);
        if (stack.isEmpty()) {
            throw new RemoteException("Stack is empty. Cannot pop.");
        }
        return stack.pop();
    }

    /**
     * Checks if the stack of the specified client is empty.
     * @param clientId The unique identifier for the client.
     * @return True if the client's stack is empty, false otherwise.
     * @throws RemoteException if an RMI-related error occurs.
     */
    @Override
    public synchronized boolean isEmpty(String clientId) throws RemoteException {
        Stack<Integer> stack = getClientStack(clientId);
        return stack.isEmpty();
    }

    /**
     * Pops the top value from the stack of the specified client after a delay.
     * This simulates a delayed response in a distributed system.
     * @param clientId The unique identifier for the client.
     * @param millis The delay in milliseconds before the pop operation is performed.
     * @return The integer value popped from the client's stack after the delay.
     * @throws RemoteException if an RMI-related error occurs or if the stack is empty.
     */
    @Override
    public synchronized int delayPop(String clientId, int millis) throws RemoteException {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted state
            throw new RemoteException("Thread interrupted during delayPop.");
        }
        return pop(clientId);
    }

    /**
     * Calculates the greatest common divisor (GCD) of a list of values.
     * @param values A list of integer values.
     * @return The GCD of the list of values.
     */
    private int gcd(ArrayList<Integer> values) {
        int result = values.get(0);
        for (int i = 1; i < values.size(); i++) {
            result = gcd(result, values.get(i));
        }
        return result;
    }

    /**
     * Calculates the GCD of two integers using the Euclidean algorithm.
     * @param a The first integer.
     * @param b The second integer.
     * @return The GCD of the two integers.
     */
    private int gcd(int a, int b) {
        if (b == 0) return a;
        return gcd(b, a % b);
    }

    /**
     * Calculates the least common multiple (LCM) of a list of values.
     * @param values A list of integer values.
     * @return The LCM of the list of values.
     */
    private int lcm(ArrayList<Integer> values) {
        int result = values.get(0);
        for (int i = 1; i < values.size(); i++) {
            result = lcm(result, values.get(i));
        }
        return result;
    }

    /**
     * Calculates the LCM of two integers.
     * @param a The first integer.
     * @param b The second integer.
     * @return The LCM of the two integers.
     */
    private int lcm(int a, int b) {
        return (a * b) / gcd(a, b);
    }
}

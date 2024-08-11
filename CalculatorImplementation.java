import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * Implementation of the Calculator RMI service.
 * This class manages stacks for each client and performs operations on these stacks.
 */
public class CalculatorImplementation extends UnicastRemoteObject implements Calculator {

    private static final long serialVersionUID = 1L;

    // Map to hold stacks for each client
    private final Map<String, LinkedList<Integer>> clientStacks;

    /**
     * Constructs a CalculatorImplementation instance.
     * Initializes the map to hold client stacks.
     * @throws RemoteException if an RMI-related error occurs.
     */
    protected CalculatorImplementation() throws RemoteException {
        clientStacks = new HashMap<>();
    }

    /**
     * Pushes a value onto the stack of the specified client.
     * If the client's stack does not exist, it is created.
     * This method is synchronized to ensure thread safety.
     * @param clientId The ID of the client whose stack is to be modified.
     * @param val The value to be pushed onto the stack.
     * @throws RemoteException if an RMI-related error occurs.
     */
    @Override
    public synchronized void pushValue(String clientId, int val) throws RemoteException {
        // Ensure the client's stack exists
        clientStacks.computeIfAbsent(clientId, k -> new LinkedList<>()).push(val);
        System.out.println("pushValue: Client " + clientId + " pushed " + val);
    }

    /**
     * Performs an operation (min, max, lcm, gcd) on the values in the client's stack and pushes the result.
     * The stack is emptied to perform the operation and then the result is pushed back onto the stack.
     * This method is synchronized to ensure thread safety.
     * @param clientId The ID of the client whose stack is to be modified.
     * @param operator The operation to be performed ("min", "max", "lcm", "gcd").
     * @throws RemoteException if the stack is empty or does not exist, or if the operator is invalid.
     */
    @Override
    public synchronized void pushOperation(String clientId, String operator) throws RemoteException {
        LinkedList<Integer> stack = clientStacks.get(clientId);

        if (stack == null || stack.isEmpty()) {
            throw new RemoteException("Stack is empty or does not exist for client: " + clientId);
        }

        Queue<Integer> values = new LinkedList<>();
        while (!stack.isEmpty()) {
            values.add(stack.pop());
        }

        int result;
        switch (operator) {
            case "min":
                result = min(values);
                break;
            case "max":
                result = max(values);
                break;
            case "lcm":
                result = lcm(values);
                break;
            case "gcd":
                result = gcd(values);
                break;
            default:
                throw new RemoteException("Invalid operator: " + operator);
        }

        stack.push(result);
        System.out.println("pushOperation: Client " + clientId + " performed " + operator + ", result pushed: " + result);
    }

    /**
     * Pops a value from the stack of the specified client.
     * This method is synchronized to ensure thread safety.
     * @param clientId The ID of the client whose stack is to be modified.
     * @return The value popped from the stack.
     * @throws RemoteException if the stack is empty or does not exist.
     */
    @Override
    public synchronized int pop(String clientId) throws RemoteException {
        LinkedList<Integer> stack = clientStacks.get(clientId);
        if (stack == null || stack.isEmpty()) {
            throw new RemoteException("Stack is empty or does not exist for client: " + clientId);
        }
        int value = stack.pop();
        System.out.println("pop: Client " + clientId + " popped " + value);
        return value;
    }

    /**
     * Checks if the stack for the specified client is empty.
     * This method is synchronized to ensure thread safety.
     * @param clientId The ID of the client whose stack is to be checked.
     * @return True if the stack is empty or does not exist; false otherwise.
     * @throws RemoteException if an RMI-related error occurs.
     */
    @Override
    public synchronized boolean isEmpty(String clientId) throws RemoteException {
        LinkedList<Integer> stack = clientStacks.get(clientId);
        return stack == null || stack.isEmpty();
    }

    /**
     * Performs a delayed pop operation on the stack of the specified client.
     * The thread is paused for the specified amount of time before performing the pop.
     * This method is synchronized to ensure thread safety.
     * @param clientId The ID of the client whose stack is to be modified.
     * @param millis The amount of time in milliseconds to delay before performing the pop.
     * @return The value popped from the stack after the delay.
     * @throws RemoteException if the stack is empty or does not exist, or if the thread is interrupted during sleep.
     */
    @Override
    public synchronized int delayPop(String clientId, int millis) throws RemoteException {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RemoteException("Thread was interrupted during sleep", e);
        }
        int value = pop(clientId);
        System.out.println("delayPop: Client " + clientId + " delayed pop, result: " + value);
        return value;
    }

    // Utility methods to perform operations on values

    /**
     * Computes the minimum value from a queue of integers.
     * @param values A queue containing the integers.
     * @return The minimum value from the queue.
     * @throws NoSuchElementException if the queue is empty.
     */
    private int min(Queue<Integer> values) {
        return values.stream().min(Integer::compare).orElseThrow();
    }

    /**
     * Computes the maximum value from a queue of integers.
     * @param values A queue containing the integers.
     * @return The maximum value from the queue.
     * @throws NoSuchElementException if the queue is empty.
     */
    private int max(Queue<Integer> values) {
        return values.stream().max(Integer::compare).orElseThrow();
    }

    /**
     * Computes the least common multiple (LCM) of the integers in a queue.
     * @param values A queue containing the integers.
     * @return The LCM of the integers in the queue.
     */
    private int lcm(Queue<Integer> values) {
        // Implement LCM calculation
        return values.stream().reduce(1, (a, b) -> (a * b) / gcd(a, b));
    }

    /**
     * Computes the greatest common divisor (GCD) of the integers in a queue.
     * @param values A queue containing the integers.
     * @return The GCD of the integers in the queue.
     */
    private int gcd(Queue<Integer> values) {
        // Implement GCD calculation
        return values.stream().reduce(values.poll(), this::gcd);
    }

    /**
     * Computes the greatest common divisor (GCD) of two integers.
     * @param a The first integer.
     * @param b The second integer.
     * @return The GCD of the two integers.
     */
    private int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
}

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Stack;
import java.util.ArrayList;  // <-- Add this import statement
import java.util.UUID;

public class CalculatorImplementation extends UnicastRemoteObject implements Calculator {
    private HashMap<String, Stack<Integer>> clientStacks;

    protected CalculatorImplementation() throws RemoteException {
        clientStacks = new HashMap<>();
    }

    private Stack<Integer> getClientStack(String clientId) {
        return clientStacks.computeIfAbsent(clientId, k -> new Stack<>());
    }

    @Override
    public synchronized void pushValue(String clientId, int val) throws RemoteException {
        Stack<Integer> stack = getClientStack(clientId);
        stack.push(val);
    }

    @Override
    public synchronized void pushOperation(String clientId, String operator) throws RemoteException {
        Stack<Integer> stack = getClientStack(clientId);
        if (stack.isEmpty()) {
            throw new RemoteException("Stack is empty. Cannot perform operation.");
        }

        // Pop all values and perform operation
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
                throw new RemoteException("Invalid operator.");
        }

        stack.push(result);
    }

    @Override
    public synchronized int pop(String clientId) throws RemoteException {
        Stack<Integer> stack = getClientStack(clientId);
        if (stack.isEmpty()) {
            throw new RemoteException("Stack is empty. Cannot pop.");
        }
        return stack.pop();
    }

    @Override
    public synchronized boolean isEmpty(String clientId) throws RemoteException {
        Stack<Integer> stack = getClientStack(clientId);
        return stack.isEmpty();
    }

    @Override
    public synchronized int delayPop(String clientId, int millis) throws RemoteException {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RemoteException("Thread interrupted during delayPop.");
        }
        return pop(clientId);
    }

    private int gcd(ArrayList<Integer> values) {
        int result = values.get(0);
        for (int i = 1; i < values.size(); i++) {
            result = gcd(result, values.get(i));
        }
        return result;
    }

    private int gcd(int a, int b) {
        if (b == 0) return a;
        return gcd(b, a % b);
    }

    private int lcm(ArrayList<Integer> values) {
        int result = values.get(0);
        for (int i = 1; i < values.size(); i++) {
            result = lcm(result, values.get(i));
        }
        return result;
    }

    private int lcm(int a, int b) {
        return (a * b) / gcd(a, b);
    }
}

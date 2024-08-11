import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Stack;
import java.util.ArrayList;

public class CalculatorImplementation extends UnicastRemoteObject implements Calculator {
    private Stack<Integer> stack;

    protected CalculatorImplementation() throws RemoteException {
        stack = new Stack<>();
    }

    @Override
    public synchronized void pushValue(int val) throws RemoteException {
        stack.push(val);
    }

    @Override
    public synchronized void pushOperation(String operator) throws RemoteException {
        if (stack.isEmpty()) {
            throw new RemoteException("Stack is empty. Cannot perform operation.");
        }
        
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
    public synchronized int pop() throws RemoteException {
        if (stack.isEmpty()) {
            throw new RemoteException("Stack is empty. Cannot pop.");
        }
        return stack.pop();
    }

    @Override
    public synchronized boolean isEmpty() throws RemoteException {
        return stack.isEmpty();
    }

    @Override
    public synchronized int delayPop(int millis) throws RemoteException {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RemoteException("Thread interrupted during delayPop.");
        }
        return pop();
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
import java.rmi.Naming;

public class CalculatorClient {
    public static void main(String[] args) {
        try {
            Calculator calculator = (Calculator) Naming.lookup("rmi://localhost:1099/CalculatorService");

            // Test operations with one client
            calculator.pushValue(10);
            calculator.pushValue(20);
            calculator.pushOperation("max");
            System.out.println("Max: " + calculator.pop());

            // Test delayPop
            calculator.pushValue(30);
            System.out.println("Delayed pop: " + calculator.delayPop(2000));

            // Test multi-client interaction (simulated)
            CalculatorClient.multiClientTest(calculator);

        } catch (Exception e) {
            System.err.println("CalculatorClient exception: " + e.toString());
            e.printStackTrace();
        }
    }

    private static void multiClientTest(Calculator calculator) {
        try {
            // Simulate multiple clients by interacting with the same calculator object.
            calculator.pushValue(5);
            calculator.pushValue(15);
            calculator.pushOperation("gcd");
            System.out.println("GCD: " + calculator.pop());

            calculator.pushValue(14);
            calculator.pushValue(21);
            calculator.pushOperation("lcm");
            System.out.println("LCM: " + calculator.pop());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

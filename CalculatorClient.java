import java.rmi.Naming;
import java.util.UUID;

public class CalculatorClient {
    public static void main(String[] args) {
        try {
            Calculator calculator = (Calculator) Naming.lookup("rmi://localhost:1099/CalculatorService");

            // Generate a unique ID for this client
            String clientId = UUID.randomUUID().toString();

            // Test operations with this client
            calculator.pushValue(clientId, 10);
            calculator.pushValue(clientId, 20);
            calculator.pushOperation(clientId, "max");
            System.out.println("Max: " + calculator.pop(clientId));

            // Test delayPop
            calculator.pushValue(clientId, 30);
            System.out.println("Delayed pop: " + calculator.delayPop(clientId, 2000));

            // Test multi-client interaction (simulated)
            CalculatorClient.multiClientTest(calculator);

        } catch (Exception e) {
            System.err.println("CalculatorClient exception: " + e.toString());
            e.printStackTrace();
        }
    }

    private static void multiClientTest(Calculator calculator) {
        try {
            String clientId1 = UUID.randomUUID().toString();
            String clientId2 = UUID.randomUUID().toString();

            // Simulate client 1
            calculator.pushValue(clientId1, 5);
            calculator.pushValue(clientId1, 15);
            calculator.pushOperation(clientId1, "gcd");
            System.out.println("Client 1 GCD: " + calculator.pop(clientId1));

            // Simulate client 2
            calculator.pushValue(clientId2, 14);
            calculator.pushValue(clientId2, 21);
            calculator.pushOperation(clientId2, "lcm");
            System.out.println("Client 2 LCM: " + calculator.pop(clientId2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

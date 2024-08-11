import java.rmi.Naming;
import java.util.UUID;

/**
 * Client class for testing the Calculator RMI service.
 * Connects to the Calculator service, performs various operations, 
 * and demonstrates interaction with the server using unique client IDs.
 */
public class CalculatorClient {
    public static void main(String[] args) {
        try {
            // Lookup the Calculator service from the RMI registry
            // The service is located at "rmi://localhost:1099/CalculatorService"
            // This connects the client to the server where the CalculatorImplementation is bound
            Calculator calculator = (Calculator) Naming.lookup("rmi://localhost:1099/CalculatorService");

            // Generate a unique ID for this client
            // UUID is used to ensure that each client has a distinct identifier
            String clientId = UUID.randomUUID().toString();

            // Perform operations with this client
            // Push value 55 onto the client's stack
            calculator.pushValue(clientId, 55);
            // Push value 15 onto the client's stack
            calculator.pushValue(clientId, 15);
            // Perform max operation on the stack and push the result
            calculator.pushOperation(clientId, "max");
            // Pop the result of the max operation and print it
            System.out.println("Max: " + calculator.pop(clientId));

            // Test delayPop
            // Push value 99 onto the client's stack
            calculator.pushValue(clientId, 99);
            // Perform a delayed pop operation (2000 milliseconds) and print the result
            System.out.println("Delayed pop: " + calculator.delayPop(clientId, 2000));

            // Test multi-client interaction (simulated)
            // This method demonstrates interaction between multiple simulated clients
            CalculatorClient.multiClientTest(calculator);

        } catch (Exception e) {
            // Print any exceptions encountered during execution
            // This will help in debugging if the client encounters errors
            System.err.println("CalculatorClient exception: " + e.toString());
            e.printStackTrace();
        }
    }

    /**
     * Simulates interaction between multiple clients with unique client IDs.
     * Demonstrates the use of the Calculator service by multiple clients.
     * @param calculator The Calculator service instance.
     */
    private static void multiClientTest(Calculator calculator) {
        try {
            // Generate unique IDs for two clients
            // UUID is used to ensure each simulated client has a distinct identifier
            String clientId1 = UUID.randomUUID().toString();
            String clientId2 = UUID.randomUUID().toString();

            // Simulate client 1 and 2 operations simultaneously to showcase separate client stacks
            // Push values 21 and 49 onto client 1's stack
            calculator.pushValue(clientId1, 21);
            calculator.pushValue(clientId1, 49);
            // Push values 9 and 6 onto client 2's stack
            calculator.pushValue(clientId2, 9);
            calculator.pushValue(clientId2, 6);
            // Perform GCD operation on client 1's stack
            calculator.pushOperation(clientId1, "gcd");
            // Perform LCM operation on client 2's stack
            calculator.pushOperation(clientId2, "lcm");
            // Pop the result of the GCD operation and print it (client 1)
            System.out.println("Client 1 GCD: " + calculator.pop(clientId1));
            // Pop the result of the LCM operation and print it (client 2)
            System.out.println("Client 2 LCM: " + calculator.pop(clientId2));
            
        } catch (Exception e) {
            // Print any exceptions encountered during multi-client testing
            e.printStackTrace();
        }
    }
}

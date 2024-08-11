import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

/**
 * Server class for the Calculator RMI service.
 * Sets up the RMI registry, creates the CalculatorImplementation instance,
 * and binds it to the registry for clients to connect.
 */
public class CalculatorServer {
    public static void main(String[] args) {
        try {
            // Start the RMI registry on the default port
            LocateRegistry.createRegistry(1099);

            // Create an instance of the CalculatorImplementation
            CalculatorImplementation calculatorImpl = new CalculatorImplementation();

            // Bind the CalculatorImplementation instance to the RMI registry
            Naming.rebind("CalculatorService", calculatorImpl);

            System.out.println("Calculator service is running.");
        } catch (Exception e) {
            System.err.println("CalculatorServer exception: " + e.toString());
            e.printStackTrace();
        }
    }
}

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
            // Start the RMI registry on port 1100
            // The registry is a service that allows remote objects to be looked up by clients
            LocateRegistry.createRegistry(1100);

            // Create an instance of the CalculatorImplementation
            // This instance will be used to handle client requests for stack operations
            CalculatorImplementation calculatorImpl = new CalculatorImplementation();

            // Bind the CalculatorImplementation instance to the RMI registry
            // The service is registered under the name "CalculatorService"
            // Clients will use this name to look up the service and interact with it
            Naming.rebind("CalculatorService", calculatorImpl);

            // Output message indicating that the server is up and running
            // This message will be printed to the console for confirmation
            System.out.println("Calculator service is running.");
        } catch (Exception e) {
            // Print any exceptions encountered during setup
            // This will help in debugging if the server fails to start
            System.err.println("CalculatorServer exception: " + e.toString());
            e.printStackTrace();
        }
    }
}

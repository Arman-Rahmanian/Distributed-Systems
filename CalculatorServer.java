import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class CalculatorServer {
    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);
            Calculator calculator = new CalculatorImplementation();
            Naming.rebind("rmi://localhost:1099/CalculatorService", calculator);
            System.out.println("Calculator Service is ready.");
        } catch (Exception e) {
            System.err.println("Calculator Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}

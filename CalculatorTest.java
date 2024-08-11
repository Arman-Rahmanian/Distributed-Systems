import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.UUID;
import static org.junit.Assert.*;

public class CalculatorTest {
    private Calculator calculator;
    private final String serverURL = "rmi://localhost:1099/CalculatorService";

    @Before
    public void setUp() throws Exception {
        // Start the RMI registry and bind the Calculator service
        LocateRegistry.createRegistry(1099);
        CalculatorImplementation calculatorImpl = new CalculatorImplementation();
        Naming.rebind(serverURL, calculatorImpl);
        calculator = (Calculator) Naming.lookup(serverURL);
    }

    @After
    public void tearDown() throws Exception {
        // Unbind the Calculator service
        Naming.unbind(serverURL);
    }

    @Test
    public void testPushValueSingleClient() throws RemoteException {
        String clientId = UUID.randomUUID().toString();
        calculator.pushValue(clientId, 5);
        calculator.pushValue(clientId, 10);
        assertEquals(10, calculator.pop(clientId));
        assertEquals(5, calculator.pop(clientId));
    }

    @Test
    public void testPushValueMultipleClients() throws RemoteException {
        String clientId1 = UUID.randomUUID().toString();
        String clientId2 = UUID.randomUUID().toString();
        String clientId3 = UUID.randomUUID().toString();

        calculator.pushValue(clientId1, 1);
        calculator.pushValue(clientId2, 2);
        calculator.pushValue(clientId3, 3);

        assertEquals(1, calculator.pop(clientId1));
        assertEquals(2, calculator.pop(clientId2));
        assertEquals(3, calculator.pop(clientId3));
    }

    @Test
    public void testPushOperationSingleClient() throws RemoteException {
        String clientId = UUID.randomUUID().toString();
        calculator.pushValue(clientId, 3);
        calculator.pushValue(clientId, 1);
        calculator.pushOperation(clientId, "min");
        assertEquals(1, calculator.pop(clientId));
    }

    @Test
    public void testPushOperationMultipleClients() throws RemoteException {
        String clientId1 = UUID.randomUUID().toString();
        String clientId2 = UUID.randomUUID().toString();
        
        // Client 1 operations
        calculator.pushValue(clientId1, 5);
        calculator.pushValue(clientId1, 10);
        calculator.pushOperation(clientId1, "max");
        assertEquals(10, calculator.pop(clientId1));

        // Client 2 operations
        calculator.pushValue(clientId2, 8);
        calculator.pushValue(clientId2, 4);
        calculator.pushOperation(clientId2, "min");
        assertEquals(4, calculator.pop(clientId2));
    }

    @Test
    public void testPopSingleClient() throws RemoteException {
        String clientId = UUID.randomUUID().toString();
        calculator.pushValue(clientId, 42);
        assertEquals(42, calculator.pop(clientId));
    }

    @Test
    public void testPopMultipleClients() throws RemoteException {
        String clientId1 = UUID.randomUUID().toString();
        String clientId2 = UUID.randomUUID().toString();
        
        calculator.pushValue(clientId1, 100);
        calculator.pushValue(clientId2, 200);

        assertEquals(100, calculator.pop(clientId1));
        assertEquals(200, calculator.pop(clientId2));
    }

    @Test
    public void testDelayPopSingleClient() throws RemoteException, InterruptedException {
        String clientId = UUID.randomUUID().toString();
        calculator.pushValue(clientId, 77);
        long startTime = System.currentTimeMillis();
        int value = calculator.delayPop(clientId, 2000);
        long elapsedTime = System.currentTimeMillis() - startTime;
        
        assertEquals(77, value);
        assertTrue(elapsedTime >= 2000);
    }

    @Test
    public void testDelayPopMultipleClients() throws RemoteException, InterruptedException {
        String clientId1 = UUID.randomUUID().toString();
        String clientId2 = UUID.randomUUID().toString();
        
        calculator.pushValue(clientId1, 5);
        calculator.pushValue(clientId2, 10);

        long startTime1 = System.currentTimeMillis();
        int value1 = calculator.delayPop(clientId1, 1000);
        long elapsedTime1 = System.currentTimeMillis() - startTime1;
        assertEquals(5, value1);
        assertTrue(elapsedTime1 >= 1000);

        long startTime2 = System.currentTimeMillis();
        int value2 = calculator.delayPop(clientId2, 1500);
        long elapsedTime2 = System.currentTimeMillis() - startTime2;
        assertEquals(10, value2);
        assertTrue(elapsedTime2 >= 1500);
    }
}

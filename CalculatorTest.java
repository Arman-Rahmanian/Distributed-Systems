import java.rmi.Naming;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Test class for the Calculator RMI service.
 * This class performs automated testing of the Calculator service,
 * including pushValue, pushOperation, pop, and delayPop operations
 * using multiple simulated clients.
 */
public class CalculatorTest {

    // Number of clients to simulate in each test
    private static final int NUM_CLIENTS = 5;

    // Delay in milliseconds for testing delayPop operation
    private static final int DELAY_MILLIS = 1000;

    // Port number for the RMI registry
    private static final int RMI_REGISTRY_PORT = 1099;

    public static void main(String[] args) {
        try {
            // Lookup the Calculator service from the RMI registry
            Calculator calculator = (Calculator) Naming.lookup("rmi://localhost:" + RMI_REGISTRY_PORT + "/CalculatorService");

            // Perform the different tests
            testPushValue(calculator);
            testPushOperation(calculator);
            testPop(calculator);
            testDelayPop(calculator);

        } catch (Exception e) {
            // Print any exceptions encountered during testing
            System.err.println("CalculatorTest exception: " + e.toString());
            e.printStackTrace();
        }
    }

    /**
     * Tests pushValue operation with multiple clients.
     * Each client pushes values onto their own stack and verifies the results.
     * @param calculator The Calculator service instance.
     * @throws Exception if an RMI-related error occurs.
     */
    private static void testPushValue(Calculator calculator) throws Exception {
        System.out.println("Testing pushValue...");

        // Create a CountDownLatch to ensure all client threads finish before continuing
        CountDownLatch latch = new CountDownLatch(NUM_CLIENTS);

        // Create a fixed thread pool to simulate multiple clients
        ExecutorService executor = Executors.newFixedThreadPool(NUM_CLIENTS);

        for (int i = 0; i < NUM_CLIENTS; i++) {
            final String clientId = "client" + i;
            executor.submit(() -> {
                try {
                    // Push values onto the client's stack
                    for (int val = 1; val <= 5; val++) {
                        calculator.pushValue(clientId, val);
                    }

                    // Verify the pushed values by popping them off
                    for (int val = 1; val <= 5; val++) {
                        int poppedValue = calculator.pop(clientId);
                        if (poppedValue != val) {
                            System.err.println("pushValue test failed for " + clientId + ": expected " + val + " but got " + poppedValue);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // Count down the latch to signal completion of this thread
                    latch.countDown();
                }
            });
        }

        // Wait for all client threads to finish
        latch.await();
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        System.out.println("pushValue test completed.");
    }

    /**
     * Tests pushOperation operation with multiple clients.
     * Each client pushes values and operations onto their own stack and verifies the results.
     * @param calculator The Calculator service instance.
     * @throws Exception if an RMI-related error occurs.
     */
    private static void testPushOperation(Calculator calculator) throws Exception {
        System.out.println("Testing pushOperation...");

        // Create a CountDownLatch to ensure all client threads finish before continuing
        CountDownLatch latch = new CountDownLatch(NUM_CLIENTS);

        // Create a fixed thread pool to simulate multiple clients
        ExecutorService executor = Executors.newFixedThreadPool(NUM_CLIENTS);

        for (int i = 0; i < NUM_CLIENTS; i++) {
            final String clientId = "client" + i;
            executor.submit(() -> {
                try {
                    // Push values onto the client's stack
                    for (int val = 1; val <= 5; val++) {
                        calculator.pushValue(clientId, val);
                    }

                    // Push min operation and verify result
                    calculator.pushOperation(clientId, "min");
                    int min = calculator.pop(clientId);
                    if (min != 1) { // Expected min value
                        System.err.println("pushOperation min test failed for " + clientId + ": expected 1 but got " + min);
                    }

                    // Push max operation and verify result
                    calculator.pushValue(clientId, 10);
                    calculator.pushOperation(clientId, "max");
                    int max = calculator.pop(clientId);
                    if (max != 10) { // Expected max value
                        System.err.println("pushOperation max test failed for " + clientId + ": expected 10 but got " + max);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // Count down the latch to signal completion of this thread
                    latch.countDown();
                }
            });
        }

        // Wait for all client threads to finish
        latch.await();
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        System.out.println("pushOperation test completed.");
    }

    /**
     * Tests pop operation with multiple clients.
     * Each client pops values from their own stack and verifies the results.
     * @param calculator The Calculator service instance.
     * @throws Exception if an RMI-related error occurs.
     */
    private static void testPop(Calculator calculator) throws Exception {
        System.out.println("Testing pop...");

        // Create a CountDownLatch to ensure all client threads finish before continuing
        CountDownLatch latch = new CountDownLatch(NUM_CLIENTS);

        // Create a fixed thread pool to simulate multiple clients
        ExecutorService executor = Executors.newFixedThreadPool(NUM_CLIENTS);

        for (int i = 0; i < NUM_CLIENTS; i++) {
            final String clientId = "client" + i;
            executor.submit(() -> {
                try {
                    // Push values onto the client's stack
                    for (int val = 1; val <= 5; val++) {
                        calculator.pushValue(clientId, val);
                    }

                    // Pop values and verify the order (LIFO)
                    for (int val = 5; val >= 1; val--) {
                        int poppedValue = calculator.pop(clientId);
                        if (poppedValue != val) {
                            System.err.println("pop test failed for " + clientId + ": expected " + val + " but got " + poppedValue);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // Count down the latch to signal completion of this thread
                    latch.countDown();
                }
            });
        }

        // Wait for all client threads to finish
        latch.await();
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        System.out.println("pop test completed.");
    }

    /**
     * Tests delayPop operation with multiple clients.
     * Each client pops a value from their own stack after a delay and verifies the results.
     * @param calculator The Calculator service instance.
     * @throws Exception if an RMI-related error occurs.
     */
    private static void testDelayPop(Calculator calculator) throws Exception {
        System.out.println("Testing delayPop...");

        // Create a CountDownLatch to ensure all client threads finish before continuing
        CountDownLatch latch = new CountDownLatch(NUM_CLIENTS);

        // Create a fixed thread pool to simulate multiple clients
        ExecutorService executor = Executors.newFixedThreadPool(NUM_CLIENTS);

        for (int i = 0; i < NUM_CLIENTS; i++) {
            final String clientId = "client" + i;
            executor.submit(() -> {
                try {
                    // Push values onto the client's stack
                    for (int val = 1; val <= 5; val++) {
                        calculator.pushValue(clientId, val);
                    }

                    // Delay pop and verify the result
                    int delayedPop = calculator.delayPop(clientId, DELAY_MILLIS);
                    int expectedValue = 5; // Expected value after delay
                    if (delayedPop != expectedValue) {
                        System.err.println("delayPop test failed for " + clientId + ": expected " + expectedValue + " but got " + delayedPop);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // Count down the latch to signal completion of this thread
                    latch.countDown();
                }
            });
        }

        // Wait for all client threads to finish
        latch.await();
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        System.out.println("delayPop test completed.");
    }
}

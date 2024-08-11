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

    private static final int NUM_CLIENTS = 5; // Number of simulated clients
    private static final int DELAY_MILLIS = 1000; // Delay time in milliseconds for delayPop test

    /**
     * Main method to perform tests on the Calculator RMI service.
     * It looks up the Calculator service from the RMI registry and
     * performs various tests on the service methods.
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        try {
            // Lookup the Calculator service from the RMI registry
            Calculator calculator = (Calculator) Naming.lookup("rmi://localhost:1099/CalculatorService");

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
     * Test the pushValue method of the Calculator service.
     * This test involves multiple clients pushing values onto their own stacks
     * and then popping the values to ensure they are correctly pushed.
     * @param calculator The Calculator service instance.
     * @throws Exception If an error occurs during testing.
     */
    private static void testPushValue(Calculator calculator) throws Exception {
        System.out.println("Testing pushValue...");

        // Create a CountDownLatch to wait for all client threads to complete
        CountDownLatch latch = new CountDownLatch(NUM_CLIENTS);
        // Create a fixed thread pool for client threads
        ExecutorService executor = Executors.newFixedThreadPool(NUM_CLIENTS);

        // Submit tasks for each client
        for (int i = 0; i < NUM_CLIENTS; i++) {
            final String clientId = "client" + i;
            executor.submit(() -> {
                try {
                    // Push values onto the client's stack
                    for (int val = 1; val <= 5; val++) {
                        calculator.pushValue(clientId, val);
                    }

                    // Verify the pushed values by popping them off
                    for (int expectedVal = 5; expectedVal >= 1; expectedVal--) {
                        int poppedValue = calculator.pop(clientId);
                        if (poppedValue != expectedVal) {
                            System.err.println("pushValue test failed for " + clientId + ": expected " + expectedVal + " but got " + poppedValue);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // Decrease the latch count when the task is done
                    latch.countDown();
                }
            });
        }

        // Wait for all client threads to finish
        latch.await();
        // Shut down the executor service
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        System.out.println("pushValue test completed.");
    }

    /**
     * Test the pushOperation method of the Calculator service.
     * This test involves multiple clients pushing values onto their stacks,
     * performing "min" and "max" operations, and verifying the results.
     * @param calculator The Calculator service instance.
     * @throws Exception If an error occurs during testing.
     */
    private static void testPushOperation(Calculator calculator) throws Exception {
        System.out.println("Testing pushOperation...");

        // Create a CountDownLatch to wait for all client threads to complete
        CountDownLatch latch = new CountDownLatch(NUM_CLIENTS);
        // Create a fixed thread pool for client threads
        ExecutorService executor = Executors.newFixedThreadPool(NUM_CLIENTS);

        // Submit tasks for each client
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
                    // Decrease the latch count when the task is done
                    latch.countDown();
                }
            });
        }

        // Wait for all client threads to finish
        latch.await();
        // Shut down the executor service
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        System.out.println("pushOperation test completed.");
    }

    /**
     * Test the pop method of the Calculator service.
     * This test involves multiple clients pushing values onto their stacks
     * and then popping them off to verify the order (LIFO).
     * @param calculator The Calculator service instance.
     * @throws Exception If an error occurs during testing.
     */
    private static void testPop(Calculator calculator) throws Exception {
        System.out.println("Testing pop...");

        // Create a CountDownLatch to wait for all client threads to complete
        CountDownLatch latch = new CountDownLatch(NUM_CLIENTS);
        // Create a fixed thread pool for client threads
        ExecutorService executor = Executors.newFixedThreadPool(NUM_CLIENTS);

        // Submit tasks for each client
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
                    // Decrease the latch count when the task is done
                    latch.countDown();
                }
            });
        }

        // Wait for all client threads to finish
        latch.await();
        // Shut down the executor service
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        System.out.println("pop test completed.");
    }

    /**
     * Test the delayPop method of the Calculator service.
     * This test involves multiple clients pushing values onto their stacks,
     * performing a delayed pop operation, and verifying the result.
     * @param calculator The Calculator service instance.
     * @throws Exception If an error occurs during testing.
     */
    private static void testDelayPop(Calculator calculator) throws Exception {
        System.out.println("Testing delayPop...");

        // Create a CountDownLatch to wait for all client threads to complete
        CountDownLatch latch = new CountDownLatch(NUM_CLIENTS);
        // Create a fixed thread pool for client threads
        ExecutorService executor = Executors.newFixedThreadPool(NUM_CLIENTS);

        // Submit tasks for each client
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
                    // Decrease the latch count when the task is done
                    latch.countDown();
                }
            });
        }

        // Wait for all client threads to finish
        latch.await();
        // Shut down the executor service
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        System.out.println("delayPop test completed.");
    }
}

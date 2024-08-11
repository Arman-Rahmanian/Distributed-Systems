## Java RMI Calculator

# Project overview
This project implements a distributed calculator service using Java RMI. The service allows multiple clients to perform stack-based arithmetic operations remotely. Each client has its own stack where values can be pushed and operations like min, max, gcd, and lcm can be performed.

# Compilation instructions:
place all files in the same folder, navigate to the directory and type the following command in the terminal: javac *.java 

# Execution instructions:
initialise the registry on the default port: rmiregistry
on a new terminal initialise the Calculator Server: java CalculatorServer
on terminals run the clients: java CalculatorClient
on a new terminal run the automated tests: java CalculatorTest

# Client test output
the output of the CalculatorClient test should be:
Max: 55
Delayed pop: 99
Client 1 GCD: 7
Client 2 LCM: 18

# Automated testing
the automated testing class tests the server with 5 simultaneous clients
if the tests pass, they should all say "xxx test completed.", otherwise an error message explaining the
expected and received result is.
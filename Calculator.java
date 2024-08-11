import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Calculator extends Remote {
    void pushValue(String clientId, int val) throws RemoteException;
    void pushOperation(String clientId, String operator) throws RemoteException;
    int pop(String clientId) throws RemoteException;
    boolean isEmpty(String clientId) throws RemoteException;
    int delayPop(String clientId, int millis) throws RemoteException;
}

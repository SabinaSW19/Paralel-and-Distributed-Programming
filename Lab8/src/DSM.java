import mpi.MPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DSM {
    private Map<String, Integer> variables = new HashMap<>();
    private Map<String, List<Integer>> subscribers = new HashMap<>();

    public DSM() {
        variables.put("v1", 0);
        subscribers.put("v1", new ArrayList<>());
        variables.put("v2", 0);
        subscribers.put("v2", new ArrayList<>());
        variables.put("v3", 0);
        subscribers.put("v3", new ArrayList<>());
    }

    public void addSubscriberProcess(String variable, Integer proc) {
        this.subscribers.get(variable).add(proc);
    }

    public void subscribeOperation(String variable) {
        int processNumber = MPI.COMM_WORLD.Rank();
        addSubscriberProcess(variable, processNumber);
        List<Integer> allProcesses = IntStream.range(0, MPI.COMM_WORLD.Size()).boxed().collect(Collectors.toList());
        notifyProcesses(allProcesses, new Message("subscribe", variable, processNumber));
    }

    public void unsubscribeOperation() {
        Message message = new Message("unsubscribe", "", 0);
        List<Integer> allProcesses = IntStream.range(0, MPI.COMM_WORLD.Size()).boxed().collect(Collectors.toList());
        notifyProcesses(allProcesses, message);
    }

    public void setReceived(String variable, Integer value) {
        variables.put(variable, value);
    }

    public void changeValueOperation(String variable, Integer value) {
        variables.put(variable, value);
        notifyProcesses(this.subscribers.get(variable), new Message("changeValue", variable, value));
    }

    public void compareExchangeOperation(String variable, Integer oldValue, Integer newValue) {
        if (variables.get(variable).equals(oldValue)) {
            variables.put(variable, newValue);
            notifyProcesses(this.subscribers.get(variable), new Message("changeValue", variable, newValue));
        }
    }

    private void notifyProcesses(List<Integer> procs, Message message) {
        for (Integer proc : procs)
            if (proc != MPI.COMM_WORLD.Rank()) {
                Message[] send = new Message[1];
                send[0] = message;
                MPI.COMM_WORLD.Send(send, 0, 1, MPI.OBJECT, proc, 0);
            }
    }

    @Override
    public String toString() {
        return "DSM: variables = " + variables + "\n\t\t\t\tsubscribers = " + subscribers;
    }
}

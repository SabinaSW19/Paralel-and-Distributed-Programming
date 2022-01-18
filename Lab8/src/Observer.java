import mpi.MPI;

public class Observer implements Runnable {
    private DSM distributedSharedMemory;

    public Observer(DSM distributedSharedMemory) {
        this.distributedSharedMemory = distributedSharedMemory;
    }

    @Override
    public void run() {
        while (true) {
            Message[] recv = new Message[1];
            MPI.COMM_WORLD.Recv(recv, 0, 1, MPI.OBJECT, MPI.ANY_SOURCE, 0);
            Message received_message = recv[0];
            if (received_message.getOperation().equals("subscribe")) {
                System.out.println("Process "+MPI.COMM_WORLD.Rank()+" received process "+received_message.getValue()+" subscribed to: "+received_message.getVariable());
                distributedSharedMemory.addSubscriberProcess(received_message.getVariable(), received_message.getValue());
            }
            if (received_message.getOperation().equals("unsubscribe"))
                break;
            if (received_message.getOperation().equals("changeValue")) {
                System.out.println("Process "+MPI.COMM_WORLD.Rank()+" received process "+received_message.getValue()+" changed value to: "+received_message.getVariable());
                distributedSharedMemory.setReceived(received_message.getVariable(), received_message.getValue());
            }
            System.out.println("Process " + MPI.COMM_WORLD.Rank() + ", " + distributedSharedMemory);
        }
    }
}

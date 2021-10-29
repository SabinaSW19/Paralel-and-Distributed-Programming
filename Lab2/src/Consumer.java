public class Consumer extends Thread {
    //sum up the results from the producer
    Buffer buffer;
    int vectorLength;

    public Consumer(Buffer buffer,int vectorLength)
    {
        this.buffer=buffer;
        this.vectorLength=vectorLength;
    }

    @Override
    public void run()
    {
        int sum=0;
        for(int i=0;i<vectorLength;i++)
        {
            try {
                sum+=buffer.receive();
                System.out.println("Consumer received "+sum);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Consumer final: "+sum);
    }
}

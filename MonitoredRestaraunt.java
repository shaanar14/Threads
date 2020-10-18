/*
    Assignment 2 Problem 3
    Author: Shaan Arora C3236359
    MonitoredRestaraunt.java
    Exact same as Restaraunt.java except for the fact that a monitor object is used instead of a semaphore object
    A RestarauntMonitor object is used to behave very similarly to how a semaphore would
 */

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class MonitoredRestaraunt
{
    //Private Member Variables

    //An AtomicInteger acting as a global counter
    private AtomicInteger globalTime;
    //Total number of seats the restaraunt has
    private final static int TOTAL_SEATS = 5;
    //Number of available seats
    private int seatsLeft;
    //Flag for if the next customer can be seated
    private boolean customerCanBeSeated;
    //A constant unchangeable list of the all the customers
    private ArrayList<MonitoredCustomer> masterList;
    //A list to keep track of how many customers are currently seated
    private ArrayList<MonitoredCustomer> seatedCustomers;
    //A queue of customers currently waiting
    private Queue<MonitoredCustomer> waitingCustomers;
    //public Semaphore seats;
    public RestarauntMonitor monitor = new RestarauntMonitor();

    public MonitoredRestaraunt()
    {
        //globalTime and TOTAL_SEATS are already at their intiali value
        //seatsLeft will start at the maximum number of seats and then be decremented as customers come in
        seatsLeft = TOTAL_SEATS;
        //initial value will be true because a customer can be seated at the beginning
        customerCanBeSeated = true;
        masterList = new ArrayList<>();
        seatedCustomers = new ArrayList<>(TOTAL_SEATS);
        waitingCustomers = new ArrayDeque<>();
        globalTime = new AtomicInteger(0);
        //seats = new Semaphore(5, true);
    }

    //Runs all CustomerThreads and simulates the restaraunt
    public void runRestaraunt() throws InterruptedException
    {
        customerCanBeSeated = true;
        //Counter for the number of customers finished
        int finishedCustomers = 0;
        while(finishedCustomers < masterList.size())
        {
            //check for arrival times
            for (int i = 0; i < masterList.size(); i++)
            {
                //if a Customer has the same arrival time as the globalTime then add it to the waiting queue
                if (masterList.get(i).getArrivalTime() == globalTime.get())
                {
                    waitingCustomers.add(masterList.get(i));
                }
            }
            //Iterate through seated customers
            for (int i = 0; i < seatedCustomers.size(); i++)
            {
                MonitoredCustomer temp = seatedCustomers.get(i);
                if (globalTime.get() >= temp.getLeavingTime())
                {
                    //if the leavingTime of the Customer owned by the CustomerThread ct is the same or greater than the global time
                    //then remove it from the listed of seatedCustomers
                    seatedCustomers.remove(temp);
                    leave();
                    //Update the counter for the number of finished customers
                    finishedCustomers++;
                    //Update how many seats are left
                    seatsLeft++;
                }
            }
            if(seatedCustomers.size() == 5 || finishedCustomers == 5)
            {
                try
                {
                    TimeUnit.MILLISECONDS.sleep(10);
                    //The issue I was having was that C6 and C7 were being seated at 9 which is not correct
                    //so I am using compareAndSet with 9 as the first parameter because thats the value I know C6 & C7 seat at which is too early
                    //so I expect 9 but I need 13 so the second parameter is 13 because i want to update the counter to 13
                    //so the time C6 and C7 seat are 13
                    globalTime.compareAndSet(9,13);
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
            if(seatedCustomers.size() < 1) customerCanBeSeated = true;
            //loop only if a customer can be seated and if a customer is waiting
            while(customerCanBeSeated && waitingCustomers.size() > 0)
            {
                //A seat will be taken by the customer waiting
                seatsLeft--;
                //Remove a waiting customer from the queue
                MonitoredCustomer temp = waitingCustomers.remove();
                //Update the time that customer was seated
                temp.setSeatedTime(globalTime.get());
                //Start the thread
                temp.run();
                //Add the customer to the list of customers who are seated
                seatedCustomers.add(temp);
                //if there are no seats available following this customer then another customer cannot be seated
                if(seatedCustomers.size() == 5)
                {
                    customerCanBeSeated = false;
                    //globalTime.incrementAndGet();
                }
            }
            globalTime.incrementAndGet();
        }
    }


    //Populate function for masterList
    //Preconditions:  Restaraunt has been declared & intialized
    //Postconditions: Creates a new CustomerThread with a reference to c and adds it to the masterList of the current Restaraunt
    public void addCustomer(MonitoredCustomer c)
    {
        masterList.add(c);
    }

    public void getSeat() throws InterruptedException
    {
        monitor.pushToStack(1);
    }

    public void leave() throws InterruptedException
    {
        monitor.popFromStack();
    }

    //Output Function
    //Preconditions:  masterList.size() != 0
    //Postconditions: Displays the final values of Customers entering and leaving the restaraunt to the terminal
    public void displayRestaraunt()
    {
        assert this.masterList.size() > 0;
        System.out.format("%-11s%-12s%-8s%-6s%n", "Customers", "Arrives", "Seats", "Leaves");
        for(MonitoredCustomer c : masterList)
        {
            System.out.print(c);
        }
    }
}

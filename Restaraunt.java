import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

public class Restaraunt
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
    private ArrayList<Customer> masterList;
    //A list to keep track of how many customers are currently seated
    private ArrayList<Customer> seatedCustomers;
    //A queue of customers currently waiting
    private Queue<Customer> waitingCustomers;

    public Restaraunt()
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
    }

    //Runs all CustomerThreads and simulates the restaraunt
    public void runRestaraunt()
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
                Customer temp = seatedCustomers.get(i);
                if (globalTime.get() >= temp.getLeavingTime())
                {
                    //if the leavingTime of the Customer owned by the CustomerThread ct is the same or greater than the global time
                    //then remove it from the listed of seatedCustomers
                    seatedCustomers.remove(temp);
                    //Update the counter for the number of finished customers
                    finishedCustomers++;
                    //Update how many seats are left
                    seatsLeft++;
                }
            }
            //
            if(seatedCustomers.size() < 1) customerCanBeSeated = true;
            //loop only if a customer can be seated and if a customer is waiting
            while(customerCanBeSeated && waitingCustomers.size() > 0)
            {
                //A seat will be taken by the customer waiting
                seatsLeft--;
                //Remove a waiting customer from the queue
                Customer temp = waitingCustomers.remove();
                //Update the time that customer was seated
                temp.setSeatedTime(globalTime.get());
                //Start the thread
                temp.run();
                //Add the customer to the list of customers who are seated
                seatedCustomers.add(temp);
                //if there are no seats available following this customer then another customer cannot be seated
                if(seatsLeft < 1) customerCanBeSeated = false;
            }
            globalTime.addAndGet(1);
        }
    }


    //Populate function for masterList
    //Preconditions:  Restaraunt has been declared & intialized
    //Postconditions: Creates a new CustomerThread with a reference to c and adds it to the masterList of the current Restaraunt
    public void addCustomer(Customer c)
    {
        masterList.add(c);
    }

    //Output Function
    //Preconditions:  masterList.size() != 0
    //Postconditions: Displays the final values of Customers entering and leaving the restaraunt to the terminal
    public void displayRestaraunt()
    {
        assert this.masterList.size() > 0;
        System.out.format("%-11s%-12s%-8s%-6s%n", "Customers", "Arrives", "Seats", "Leaves");
        for(Customer c : masterList)
        {
            System.out.print(c);
        }
    }
}

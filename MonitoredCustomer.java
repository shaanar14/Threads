/*
    Assignment 2 Problem 2
    Author: Shaan Arora C3236359
    Customer.java
    Core object to simulate a COVID safe restaraunt.
    Implements Runnable so we can run the Restaraunt concurrently using threads and acquiring/release permits from a semaphore object
 */

import java.util.concurrent.TimeUnit;

public class MonitoredCustomer implements Runnable
{
    //Private member variables

    //The ID of a customer
    private String id;
    //The time in which a customer object arrives
    private int arrivalTime;
    //Time it takes for a customer to eat
    private int eatingTime;
    //Time the customer was seated
    private int seatedTime;
    //The time the customer left
    private int leavingTime;
    //Resource a Customer will try to access
    private MonitoredRestaraunt diningAt;

    //Default Constructor
    public MonitoredCustomer()
    {
        this.id = "";
        this.arrivalTime = 0;
        this.eatingTime = 0;
        this.seatedTime = 0;
        this.leavingTime = 0;
    }

    //Parameter constructor
    public MonitoredCustomer(MonitoredRestaraunt r)
    {
        this.id = "";
        this.arrivalTime = 0;
        this.eatingTime = 0;
        this.seatedTime = 0;
        this.leavingTime = 0;
        this.diningAt = r;
    }

    //Run function so a Customer object can behave concurrently
    @Override
    public void run()
    {
        try
        {
            TimeUnit.MILLISECONDS.sleep(10);
            this.acquire();
            this.eat();
            TimeUnit.MILLISECONDS.sleep(10);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    //Acquire a permit/seat from the Restaraunt the Customer object has reference to
    public void acquire() throws InterruptedException
    {
        this.diningAt.getSeat();
    }

    //Release the permit/seat it was using
    public void release() throws InterruptedException
    {
        this.diningAt.leave();
    }

    //Another setter for leavingTime reprsenting the action of a customer eating
    public void eat()
    {
        this.leavingTime = this.seatedTime + this.eatingTime;
    }

    //Setters

    public void setID(String i) {this.id = i;}

    public void setArrivalTime(int a) {this.arrivalTime = a;}

    public void setEatingTime(int e) {this.eatingTime = e;}

    public void setSeatedTime(int s) {this.seatedTime = s;}

    public void setLeavingTime(int l) {this.leavingTime = l;}

    //Getters

    public String getID() {return this.id;}

    public int getArrivalTime() {return this.arrivalTime;}

    public int getEatingTime() {return this.eatingTime;}

    public int getSeatedTime() {return this.seatedTime;}

    public int getLeavingTime() {return this.leavingTime;}

    @Override
    public String toString()
    {
        return String.format("%-11s%-12d%-8d%-6d%n", this.getID(), this.getArrivalTime(), this.getSeatedTime(), this.getLeavingTime());
    }
}

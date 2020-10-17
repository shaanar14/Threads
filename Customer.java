/*
    Assignment 2 Problem 2
    Author: Shaan Arora C3236359

 */

import java.util.concurrent.Semaphore;

public class Customer implements Runnable
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
    private Semaphore seat;

    //Default Constructor
    public Customer(Semaphore seat)
    {
        this.id = "";
        this.arrivalTime = 0;
        this.eatingTime = 0;
        this.seatedTime = 0;
        this.leavingTime = 0;
        this.seat = seat;
    }

    //Parameter constructor
    public Customer(String i, int a , int e, int s, int l, Semaphore seat)
    {
        this.id = i;
        this.arrivalTime = a;
        this.eatingTime = e;
        this.seatedTime = s;
        this.leavingTime = l;
        this.seat = seat;
    }

    @Override
    public void run()
    {
        this.acquire();
        this.eat();
        this.release();
    }

    public void acquire()
    {
        if(seat != null)
        {
            try
            {
                seat.acquire();
            }
            catch(InterruptedException e)
            {
                System.err.format("Error: %s\n",e);
            }
        }
    }

    public void release()
    {
        if(seat != null) seat.release();
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

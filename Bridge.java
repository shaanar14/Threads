/*
    Assignment 2 Problem 1
    Author: Shaan Arora C3236359
    Second core object that contains a Semaphore that our Farmer  objects will try to gain access to when executed by a thread
 */

import java.util.concurrent.*;
public class Bridge
{
    //Semaphore Object acting as the resource a Farmer object will try to acquire
    private final Semaphore semaphore;
    //Maximum number of steps a farmer takes to cross the bridge
    private static final int maxSteps = 20;
    //Count of number farmers that have cross
    private int neon;

    public Bridge()
    {
        //One resource with fairness set to true
        this.semaphore = new Semaphore(1, true);
        this.neon = 0;
    }

    public void crossBridge(Farmer f)
    {
        System.out.printf("%s: Waiting for bridge. Going towards %s\n", f.getName(), f.getDirection());
        //A Farmer is attempting to cross the bridge
        try
        {
            semaphore.acquire();
            //using f.step() breaks it
            f.setSteps(5);
            //make this a 200ms delay every 5 steps
            for(; f.getSteps() < maxSteps; f.step())
            {
                System.out.printf("%s: Crossing the bridge Step %d\n", f.getName(), f.getSteps());
                TimeUnit.MILLISECONDS.sleep(200);
            }
        }
        catch(InterruptedException iex)
        {
            iex.printStackTrace();
        }
        //A Farmer has crossed the bridge
        System.out.printf("%s: Across the bridge\n", f.getName());
        this.neon++;
        System.out.printf("NEON = %d\n", this.neon);
        //Swap direction once a farmer has crossed the bridge but its ID remains
        //possibly redundant
        f.setCrossed(true);
        f.setDirection(f.getDirection() == Farmer.Direction.North ? Farmer.Direction.South : Farmer.Direction.North);
        semaphore.release();
        if(this.getNeon() == 100)
        {
            System.out.println("Neon 100 reached, program exiting");

            System.exit(0);
        }
    }

    public int getNeon() {return this.neon;}
}

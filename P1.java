/*
    Assignment 2 Problem 1
    Author: Shaan Arora C3236359
    Main driver class which runs multiple threads to simulate the Single Lange Bridge Problem
 */

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class P1
{
    private static volatile int northCount = 0;
    private static volatile int southCount = 0;
    public static void main(String[] args)
    {
        assert args.length > 0;
        String fileName = args[0];
        File f = new File(fileName);
        try
        {
            Scanner scan = new Scanner(f);
            if(scan.hasNextLine())
            {
                //Clean the line from the file that is being read such that we only have integers
                String clean = scan.nextLine().replaceAll("\\D+", "");
                //Turn that cleaned up input into a String array so that it is easier to access each individual integer
                String[] test = clean.split("");
                //Assumes that N comes first in the file i.e. N = 2 then S = 2
                int n =  Integer.parseInt(test[0]);
                int s =  Integer.parseInt(test[1]);
                if(n == 0 || s == 0)
                {
                    System.out.println("Cannot have 0 farmers, exiting program so this can be fixed");
                    System.exit(-1);
                }
                //Maximum number of farmers from the north island
                final Integer maxNorth = n;
                //Maximum number of farmers from the south island
                final Integer maxSouth = s;
                final Bridge bridge = new Bridge();
                //Farmers travelling from the south island travelling north
                //Was using new Runnable but IDE recommended lambda
                Thread travelNorth = new Thread(() ->
                {
                    while(true)
                    {
                        Farmer farmer = new Farmer(bridge, Farmer.Direction.North);
                        synchronized(maxSouth)
                        {
                            southCount++;
                            if(southCount > maxSouth){break;}
                            farmer.setName("S_Farmer" + southCount);
                        }
                        Thread th = new Thread(farmer);
                        th.start();
                        try{TimeUnit.MILLISECONDS.sleep(200);}
                        catch(InterruptedException iex){iex.printStackTrace();}
                    }
                });
                //Farmers from the north island travelling south
                Thread travelSouth = new Thread(() ->
                {
                    while(true)
                    {
                        Farmer farmer = new Farmer(bridge, Farmer.Direction.South);
                        synchronized(maxNorth)
                        {
                            northCount++;
                            if(northCount > maxNorth){break;}
                            farmer.setName("N_Farmer" + northCount);
                        }
                        Thread th = new Thread(farmer);
                        th.start();
                        try{TimeUnit.MILLISECONDS.sleep(200);}
                        catch(InterruptedException iex){iex.printStackTrace();}
                    }
                });
                //Start the thread for all the north island farmers travelling south
                travelSouth.start();
                //Start the thread for all the south island farmers travelling north
                travelNorth.start();
                travelSouth.join();
                travelNorth.join();
            }
        }
        catch(IOException | InterruptedException ex)
        {
            ex.printStackTrace();
        }
    }
}
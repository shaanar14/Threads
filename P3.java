/*
    Assignment 2 Problem 2
    Main driver class which runs multiple threads to simulate a restaraunt and customers entering & leaving according to specific rules
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class P3
{
    public static void main(String[] args)
    {
        assert args.length > 0 : "Command Line Arguments expected";
        final MonitoredRestaraunt restaraunt = new MonitoredRestaraunt();
        String fileName = args[0];
        File f = new File(fileName);
        try
        {
            Scanner scan = new Scanner(f);
            while(scan.hasNextLine())
            {
                //capture the next line read in from the file
                String line = scan.nextLine();
                //If we reach the last line of the file then just break out of the loop
                if(line.equalsIgnoreCase("END")) break;
                //Remove all spaces from the line read in
                String[] output = line.split(" ");
                MonitoredCustomer temp = new MonitoredCustomer(restaraunt);
                //Should really do error checking to make sure that
                //  the first value of output is actually a number, the second is actually a valid ID and the third is an integer
                //Create a Customer object
                temp.setArrivalTime(Integer.parseInt(output[0]));
                temp.setID(output[1]);
                temp.setEatingTime(Integer.parseInt(output[2]));
                //Add that Customer object list to the masterList of Restaraunt
                restaraunt.addCustomer(temp);
            }
            //close the file being read
            scan.close();
            restaraunt.runRestaraunt();
            restaraunt.displayRestaraunt();
        } catch (FileNotFoundException | InterruptedException e)
        {
            e.printStackTrace();
        }
        /*RestarauntMonitor monitor = new RestarauntMonitor();
        try
        {
            monitor.pushToStack(1);
            monitor.pushToStack(2);
            monitor.pushToStack(3);
            monitor.pushToStack(4);
            monitor.pushToStack(5);
            System.out.println(monitor.popFromStack());
            System.exit(-1);
        } catch (InterruptedException e)
        {

            e.printStackTrace();
        }*/

    }
}

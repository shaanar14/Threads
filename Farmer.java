/*
    Assignment 2 Problem 1
    Author: Shaan Arora C3236359
    Core object that is implements Runnable so that a Farmer object can be executed by a thread
         and in the context of the problem will try to access a resource which will be a Semaphore object which is contained in a Bridge object
 */

public class Farmer implements Runnable
{
    private String name;
    private Bridge bridge;
    private int steps;
    public enum Direction {North, South}
    private Direction direction;
    private boolean crossed;

    public Farmer()
    {
        this.name = "";
        this.bridge = new Bridge();
        this.steps = 0;
        this.direction = Direction.North;
        this.crossed = false;
    }

    public Farmer(Bridge b, Direction d)
    {
        this.name = "";
        this.bridge = b;
        this.steps = 0;
        this.direction = d;
        this.crossed = false;
    }

    @Override
    public void run()
    {
        boolean running = true;
        //Continously attempt to cross the bridge
        while(running)
        {
            //Might need the this keyword
            bridge.crossBridge(this);
            if(Thread.interrupted())
            {
                running = false;
            }
        }
    }

    //Setters

    public void setName(String n) {this.name = n;}

    public void setBridge(Bridge b) {this.bridge = b;}

    public void setSteps(int s) {this.steps = s;}

    //incremements the number of steps the farmer has taken
    public void step() {this.steps += 5;}

    public void setDirection(Direction d) {this.direction = d;}

    public void setCrossed(boolean c) {this.crossed = c;}

    //Getters

    public String getName() {return this.name;}

    public Bridge getBridge() {return this.bridge;}

    public int getSteps() {return this.steps;}

    public Direction getDirection() {return this.direction;}

    public boolean isCrossed() {return this.crossed;}
}

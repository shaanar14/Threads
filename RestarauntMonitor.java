import java.util.Stack;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RestarauntMonitor
{
    private Stack<Integer> stack = new Stack<>();
    final static int CAPACITY = 5;
    final Lock lock = new ReentrantLock();
    final Condition stackEmpty = lock.newCondition();
    final Condition stackFull = lock.newCondition();

    public void pushToStack(int n) throws InterruptedException
    {
        try
        {
            lock.lock();
            while(stack.size() == CAPACITY)
            {
                stackFull.await();
            }
            stack.push(n);
            stackEmpty.signalAll();
        }
        finally
        {
            lock.unlock();
        }
    }

    public int popFromStack() throws InterruptedException
    {
        try
        {
            lock.lock();
            while (stack.size() == 0)
            {
                stackEmpty.await();
            }
            return stack.pop();
        }
        finally
        {
            stackFull.signalAll();
            lock.unlock();
        }
    }

}

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DataSet
{
   /**
      Constructs an empty data set.
   */
   public DataSet(double[] values)
   {
      this.values = values;
      max = 0;      
      myLock = new ReentrantLock();
      completionCondition = myLock.newCondition();      
   }

   /**
      Gets the maximum of the added data.
      @return the average or 0 if no data has been added
   */
   public double getMaximum() 
   {
      if (values.length == 0) return 0;

      max = values[0];      
      final int THREADS = 10;      
      int size = values.length / THREADS;
      completionCount = 0; // Initialize completionCount
      
      for (int i = 0; i < THREADS; i++)
      {
         Runnable r = new RangeProcessor(i * size, (i + 1) * size);
         Thread t = new Thread(r);
         t.start();
      }

      myLock.lock(); // Acquire the lock
      try
      {
         while (completionCount < THREADS) // Check if completion count has reached the total number of threads
         {
            try
            {
               completionCondition.await(); // Wait until completion count changes
            }
            catch (InterruptedException e)
            {
               return 0; // Return 0 if InterruptedException occurs
            }
         }
      }
      finally
      {
         myLock.unlock(); // Release the lock
      }

      return max;
   }

   private class RangeProcessor implements Runnable
   {
      public RangeProcessor(int start, int end)
      {
         this.start = start;
         this.end = Math.min(end, values.length);
      }
      
      public void run()
      {
         double rangeMax = values[start];
         for (int i = start + 1; i < end; i++)
            rangeMax = Math.max(rangeMax, values[i]);
         myLock.lock();
         try
         {
            max = Math.max(max, rangeMax);
            completionCount++;
            completionCondition.signal(); // Signal that the completion count has changed
         }
         finally
         {
            myLock.unlock();
         }
      }
      
      private int start;
      private int end;
   }
   
   private double[] values;

   private double max;
   private int completionCount;
   private Lock myLock;
   private Condition completionCondition;
   
   // this method is used to check your work
   
   public static void main(String[] args)  
   {
      final int SIZE = 10000000;
      double[] v = new double[SIZE];
      int shift = (int) (Math.random() * SIZE);
      for (int i = 0; i < SIZE; i++) v[(i + shift) % SIZE] = i;
      DataSet data = new DataSet(v);
      double max = data.getMaximum();
      System.out.println(max);
   }
}

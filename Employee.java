package project1;

import java.util.Random;
import java.util.Queue;
import java.util.PriorityQueue;

public class Employee implements Runnable{
   public static long time = System.currentTimeMillis();
   
   private int numberOfCustomers;
   
   //Queues of customers
   public static Queue<Customer> doneCustomers; //customers ordered by their id
   
   //Keeping track of available registers
   public static boolean[] registers;
   
   public static Random rand;
   public Thread thread;
   
   public Employee(int numRegs, int numCusts) {
      rand = new Random();
      thread = new Thread(this, "employee");
      doneCustomers = new PriorityQueue<>();
      registers = new boolean[(int) (numRegs/2 + .5)];
      numberOfCustomers = numCusts;
   }
   
   @Override
   public void run() {
      //waiting for all customers to finish shopping
      while(doneCustomers.size() != numberOfCustomers) { 
         System.out.print("");
      }
      
      //Employee is closing the store
      try { 
         Thread.sleep(rand.nextInt(10000)); 
      } catch (InterruptedException ie) {
         System.out.println(ie);
      }
      
      //wake up all sleeping threads
      for(Customer c : doneCustomers) {
         c.thread.interrupt();
      }
      
      /*
       * Next block of commented out code used to test to make sure customers are woken up
       * As well as check that customers leave in correct order
       */
      
      /*
      try {
         Thread.sleep(1000); //Integer.MAX_VALUE
      }catch(InterruptedException ie) {
         System.out.println(ie);
      }
      */
      
      
      //Send customers home in ascending order of their id's 
      while(!doneCustomers.isEmpty()) {
         Customer c = doneCustomers.peek();
         c.msg("leaves the parking lot");
         doneCustomers.remove(c);
         c.canGoHome = true;
      }
      
      /*
       * Next block of code is attempt to use join and isAlive.
       * Didn't work. Sent customers home a different way
       */
      
      /*
      while(!doneCustomers.isEmpty()) {
         Customer c = doneCustomers.peek();
         try {
            c.thread.join();
         } catch (InterruptedException ie) {
            System.out.println(ie);
         }
         c.canGoHome = true;
         while(!c.thread.isAlive()) {
            System.out.print("");//I saw that I had a problem here.
         }
         doneCustomers.remove(c);
      }
      */
      
      //Employee's job is done. He can go home now
      msg("Job is done. Going home now");
      
   }
   
   public void start() {
      thread.start();
   }
   
   public static void msg(String m) {
      System.out.println("["+(System.currentTimeMillis()-time)+"] "+Thread.currentThread().getName()+": "+m);
   }
   
   public static int findFreeRegister() {
      for(int i = 0; i < registers.length; i++) {
         if(registers[i] == false) {
            return i;
         }
      }
      return -1;
   }
}

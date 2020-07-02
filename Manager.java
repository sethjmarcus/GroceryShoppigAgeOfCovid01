package project1;

import java.util.Random;
import java.util.List;
import java.util.LinkedList;

public class Manager implements Runnable{
   public static long time = System.currentTimeMillis();
   
   private int numberOfCustomers;
   private Thread thread;
   private Random rand;
   private static boolean active; //Is the manager in the store?
   
   //List of customers who have gone/been placed in the store
   public static List<Customer> placedCustomers = new LinkedList<>();
   
   public Manager(int numCusts) {
      rand = new Random();
      active = false;
      thread = new Thread(this, "manager");
      numberOfCustomers = numCusts;
   }
   
   @Override
   public void run() {
      //Manager is coming to the store
      try {
         Thread.sleep(rand.nextInt(10000)); 
      } catch (InterruptedException ie) {
         System.out.println(ie);
      }
      msg("Manager has arrived to the store");
      active = true;
      
      //While there are still customers who haven't entered the store, the manager needs to stay
      while(placedCustomers.size() != numberOfCustomers) {
         System.out.print("");
      }
      
      msg("Is heading home.");
      active = false;
   }
   
   public void start() {
      thread.start();
   }
   
   public void msg(String m) {
      System.out.println("["+(System.currentTimeMillis()-time)+"] "+Thread.currentThread().getName()+": "+m);
   }

   public static boolean isActive() {
      return active;
   }
   
}

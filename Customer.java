package project1;

import java.util.Random;
import java.util.Queue;
import java.util.LinkedList;


public class Customer implements Runnable, Comparable<Customer>{
   private static long time = System.currentTimeMillis();
   
   private String name;
   private int storeCapacity;
   private boolean elderly;
   private int id; //Used for ordering based by customer creation
   private Random rand;
   public boolean canGoHome;
   public Thread thread;
   
   //Queues of Customers
   private static Queue<Customer> nextBatch; //Queue of customers in the store/next batch of customers to go in the store
   private static Queue<Customer> waitingOutside; //Queue of Customers lining up outside
   
   public Customer(String n, int i, int cap) {
      storeCapacity = cap;
      name = n;
      id = i;
      thread = new Thread(this, n);
      rand = new Random();
      if(rand.nextInt() % 4 == 0) {
         elderly = true;
      }else {
         elderly = false;
      }
      canGoHome = false;
      nextBatch = new LinkedList<>();
      waitingOutside  = new LinkedList<>();
   }
   
   public void start() {
      thread.start();
   }
   
   @Override
   public void run() {
      
      gotoSleep(rand.nextInt(10000)); 
      //Used 10000000 milliseconds ~ 3 hours, instead of Integer.Max_Value, which is 
      //apprimately 24 days which seems too unreasonable.
      
      waitingOutside.add(this);
      
      msg("Is now on the line");
      while(!Manager.isActive()) {
         System.out.print("");//busy waiting until manager arrives
      }//Manager has arrived
      
      while(nextBatch.size() >= storeCapacity) { //busy wait. Too many people inside
         System.out.print("");
      }
      nextBatch.add(this);
      
      msg("Entering The Store. Beginning to shop");//customer has entered store
      Manager.placedCustomers.add(this);
      
      //moving customer from inside to outside
      waitingOutside.remove(this); 
      
    //customer is now inside store shopping
      gotoSleep(rand.nextInt(10000)); 

      msg("Finished Shopping. Heading to checkout");
      
      //Customer is rushing to checkout
      int currPriority = thread.getPriority();
      thread.setPriority(currPriority%10 + 1); //in case currPriority is 10, making it 1
      gotoSleep(rand.nextInt(10000)); 
      thread.setPriority(5);
      
      Thread.yield();
      if(!this.elderly) {
         Thread.yield();
      }
      
      //Customer is now on the line checking out
      msg("Is now online, waiting to checkout");
      
      //waiting for next free register
      int nextFreeRegister; 
      do{
         nextFreeRegister = Employee.findFreeRegister();
      }while(nextFreeRegister < 0);//no free registers
      
      Employee.registers[nextFreeRegister] = true; //register in use
      msg("Is using register " + (nextFreeRegister*2 + 1)); 
      
      gotoSleep(rand.nextInt(10000)); //Customer takes time at check out 
      msg("Is done using the register");
      
      //Bookeeping of registers, who is in store, who has left the store
      nextBatch.remove(this);
      Employee.registers[nextFreeRegister] = false;
      Employee.doneCustomers.add(this);
      
      //Customer sleeps until employee wakes them up
      try {
         Thread.sleep(Integer.MAX_VALUE);
      } catch (InterruptedException ie) {
         msg("Has been woken up");
      }
      
      //Employee will tell the customer when they can go home and die
      while(!canGoHome) {
         System.out.print("");
      }
   }
   
   //Utility method. Puts thread to sleep for time long.
   public void gotoSleep(int time) {
      try {
         Thread.sleep(time);
      } catch (InterruptedException ie) {
         System.out.println(ie);
      }
   }
   
   public void msg(String m) {
      System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
   }

   @Override
   public int compareTo(Customer c) {
      if( this.id - c.id > 0) {
         return 1;
      }else if(this.id - c.id < 0) {
         return -1;
      }
      return 0;
   }
   
   public void canLeave() {
      this.canGoHome = true;
   }
   
   public String getName() {
      return this.name;
   }
}


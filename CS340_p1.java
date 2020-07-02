package project1;

public class Marcus_Seth_CS340_p1{   
   
   public static long time = System.currentTimeMillis();
   
   private static int numCustomers = 20; 
   private static final int storeCapacity = 6;
   private static final int numSelfCheckOut = 4;
   
   public static void main(String[] args) {
      if(args.length > 0) {
         numCustomers = Integer.parseInt(args[0]);
      }
      
      //creating the Threads
      Customer[] customers = new Customer[numCustomers]; 
      for(int i = 0; i < numCustomers; i++) {
         customers[i] = new Customer("Customer " + Integer.toString(i), i, storeCapacity);
      }
      Manager manager = new Manager(numCustomers);
      Employee employee = new Employee(numSelfCheckOut, numCustomers);
      
      //Start Threads
      for(int i = 0; i < numCustomers; i++) {
         customers[i].start();
      }
      manager.start();
      employee.start();
   }
   
   public String getName() {
      return "Main Thread";
   }
   
   public void msg(String m) {
      System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
   }
   
}

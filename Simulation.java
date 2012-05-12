import java.util.*;
import java.io.*;

public class Simulation {
  private final static int TIME_TO_CROSS = 3;
  private final static int PREVENT_STARVATION = 0;
  
  public static void main(String argv[]) {
    System.out.println("Monkey simulator");
    
    Simulation sim = new Simulation();
    
    sim.run();
  }
  
  public Simulation() {
    // nothing to do
  }
  
  public void run() {
    /**
     The set of ALL baboons in the simulation, though once we start running through the simulation, it will be those that remain
    */
    ArrayList<Baboon> baboons = new ArrayList<Baboon>();
    /**
     Which baboons are currently queued to cross but possibly blocked by a semaphore?
    */
    ArrayList<Baboon> wantToCross = new ArrayList<Baboon>();
    /**
     Baboons currently crossing on the wire right now
    */
    ArrayList<Baboon> crossing = new ArrayList<Baboon>();
    /**
     Baboons that have completed their crossing
    */
    ArrayList<Baboon> crossed = new ArrayList<Baboon>();
    
    try {
      BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
      String buffer = "";
      Baboon baboon = null;
      
      while(true) {
        baboon = new Baboon();
        
        System.out.print("Enter a numeric identifier for this monkey: ");
        buffer = in.readLine();
        
        /**
        If we get an EOF, break out of the while loop, we're done getting monkey information
        */
        if (buffer == null) {
          break;
        }
        
        baboon.setIdentifier(Integer.parseInt(buffer));
        
        System.out.print("Enter the direction of the baboon (" + Baboon.WEST + " = West, " + Baboon.EAST + " = East): ");
        buffer = in.readLine();
        
        if (buffer == null) {
          break;
        }
        
        baboon.setDirection(Integer.parseInt(buffer));
        
        System.out.print("Enter the time slot the baboon should attempt to cross at: ");
        buffer = in.readLine();
        
        if (buffer == null) {
          break;
        }
        
        baboon.setTimeSlot(Integer.parseInt(buffer));
        
        baboons.add(baboon);
        baboon = null;
      }
    } catch (Exception e) {
      System.err.println("We encountered a problem, quitting..." + e);
      System.exit(-1);
    }
    
    System.out.println("\n\nRunning simulation now....");
    
    /**
     Order the baboons into their crossing order
    */
    Collections.sort(baboons, new Comparator<Baboon>() {
      public int compare(Baboon b1, Baboon b2) {
        return (b1.getTimeSlot() <= b2.getTimeSlot() ? 0 : 1);
      }
    });
    
    /**
     Semaphores
    */
    int eastBound = 0;
    int westBound = 0;
    int timer = 0;
    
    /**
     Who we're currently looking at
    */
    Baboon baboon = null;
    
    /**
     Continue to loop while there are still baboons to cross
    */
    while (baboons.size() > 0 || wantToCross.size() > 0 || crossing.size() > 0) {
      /**
       Check if we have any queued up baboons that can go now, otherwise we'll do a new one
      */
      if (wantToCross.size() > 0) {
        baboon = wantToCross.remove(0);
      }
      else if (baboons.size() > 0 && baboons.get(0).getTimeSlot() <= timer) {
        baboon = baboons.remove(0);
      }
      
      if (baboon != null) {
        if (baboon.getDirection() == Baboon.EAST) {
          /**
           The coast is clear, we can head east
           */ 
          if (westBound == 0) {
            eastBound++;
            
            System.out.println("Baboon " + baboon.getIdentifier() + " has begun crossing " + (baboon.getDirection() == Baboon.EAST ? "East" : "West") + " bound");
            
            baboon.setTimeSlot(timer);
            crossing.add(baboon);
          } else {
            System.out.println("Baboon " + baboon.getIdentifier() + " wants to cross " + (baboon.getDirection() == Baboon.EAST ? "East" : "West") + " bound but can't");
            if (PREVENT_STARVATION == 0) {
              wantToCross.add(baboon);
            }
          }
        } else if (baboon.getDirection() == Baboon.WEST) {
          /**
           The coast is clear, we can head west
          */
          if (eastBound == 0) {
            westBound++;
            
            System.out.println("Baboon " + baboon.getIdentifier() + " has begun crossing " + (baboon.getDirection() == Baboon.EAST ? "East" : "West") + " bound");
            
            baboon.setTimeSlot(timer);
            crossing.add(baboon);
          } else {
            System.out.println("Baboon " + baboon.getIdentifier() + " wants to cross " + (baboon.getDirection() == Baboon.EAST ? "East" : "West") + " bound but can't");
            if (PREVENT_STARVATION == 0) {
              wantToCross.add(baboon);
            }
          }
        }
      }
      
      /**
       Check for baboons that finished crossing
      */
      for (int i = 0; i < crossing.size(); i++) {
        if ((crossing.get(i).getTimeSlot() + TIME_TO_CROSS) <= timer) {
          System.out.println("Baboon " + crossing.get(i).getIdentifier() + " has finished crossing from the " + (crossing.get(i).getDirection() == Baboon.EAST ? "East" : "West") + " bound direction at time " + timer);
          
          if (crossing.get(i).getDirection() == Baboon.EAST) {
            eastBound--;
          } else if (crossing.get(i).getDirection() == Baboon.WEST) {
            westBound--;
          }
          
          crossing.remove(i);
          
          i--;
        }
      }
      
      timer++;
      baboon = null;
      
      /**
       Make sure the waiting to cross list is sorted
      */
      Collections.sort(wantToCross, new Comparator<Baboon>() {
        public int compare(Baboon b1, Baboon b2) {
          return (b1.getTimeSlot() <= b2.getTimeSlot() ? 0 : 1);
        }
      });
    }
  }
}
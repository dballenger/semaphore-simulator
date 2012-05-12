public class Baboon {
  private int identifier;
  /**
   Direction the monkey wants to go
   
   1 = left
   2 = right
  */
  private int direction;
  
  final public static int WEST = 1;
  final public static int EAST = 2;
  
  /**
   What time slot to start attempting to cross at
  */
  private int time_slot;
  
  /**
   Has the baboon crossed?
  */
  private boolean crossed;
  
  public Baboon() {
    this.crossed = false;
  }
  
  public Baboon(int identifier, int direction, int time_slot) {
    this.identifier = identifier;
    this.direction = direction;
    this.time_slot = time_slot;
    this.crossed = false;
  }
  
  public void setIdentifier(int identifier) {
    this.identifier = identifier;
  }
  
  public void setDirection(int direction) {
    this.direction = direction;
  }
  
  public void setTimeSlot(int time_slot) {
    this.time_slot = time_slot;
  }
  
  public int getIdentifier() {
    return this.identifier;
  }
  
  public int getDirection() {
    return this.direction;
  }
  
  public int getTimeSlot() {
    return this.time_slot;
  }
  
  public boolean getCrossingStatus() {
    return this.crossed;
  }
}
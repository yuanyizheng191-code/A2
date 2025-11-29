import java.util.*;
import java.io.*;

public class Ride implements RideInterface {
    private String rideName;
    private int maxRider;
    private Employee operator;
    private int numOfCycles;
    
    // Waiting queue
    private Queue<Visitor> waitingLine;
    // Ride history
    private LinkedList<Visitor> rideHistory;

    // Default constructor
    public Ride() {
        this.rideName = "Unknown Ride";
        this.maxRider = 2;
        this.operator = null;
        this.numOfCycles = 0;
        this.waitingLine = new LinkedList<>();
        this.rideHistory = new LinkedList<>();
    }

    // Parameterized constructor
    public Ride(String rideName, int maxRider, Employee operator) {
        this.rideName = rideName;
        this.maxRider = maxRider;
        this.operator = operator;
        this.numOfCycles = 0;
        this.waitingLine = new LinkedList<>();
        this.rideHistory = new LinkedList<>();
    }

    // Getter and Setter methods
    public String getRideName() {
        return rideName;
    }

    public void setRideName(String rideName) {
        this.rideName = rideName;
    }

    public int getMaxRider() {
        return maxRider;
    }

    public void setMaxRider(int maxRider) {
        this.maxRider = maxRider;
    }

    public Employee getOperator() {
        return operator;
    }

    public void setOperator(Employee operator) {
        this.operator = operator;
    }

    public int getNumOfCycles() {
        return numOfCycles;
    }

    // Part 3: Queue operation methods
    @Override
    public void addVisitorToQueue(Visitor visitor) {
        if (visitor != null) {
            waitingLine.add(visitor);
            System.out.println("✓ " + visitor.getName() + " has joined " + rideName + " waiting queue");
        } else {
            System.out.println("✗ Cannot add null visitor to queue");
        }
    }

    @Override
    public void removeVisitorFromQueue() {
        if (!waitingLine.isEmpty()) {
            Visitor removed = waitingLine.poll();
            System.out.println("✓ " + removed.getName() + " has been removed from waiting queue");
        } else {
            System.out.println("✗ Waiting queue is empty, cannot remove visitor");
        }
    }

    @Override
    public void printQueue() {
        if (waitingLine.isEmpty()) {
            System.out.println(rideName + " waiting queue is empty");
            return;
        }
        
        System.out.println("=== " + rideName + " Waiting Queue ===");
        int position = 1;
        for (Visitor visitor : waitingLine) {
            System.out.println(position + ". " + visitor);
            position++;
        }
    }

    // Part 4A: History operation methods
    @Override
    public void addVisitorToHistory(Visitor visitor) {
        if (visitor != null) {
            rideHistory.add(visitor);
            System.out.println("✓ " + visitor.getName() + " has been added to ride history");
        } else {
            System.out.println("✗ Cannot add null visitor to history");
        }
    }

    @Override
    public boolean checkVisitorFromHistory(Visitor visitor) {
        if (visitor == null) return false;
        
        boolean found = rideHistory.contains(visitor);
        System.out.println(found ? 
            "✓ " + visitor.getName() + " is in ride history" : 
            "✗ " + visitor.getName() + " is not in ride history");
        return found;
    }

    @Override
    public int numberOfVisitors() {
        int count = rideHistory.size();
        System.out.println("Number of visitors in ride history: " + count);
        return count;
    }

    @Override
    public void printRideHistory() {
        if (rideHistory.isEmpty()) {
            System.out.println(rideName + " ride history is empty");
            return;
        }
        
        System.out.println("=== " + rideName + " Ride History ===");
        Iterator<Visitor> iterator = rideHistory.iterator();
        int position = 1;
        while (iterator.hasNext()) {
            System.out.println(position + ". " + iterator.next());
            position++;
        }
    }

    // Part 4B: Sorting method
    public void sortRideHistory(Comparator<Visitor> comparator) {
        Collections.sort(rideHistory, comparator);
        System.out.println("✓ Ride history has been sorted");
    }

    // Part 5: Run ride cycle
    @Override
    public void runOneCycle() {
        // Check if there's an operator
        if (operator == null) {
            System.out.println("✗ Cannot run " + rideName + " - No operator assigned");
            return;
        }
        
        // Check if waiting queue is empty
        if (waitingLine.isEmpty()) {
            System.out.println("✗ Cannot run " + rideName + " - Waiting queue is empty");
            return;
        }
        
        System.out.println("=== Running " + rideName + " One Cycle ===");
        System.out.println("Operator: " + operator.getName());
        
        int ridersThisCycle = 0;
        // Remove up to maxRider visitors from queue
        while (ridersThisCycle < maxRider && !waitingLine.isEmpty()) {
            Visitor rider = waitingLine.poll();
            addVisitorToHistory(rider);
            ridersThisCycle++;
            System.out.println("✓ " + rider.getName() + " is riding " + rideName);
        }
        
        numOfCycles++;
        System.out.println("Number of riders this cycle: " + ridersThisCycle);
        System.out.println("Total number of cycles run: " + numOfCycles);
    }

    // Part 6: Export ride history to file
    public void exportRideHistory(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Visitor visitor : rideHistory) {
                // Format: name,age,id,ticketNumber,membershipLevel
                writer.println(visitor.getName() + "," + 
                              visitor.getAge() + "," + 
                              visitor.getId() + "," + 
                              visitor.getTicketNumber() + "," + 
                              visitor.getMembershipLevel());
            }
            System.out.println("✓ Ride history has been exported to file: " + filename);
        } catch (IOException e) {
            System.out.println("✗ Error exporting file: " + e.getMessage());
        }
    }

    // Part 7: Import ride history from file
    public void importRideHistory(String filename) {
        int importedCount = 0;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    String name = parts[0];
                    int age = Integer.parseInt(parts[1]);
                    String id = parts[2];
                    String ticketNumber = parts[3];
                    String membershipLevel = parts[4];
                    
                    Visitor visitor = new Visitor(name, age, id, ticketNumber, membershipLevel);
                    rideHistory.add(visitor);
                    importedCount++;
                }
            }
            System.out.println("✓ Imported " + importedCount + " visitors to ride history from file");
        } catch (IOException e) {
            System.out.println("✗ Error importing file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("✗ File format error: " + e.getMessage());
        }
    }
}
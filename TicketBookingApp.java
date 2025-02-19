import java.util.*;

class TicketBookingSystem {
    private static final int TOTAL_SEATS = 10;
    private static boolean[] seats = new boolean[TOTAL_SEATS];
    private static final Object lock = new Object();

    public static void bookSeat(String customerName, int seatNumber) {
        synchronized (lock) {
            if (seatNumber < 0 || seatNumber >= TOTAL_SEATS) {
                System.out.println(customerName + " attempted to book an invalid seat.");
                return;
            }
            if (!seats[seatNumber]) {
                seats[seatNumber] = true;
                System.out.println(customerName + " successfully booked seat " + seatNumber);
            } else {
                System.out.println(customerName + " attempted to book seat " + seatNumber + " but it was already taken.");
            }
        }
    }
}

class BookingThread extends Thread {
    private String customerName;
    private int seatNumber;
    
    public BookingThread(String customerName, int seatNumber, int priority) {
        this.customerName = customerName;
        this.seatNumber = seatNumber;
        setPriority(priority);
    }

    @Override
    public void run() {
        TicketBookingSystem.bookSeat(customerName, seatNumber);
    }
}

public class TicketBookingApp {
    public static void main(String[] args) {
        List<Thread> bookingThreads = new ArrayList<>();
        
        // Simulating VIP customers with higher priority
        bookingThreads.add(new BookingThread("VIP_Alice", 2, Thread.MAX_PRIORITY));
        bookingThreads.add(new BookingThread("VIP_Bob", 3, Thread.MAX_PRIORITY));
        
        // Normal customers with normal priority
        bookingThreads.add(new BookingThread("Charlie", 2, Thread.NORM_PRIORITY));
        bookingThreads.add(new BookingThread("David", 3, Thread.NORM_PRIORITY));
        bookingThreads.add(new BookingThread("Eve", 4, Thread.MIN_PRIORITY));
        
        // Start all threads
        for (Thread t : bookingThreads) {
            t.start();
        }
        
        // Wait for all threads to finish
        for (Thread t : bookingThreads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

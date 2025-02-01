
import java.util.*;

class Passenger {

    private String name;
    private int age;
    private String gender;
    private String contactNumber;

    public Passenger(String name, int age, String gender, String contactNumber) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.contactNumber = contactNumber;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getContactNumber() {
        return contactNumber;
    }
}

class Ticket {

    private static int counter = 1000;
    private final int pnrNumber;
    private Passenger passenger;
    private String coachType;
    private int seatNumber;
    private boolean isWaitlisted;
    private int waitlistNumber;

    public Ticket(Passenger passenger, String coachType) {
        this.pnrNumber = ++counter;
        this.passenger = passenger;
        this.coachType = coachType;
    }

    public int getPnrNumber() {
        return pnrNumber;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public String getCoachType() {
        return coachType;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public boolean isWaitlisted() {
        return isWaitlisted;
    }

    public void setWaitlisted(boolean waitlisted) {
        isWaitlisted = waitlisted;
    }

    public int getWaitlistNumber() {
        return waitlistNumber;
    }

    public void setWaitlistNumber(int waitlistNumber) {
        this.waitlistNumber = waitlistNumber;
    }
}

class Coach {

    private String type;
    private boolean[] seats;
    private int waitlistCounter;
    private static final int TOTAL_SEATS = 60;
    private static final int MAX_WAITLIST = 10;
    private Map<Integer, Ticket> bookedTickets;
    private Queue<Ticket> waitlistedTickets;

    public Coach(String type) {
        this.type = type;
        this.seats = new boolean[TOTAL_SEATS];
        this.waitlistCounter = 0;
        this.bookedTickets = new HashMap<>();
        this.waitlistedTickets = new LinkedList<>();
    }

    public Ticket bookTicket(Passenger passenger) {
        Ticket ticket = new Ticket(passenger, type);
        for (int i = 0; i < TOTAL_SEATS; i++) {
            if (!seats[i]) {
                seats[i] = true;
                ticket.setSeatNumber(i + 1);
                bookedTickets.put(ticket.getPnrNumber(), ticket);
                return ticket;
            }
        }
        if (waitlistCounter < MAX_WAITLIST) {
            ticket.setWaitlisted(true);
            ticket.setWaitlistNumber(++waitlistCounter);
            waitlistedTickets.add(ticket);
            bookedTickets.put(ticket.getPnrNumber(), ticket);
            return ticket;
        }
        return null;
    }

    public boolean cancelTicket(int pnrNumber) {
        Ticket ticket = bookedTickets.get(pnrNumber);
        if (ticket == null) {
            return false;
        }

        bookedTickets.remove(pnrNumber);

        if (ticket.isWaitlisted()) {
            waitlistedTickets.remove(ticket);
            waitlistCounter--;
            int newWaitlistNum = 1;
            for (Ticket waitlistedTicket : waitlistedTickets) {
                waitlistedTicket.setWaitlistNumber(newWaitlistNum++);
            }
        } else {
            seats[ticket.getSeatNumber() - 1] = false;
            if (!waitlistedTickets.isEmpty()) {
                Ticket waitlistedTicket = waitlistedTickets.poll();
                waitlistedTicket.setWaitlisted(false);
                waitlistedTicket.setSeatNumber(ticket.getSeatNumber());
                waitlistCounter--;
            }
        }
        return true;
    }

    public String getStatus(int pnrNumber) {
        Ticket ticket = bookedTickets.get(pnrNumber);
        if (ticket == null) {
            return "Ticket not found";
        }
        if (ticket.isWaitlisted()) {
            return String.format("PNR: %d - Waitlisted (WL/%d)",
                    pnrNumber, ticket.getWaitlistNumber());
        } else {
            return String.format("PNR: %d - Confirmed (Seat: %d)",
                    pnrNumber, ticket.getSeatNumber());
        }
    }

    public String prepareChart() {
        StringBuilder chart = new StringBuilder();
        chart.append("Booking Chart for ").append(type).append(" Coach\n");
        chart.append("Confirmed Bookings:\n");

        bookedTickets.values().stream()
                .filter(ticket -> !ticket.isWaitlisted())
                .forEach(ticket -> chart.append(String.format(
                "Seat %d: %s (PNR: %d)\n",
                ticket.getSeatNumber(),
                ticket.getPassenger().getName(),
                ticket.getPnrNumber()
        )));

        if (!waitlistedTickets.isEmpty()) {
            chart.append("\nWaitlisted Passengers:\n");
            waitlistedTickets.forEach(ticket -> chart.append(String.format(
                    "WL/%d: %s (PNR: %d)\n",
                    ticket.getWaitlistNumber(),
                    ticket.getPassenger().getName(),
                    ticket.getPnrNumber()
            )));
        }

        return chart.toString();
    }

    public int getAvailableSeats() {
        int count = 0;
        for (boolean seat : seats) {
            if (!seat) {
                count++;
            }
        }
        return count;
    }

    public int getAvailableWaitlist() {
        return MAX_WAITLIST - waitlistCounter;
    }
}

public class RailwayReservationSystem {

    private Coach acCoach;
    private Coach nonAcCoach;
    private Coach seaterCoach;

    public RailwayReservationSystem() {
        this.acCoach = new Coach("AC");
        this.nonAcCoach = new Coach("Non-AC");
        this.seaterCoach = new Coach("Seater");
    }

    public Ticket bookTicket(String coachType, Passenger passenger) {
        Coach selectedCoach = getCoach(coachType);
        if (selectedCoach == null) {
            throw new IllegalArgumentException("Invalid coach type");
        }
        return selectedCoach.bookTicket(passenger);
    }

    public boolean cancelTicket(String coachType, int pnrNumber) {
        Coach selectedCoach = getCoach(coachType);
        if (selectedCoach == null) {
            throw new IllegalArgumentException("Invalid coach type");
        }
        return selectedCoach.cancelTicket(pnrNumber);
    }

    public String getStatus(String coachType, int pnrNumber) {
        Coach selectedCoach = getCoach(coachType);
        if (selectedCoach == null) {
            throw new IllegalArgumentException("Invalid coach type");
        }
        return selectedCoach.getStatus(pnrNumber);
    }

    public String prepareChart(String coachType) {
        Coach selectedCoach = getCoach(coachType);
        if (selectedCoach == null) {
            throw new IllegalArgumentException("Invalid coach type");
        }
        return selectedCoach.prepareChart();
    }

    private Coach getCoach(String coachType) {
        switch (coachType.toUpperCase()) {
            case "AC":
                return acCoach;
            case "NON-AC":
                return nonAcCoach;
            case "SEATER":
                return seaterCoach;
            default:
                return null;
        }
    }

    public static void main(String[] args) {
        RailwayReservationSystem system = new RailwayReservationSystem();
        Passenger passenger1 = new Passenger("John Doe", 30, "Male", "1234567890");
        Ticket ticket = system.bookTicket("AC", passenger1);
        if (ticket != null) {
            System.out.println("Booking successful! PNR: " + ticket.getPnrNumber());
            System.out.println(system.getStatus("AC", ticket.getPnrNumber()));
            if (system.cancelTicket("AC", ticket.getPnrNumber())) {
                System.out.println("Ticket cancelled successfully!");
            }
        }
        System.out.println(system.prepareChart("AC"));
    }
}

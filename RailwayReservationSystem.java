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
    private static final int TOTAL_SEATS = 5;  // Reduced for testing
    private static final int MAX_WAITLIST = 3;
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
        if (ticket == null) return false;

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
        if (ticket == null) return "Ticket not found";

        if (ticket.isWaitlisted()) {
            return "PNR: " + pnrNumber + " - Waitlisted (WL/" + ticket.getWaitlistNumber() + ")";
        } else {
            return "PNR: " + pnrNumber + " - Confirmed (Seat: " + ticket.getSeatNumber() + ")";
        }
    }

    public String prepareChart() {
        StringBuilder chart = new StringBuilder();
        chart.append("Booking Chart for ").append(type).append(" Coach\n");

        chart.append("Confirmed Bookings:\n");
        bookedTickets.values().stream()
                .filter(ticket -> !ticket.isWaitlisted())
                .forEach(ticket -> chart.append("Seat " + ticket.getSeatNumber() + ": " + ticket.getPassenger().getName() +
                        " (PNR: " + ticket.getPnrNumber() + ")\n"));

        if (!waitlistedTickets.isEmpty()) {
            chart.append("\nWaitlisted Passengers:\n");
            waitlistedTickets.forEach(ticket -> chart.append("WL/" + ticket.getWaitlistNumber() + ": " +
                    ticket.getPassenger().getName() + " (PNR: " + ticket.getPnrNumber() + ")\n"));
        }
        return chart.toString();
    }
}

public class RailwayReservationSystem {
    private Coach acCoach, nonAcCoach, seaterCoach;

    public RailwayReservationSystem() {
        this.acCoach = new Coach("AC");
        this.nonAcCoach = new Coach("Non-AC");
        this.seaterCoach = new Coach("Seater");
    }

    public Ticket bookTicket(String coachType, Passenger passenger) {
        Coach coach = getCoach(coachType);
        return coach != null ? coach.bookTicket(passenger) : null;
    }

    public boolean cancelTicket(String coachType, int pnrNumber) {
        Coach coach = getCoach(coachType);
        return coach != null && coach.cancelTicket(pnrNumber);
    }

    public String getStatus(String coachType, int pnrNumber) {
        Coach coach = getCoach(coachType);
        return coach != null ? coach.getStatus(pnrNumber) : "Invalid coach type!";
    }

    public String prepareChart(String coachType) {
        Coach coach = getCoach(coachType);
        return coach != null ? coach.prepareChart() : "Invalid coach type!";
    }

    private Coach getCoach(String coachType) {
        return switch (coachType.toUpperCase()) {
            case "AC" -> acCoach;
            case "NON-AC" -> nonAcCoach;
            case "SEATER" -> seaterCoach;
            default -> null;
        };
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        RailwayReservationSystem system = new RailwayReservationSystem();

        while (true) {
            System.out.println("\n--- Railway Reservation System ---");
            System.out.println("1. Book Ticket");
            System.out.println("2. Cancel Ticket");
            System.out.println("3. Check Ticket Status");
            System.out.println("4. Display Coach Chart");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  

            switch (choice) {
                case 1:
                    System.out.print("Enter passenger name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter age: ");
                    int age = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter gender: ");
                    String gender = scanner.nextLine();
                    System.out.print("Enter contact number: ");
                    String contact = scanner.nextLine();
                    System.out.print("Enter coach type (AC/NON-AC/SEATER): ");
                    String coachType = scanner.nextLine().toUpperCase();

                    Passenger passenger = new Passenger(name, age, gender, contact);
                    Ticket ticket = system.bookTicket(coachType, passenger);

                    System.out.println(ticket != null ? "PNR: " + ticket.getPnrNumber() : "Booking failed! No seats available.");
                    break;
                case 2:
                    System.out.print("Enter PNR: ");
                    int pnr = scanner.nextInt();
                    System.out.println(system.cancelTicket("AC", pnr) ? "Ticket Cancelled" : "Invalid PNR!");
                    break;
                case 3:
                    System.out.print("Enter PNR: ");
                    int statusPnr = scanner.nextInt();
                    System.out.println(system.getStatus("AC", statusPnr));
                    break;
                case 4:
                    System.out.println(system.prepareChart("AC"));
                    break;
                case 5:
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}

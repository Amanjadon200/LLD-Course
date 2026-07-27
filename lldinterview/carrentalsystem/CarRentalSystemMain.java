package lldinterview.carrentalsystem;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarRentalSystemMain {
    public static void main(String[] args) {
        // Create a rental store
        RentalStore rentalStore = new RentalStore();
        rentalStore.addVehicle(VehicleFactory.createVehicle(VehicleType.ECONOMY, "ABC123", "Toyota Corolla", 50.0));
        rentalStore.addVehicle(VehicleFactory.createVehicle(VehicleType.ECONOMY, "XYZ789", "Honda Civic", 60.0));

        // Create a payment processor with a credit card payment strategy
        PaymentStrategy paymentStrategy = new CreditCardPayment();
        PaymentProcessor paymentProcessor = new PaymentProcessor(paymentStrategy);

        // Create a booking manager
        BookingManager bookingManager = new BookingManager();

        // Create the rental system
        RentalSystem rentalSystem = new RentalSystem(rentalStore, paymentProcessor, bookingManager);

        // Search for a vehicle
        List<Vehicle> vehicles = rentalSystem.searchVehicle("New York", VehicleType.ECONOMY);
        if (!vehicles.isEmpty()) {
            Vehicle vehicle = vehicles.get(0);
            System.out.println("Found vehicle: " + vehicle.getModel() + " with registration number: "
                    + vehicle.getRegistrationNumber());
            // Create a booking for the vehicle
            LocalDate startDate = LocalDate.of(2024, 6, 1);
            LocalDate endDate = LocalDate.of(2024, 6, 5);
            Booking booking = rentalSystem.createBooking(vehicle.getRegistrationNumber(), startDate, endDate, "user123");
            if (booking != null) {
                System.out.println("Booking created with ID: " + booking.getBookingId());
                // Process payment for the booking
                double amount = vehicle.calculateRentalFee(5); // Assuming 5 days of rental
                rentalSystem.processPayment(booking, amount);
                System.out.println("Payment processed for booking ID: " + booking.getBookingId());
            } else {
                System.out.println("Vehicle is not available for the selected dates.");
            }
        } else {
            System.out.println("No available vehicles found.");
        }
    }
}

enum VehicleType {
    SEDAN, BIKE, SUV, ECONOMY, LUXURY
}

enum VehicleStatus {
    AVAILABLE, BOOKED
}

abstract class Vehicle {
    private String registrationNumber;
    private String model;
    private VehicleType type;
    private double baseRentalPrice;

    // Constructor
    public Vehicle(String registrationNumber, String model, VehicleType type,
            double baseRentalPrice) {
        this.registrationNumber = registrationNumber;
        this.model = model;
        this.type = type;
        this.baseRentalPrice = baseRentalPrice;
    }

    // Abstract method for calculating rental fee
    public abstract double calculateRentalFee(int days);

    // Getters and setters
    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getModel() {
        return model;
    }

    public VehicleType getType() {
        return type;
    }

    public double getBaseRentalPrice(int days) {
        return baseRentalPrice*days;
    }
}
class User {
    private String userId;
    private String name;
    private String email;

    public User(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }
    public String getUserId() {
        return userId;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    // Getters and setters can be defined here
}
class VehicleFactory {
    public static Vehicle createVehicle(VehicleType vehicleType, String registrationNumber, String model,
            double baseRentalPrice) {
        switch (vehicleType) {
            case ECONOMY:
                return new EconomyVehicle(registrationNumber, model, vehicleType, baseRentalPrice);
            default:
                throw new IllegalArgumentException("Unsupported vehicle type: " + vehicleType);
        }
    }
}

class EconomyVehicle extends Vehicle {
    public EconomyVehicle(String registrationNumber, String model, VehicleType vehicleType, double baseRentalPrice) {
        super(registrationNumber, model, vehicleType, baseRentalPrice);
    }

    public double calculateRentalFee(int days) {
        return this.getBaseRentalPrice(days);
    }
}

class Booking {
    private String bookingId;
    private String registrationNumber;
    private LocalDate startDate;
    private LocalDate endDate;
    private BookingStatus bookingStatus;
    private String userId;

    public Booking(String bookingId, String registrationNumber, LocalDate startDate, LocalDate endDate,
            BookingStatus bookingStatus, String userId) {
        this.bookingId = bookingId;
        this.registrationNumber = registrationNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.bookingStatus = bookingStatus;
        this.userId = userId;
    }
    public String getRegistrationNumber() {
        return registrationNumber;
    }
    public LocalDate getStartDate() {
        return startDate;
    }
    public LocalDate getEndDate() {
        return endDate;
    }
    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }
    public String getBookingId() {
        return bookingId;
    }
    public String getUserId() {
        return userId;
    }
    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }
}

class BookingManager {
    private List<Booking> booking;

    public BookingManager() {
        this.booking = new ArrayList<>();
    }

    public void addBooking(String bookingId, Booking booking) {
        this.booking.add(booking);
    }

    public boolean checkAvailablity(String registrationNumber, LocalDate startDate, LocalDate endDate) {
        for (Booking book : booking) {
            if (book.getRegistrationNumber().equals(registrationNumber)) {
                if ((startDate.isBefore(book.getEndDate()) && endDate.isAfter(book.getStartDate()))) {
                    return false; // Overlapping booking found
                }
            }
        }
        return true;
    }
}
// class Payment{
// String id;

// }
interface PaymentStrategy {
    void pay(double amount); // abstract amd public
}

class CreditCardPayment implements PaymentStrategy {
    public void pay(double amount) {
        System.out.println("credir card payment processing");
    }
}

class PaymentProcessor {
    private final PaymentStrategy paymentStrategy;

    public PaymentProcessor(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    public void processPayment(double amount) {
        paymentStrategy.pay(amount);
    }
}

class RentalSystem {
    RentalStore rentalStore;
    PaymentProcessor paymentProcessor;
    BookingManager bookingManager;

    public RentalSystem(RentalStore rentalStore, PaymentProcessor paymentProcessor, BookingManager bookingManager) {
        this.rentalStore = rentalStore;
        this.paymentProcessor = paymentProcessor;
        this.bookingManager = bookingManager;
    }

    public List<Vehicle> searchVehicle(String city, VehicleType type) {
        return rentalStore.searchVehicle(city, type);
    }

    public Booking createBooking(String registationNumber, LocalDate startDate, LocalDate endDate, String userId) {
        boolean available = bookingManager.checkAvailablity(registationNumber, startDate, endDate);
        if (available) {
            Booking booking = new Booking("BK001", registationNumber, startDate, endDate,
                    BookingStatus.PENDING_PAYMENT, userId);
            bookingManager.addBooking(booking.getBookingId(), booking);
            return booking;
        }
        return null;
    }

    public void processPayment(Booking booking, double amount) {
        if (booking.getBookingStatus()  != BookingStatus.PENDING_PAYMENT) {
            throw new IllegalStateException("Booking is not in pending payment state");
        }
        paymentProcessor.processPayment(amount);
        booking.setBookingStatus(BookingStatus.CONFIRMED);
    }

    public void cancelBooking(Booking booking) {
        booking.setBookingStatus(BookingStatus.CANCELLED);
    }
}

enum BookingStatus {
    PENDING_PAYMENT,
    CONFIRMED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED,
    EXPIRED
}

class RentalStore {
    Map<String, Vehicle> vehicleMap;
    // Map<>
    Location location;

    public RentalStore() {
        this.vehicleMap = new HashMap<>();
    }

    public void addVehicle(Vehicle vehicle) {
        vehicleMap.putIfAbsent(vehicle.getRegistrationNumber(), vehicle);
    }

    public List<Vehicle> searchVehicle(String city, VehicleType vehicleType) {
        List<Vehicle> availableVehicles = new ArrayList<>();
        for (Vehicle vehicle : vehicleMap.values()) {
            if (vehicle.getType() == vehicleType) {
                availableVehicles.add(vehicle);
            }
        }
        return availableVehicles;
    }
}

class Location {
    private String address;
    private String city;
    private String state;
    private String zipCode;

    public Location(String address, String city, String state, String zipCode) {
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }
    // Getters and setters can be defined here
}

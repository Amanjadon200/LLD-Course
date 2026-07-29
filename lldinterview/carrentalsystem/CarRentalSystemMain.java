package lldinterview.carrentalsystem;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;

public class CarRentalSystemMain {

    public static void main(String[] args) {
        // Create a rental store
        RentalStore rentalStore = new RentalStore(new Location("123 Main St", "New York", "NY", "10001"));
        rentalStore.addVehicle(VehicleFactory.createVehicle(VehicleType.ECONOMY, "ABC123", "Toyota Corolla", 50.0, new Location("123 Main St", "New York", "NY", "10001")));
        rentalStore.addVehicle(VehicleFactory.createVehicle(VehicleType.ECONOMY, "XYZ789", "Honda Civic", 60.0, new Location("456 Oak Ave", "New York", "NY", "10002")));
        // Create a payment processor with a credit card payment strategy
        PaymentStrategy paymentStrategy = new CreditCardPayment();
        PaymentProcessor paymentProcessor = new PaymentProcessor(paymentStrategy);
        // Create a booking manager
        BookingManager bookingManager = new BookingManager();

        // Create the rental system
        RentalSystem rentalSystem = new RentalSystem(rentalStore, paymentProcessor, bookingManager);

        // Search for a vehicle
        VehicleSearchService vehicleSearchService = new VehicleSearchService(bookingManager, rentalStore);
        List<Vehicle> vehicles = vehicleSearchService.searchAvailableVehicles("New York", VehicleType.ECONOMY, LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 5));
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
    private Location location;

    // Constructor
    public Vehicle(String registrationNumber, String model, VehicleType type,
            double baseRentalPrice, Location location) {
        this.registrationNumber = registrationNumber;
        this.model = model;
        this.type = type;
        this.baseRentalPrice = baseRentalPrice;
        this.location = location;
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
        return baseRentalPrice * days;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
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
            double baseRentalPrice, Location location) {
        switch (vehicleType) {
            case ECONOMY:
                return new EconomyVehicle(registrationNumber, model, vehicleType, baseRentalPrice, location);
            default:
                throw new IllegalArgumentException("Unsupported vehicle type: " + vehicleType);
        }
    }
}

class EconomyVehicle extends Vehicle {

    public EconomyVehicle(String registrationNumber, String model, VehicleType vehicleType, double baseRentalPrice, Location location) {
        super(registrationNumber, model, vehicleType, baseRentalPrice, location);
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

    private Map<String, List<Booking>> booking;
    private AtomicInteger counter = new AtomicInteger(1);
    Map<String, Lock> vehicleLocks;

    public BookingManager() {
        this.booking = new ConcurrentHashMap<>();
        this.vehicleLocks = new ConcurrentHashMap<>();
    }

    public Booking createBooking(String registrationNumber, LocalDate startDate, LocalDate endDate, String userId) {
        Lock lock = vehicleLocks.computeIfAbsent(registrationNumber, k -> new java.util.concurrent.locks.ReentrantLock());
        lock.lock();
        try {
            boolean available = checkAvailablity(registrationNumber, startDate, endDate);
            if (available) {
                Booking booking = new Booking("BK" + counter.getAndIncrement(), registrationNumber, startDate, endDate, BookingStatus.PENDING_PAYMENT, userId);
                this.booking.computeIfAbsent(registrationNumber, k -> new ArrayList<>()).add(booking);
                return booking;
            }

        } finally {
            lock.unlock();
        }
        return null;
    }

    public boolean checkAvailablity(String registrationNumber, LocalDate startDate, LocalDate endDate) {
        List<Booking> bookingsForVehicle = booking.get(registrationNumber);
        if (bookingsForVehicle != null) {
            for (Booking book : bookingsForVehicle) {
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

    public void processPayment(Booking booking, double amount) {
        if (booking.getBookingStatus() != BookingStatus.PENDING_PAYMENT) {
            throw new IllegalStateException("Booking is not in pending payment state");
        }
        paymentStrategy.pay(amount);
        booking.setBookingStatus(BookingStatus.CONFIRMED);
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

    public Booking createBooking(String registrationNumber, LocalDate startDate, LocalDate endDate, String userId) {
        return bookingManager.createBooking(registrationNumber, startDate, endDate, userId);
    }

    public void processPayment(Booking booking, double amount) {
        paymentProcessor.processPayment(booking, amount);
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

class VehicleSearchService {

    BookingManager bookingManager;
    RentalStore rentalStore;

    public VehicleSearchService(BookingManager bookingManager, RentalStore rentalStore) {
        this.bookingManager = bookingManager;
        this.rentalStore = rentalStore;
    }

    public List<Vehicle> searchAvailableVehicles(String city, VehicleType vehicleType, LocalDate startDate, LocalDate endDate) {
        List<Vehicle> availableVehicles = new ArrayList<>();
        List<Vehicle> vehiclesInCity = rentalStore.searchVehicle(city, vehicleType);
        for (Vehicle vehicle : vehiclesInCity) {
            if (bookingManager.checkAvailablity(vehicle.getRegistrationNumber(), startDate, endDate)) {
                availableVehicles.add(vehicle);
            }
        }
        return availableVehicles;
    }
} 

class RentalStore {

    Map<String, Vehicle> vehicleMap;
    // Map<>
    Location location;

    public RentalStore(Location location) {
        this.vehicleMap = new HashMap<>();
        this.location = location;
    }

    public void addVehicle(Vehicle vehicle) {
        vehicleMap.putIfAbsent(vehicle.getRegistrationNumber(), vehicle);
    }

    public List<Vehicle> searchVehicle(String city, VehicleType vehicleType) {
        List<Vehicle> availableVehicles = new ArrayList<>();
        for (Vehicle vehicle : vehicleMap.values()) {
            if (vehicle.getType() == vehicleType && vehicle.getLocation().getCity().equals(city)) { // Assuming all vehicles are available in the same city for simplicity
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
    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZipCode() {
        return zipCode;
    }
}

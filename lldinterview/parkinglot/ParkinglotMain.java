package lldinterview.parkinglot;

import java.util.ArrayList;
import java.util.List;

public class ParkinglotMain {
    public static void main(String[] args) {
        List<ParkingSlot> parkingSlotList = new ArrayList<>();
        ParkingSlot ps1 = ParkingSlotFactory.createFactory(Size.Small, VehicleType.Bike);
        ParkingSlot ps2 = ParkingSlotFactory.createFactory(Size.Large, VehicleType.Bike);
        ParkingSlot ps3 = ParkingSlotFactory.createFactory(Size.Medium, VehicleType.Bike);
        ParkingSlot ps4 = ParkingSlotFactory.createFactory(Size.Small, VehicleType.Car);
        ParkingSlot ps5 = ParkingSlotFactory.createFactory(Size.Large, VehicleType.Car);
        ParkingSlot ps6 = ParkingSlotFactory.createFactory(Size.Medium, VehicleType.Car);
        parkingSlotList.add(ps1);
        parkingSlotList.add(ps2);
        parkingSlotList.add(ps3);
        parkingSlotList.add(ps4);
        parkingSlotList.add(ps5);
        parkingSlotList.add(ps6);
        ParkingLot parkingLot = new ParkingLot(parkingSlotList, 9012);
        // entry of vehicle
        Vehicle vehicle1 = new Car(Size.Large);
        ParkingSlot parkingSlot=parkingLot.findParkingSlot(vehicle1);
        PaymentStrategy paymentStrategy=new CreditCardPayment();
        PaymentProcessor paymentProcessor=new PaymentProcessor(paymentStrategy);
        if(parkingSlot!=null){
            Plan plan =new BasicParkingPlan();
            // calculate parking fees
            int fees=parkingLot.calculateFee(vehicle1,plan,1);
            paymentProcessor.processPayment(fees);
            parkingSlot.reserveParking();

        }
        else{
            System.out.println("Slot is not available at this point please check later...");
        }
        
    }
}

/*
 * Requiremenets:
 * 1. desing a parking lot for differnt different type of vehicle ex - car bike
 * 2. calcualte parking fee according to vehicle type
 * 3. calculate fee on basis of plan like basic parking or premium parking
 * 4 system sholuld handle parking slot efficieltly on basisi of vehicle size
 * 5. allow multiple types of payment.
 */
/*
 * Entities:
 * 1. vehicle
 * 2. parking lot
 * 3. parking slot
 * 4. Payment
 */
class ParkingSlotFactory {
    public static ParkingSlot createFactory(Size size, VehicleType slotType) {
        switch (slotType) {
            case Car:
                return new ParkingSlot(size, VehicleType.Car, false);
            case Bike:
                return new ParkingSlot(size, VehicleType.Bike, false);
            default:
                return null;
        }
    }
}

class ParkingLot {
    private List<ParkingSlot> parkingSlot;
    private int parkingLotNumber;

    public ParkingLot(List<ParkingSlot> parkingSlot, int parkingLotNumber) {
        this.parkingSlot = parkingSlot;
        this.parkingLotNumber = parkingLotNumber;
    }

    public ParkingSlot findParkingSlot(Vehicle vehicle) {
        for (ParkingSlot slot : parkingSlot) {
            if (slot.getSlotType() == vehicle.getVehicleType()) {
                if (slot.getSize() == vehicle.getSize() && !slot.isOccupied()) {
                    return slot;
                }
            }
        }
        return null;
    }
    public int calculateFee(Vehicle vehicle,Plan plan, int duration) {
         int planPrice = plan.planPrice(vehicle.getVehicleType(), duration);
            return planPrice * duration;
    }
}

class ParkingSlot {
    private Size size;
    private VehicleType slotType;
    private boolean isOccupied;

    public ParkingSlot(Size size, VehicleType slotType, boolean isOccupied) {
        this.size = size;
        this.slotType = slotType;
        this.isOccupied = isOccupied;
    }
    public void reserveParking(){
        this.isOccupied=true;
    }
    public void freeParking(){
        this.isOccupied=false;
    }
    public boolean isOccupied() {
        return isOccupied;
    }
    public Size getSize() {
        return size;
    }
    public VehicleType getSlotType() {
        return slotType;
    }
}
// The compiler roughly converts
enum VehicleType {
    Car, Bike
}
// final class VehicleType {

//     public static final VehicleType Car =
//             new VehicleType("Car");

//     public static final VehicleType Bike =
//             new VehicleType("Bike");

//     private VehicleType(String name) {}

// }
// Enum constants are singleton instances created once by the JVM. Each constant like VehicleType.Car is a public static final object. Since there's only one instance of each constant, comparing references with == is correct, faster, and null-safe
enum Size{
    Small, Medium, Large
}
abstract class Vehicle {
    private Size size;
    private VehicleType vehicleType;

    public Vehicle(Size size, VehicleType vehicleType) {
        this.size = size;
        this.vehicleType = vehicleType;
    }
    public Size getSize() {
        return size;
    }
    public VehicleType getVehicleType() {
        return vehicleType;
    }
}

class Car extends Vehicle {
    public Car(Size size) {
        super(size, VehicleType.Car);
    }
}

class Bike extends Vehicle {
    public Bike(Size size) {
        super(size, VehicleType.Bike);
    }
}

interface Plan {
    int planPrice(VehicleType vehicleType, int duration);
}

class BasicParkingPlan implements Plan {
    public int planPrice(VehicleType vehicleType, int duration) {
        if (vehicleType==VehicleType.Car) {
            if (duration < 30) {
                return 5; // per minute
            } else if (duration >= 30 && duration <= 300) {
                return 8; // per minute
            } else {
                return -1;
            }
        } else if (vehicleType==VehicleType.Bike) {
            if (duration < 30) {
                return 2;// per minute
            } else if (duration >= 30 && duration <= 300) {
                return 5;// per minute
            } else {
                return -1;
            }
        }
        return -1;

    }
}

class PremiumParkingPlan implements Plan {
    public int planPrice(VehicleType vehicleType, int duration) {
        if (vehicleType==VehicleType.Car) {
            if (duration < 30) {
                return 150;
            } else if (duration >= 30 && duration <= 300) {
                return 200;
            } else {
                return -1;
            }
        } else if (vehicleType==VehicleType.Bike) {
            if (duration < 30) {
                return 50;
            } else if (duration >= 30 && duration <= 300) {
                return 100;
            } else {
                return -1;
            }
        }
        return -1;

    }
}
//PaymentProcessor field should be private
class PaymentProcessor{
    private final PaymentStrategy paymentStrategy;
    public PaymentProcessor(PaymentStrategy paymentStrategy){
        this.paymentStrategy=paymentStrategy;
    }
    public void processPayment(int amount){
        paymentStrategy.pay(amount);
    }
}
interface PaymentStrategy{
    void pay(int amount);
}
class CreditCardPayment implements PaymentStrategy{
    public void pay(int amount){
        System.out.println("CreditCardPayment Processing");
    }
}
class DebitCardPayment implements PaymentStrategy{
    public void pay(int amount){
        System.out.println("DebitCardPayment Processing");
    }
}
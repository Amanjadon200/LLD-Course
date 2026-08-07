package lldinterview.elevator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ElevatorMain {

}

class ElevatorController {
    List<Elevator> elevators;

    public ElevatorController(List<Elevator> elevators) {
        this.elevators = elevators;
    }

    public boolean requestElevator(int floor, RequestType type) {
        if (floor < 0 || floor > 10) {
            throw new IllegalArgumentException("Floor cannot be less than 0 or greater than 10");
        }
        Request request = new Request(floor, type);

        Elevator best = selectBestElevator(request);

        return best.addRequest(request);
    }

    private Elevator selectBestElevator(Request request) {

        Elevator best = findCommittedToFloor(request);

        if (best != null)
            return best;

        // best = findNearestIdle(request.getFloor());

        if (best != null)
            return best;
        return null;
        // return findNearest(request.getFloor());
    }

    private Elevator findCommittedToFloor(Request request) {

        int floor = request.getFloor();

        Direction direction = request.getType() == RequestType.PICKUP_UP ? Direction.UP : Direction.DOWN;

        Elevator nearest = null;

        int minDistance = Integer.MAX_VALUE;

        for (Elevator elevator : elevators) {

            if (elevator.getDirection() != direction)
                continue;

            if (direction == Direction.UP &&
                    elevator.getCurrentFloor() > floor)
                continue;

            if (direction == Direction.DOWN &&
                    elevator.getCurrentFloor() < floor)
                continue;

            if (!elevator.hasRequestsAtOrBeyond(
                    floor,
                    direction))
                continue;

            int distance = Math.abs(elevator.getCurrentFloor() - floor);

            if (distance < minDistance) {

                minDistance = distance;
                nearest = elevator;
            }
        }

        return nearest;
    }
}

class Elevator {

    private int currentFloor;

    private Direction direction;

    private final Set<Request> requests;

    public Elevator() {

        currentFloor = 0;

        direction = Direction.IDLE;

        requests = new HashSet<>();
    }

    public boolean addRequest(Request request) {

        if (request.getFloor() < 0 || request.getFloor() > 9)
            return false;

        if (request.getFloor() == currentFloor)
            return true;

        return requests.add(request);
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public Direction getDirection() {
        return direction;
    }

    public boolean hasRequestsAhead(Direction dir) {

        for (Request request : requests) {

            if (dir == Direction.UP &&
                    request.getFloor() > currentFloor)
                return true;

            if (dir == Direction.DOWN &&
                    request.getFloor() < currentFloor)
                return true;
        }

        return false;
    }

    public boolean hasRequestsAtOrBeyond(int floor,
            Direction dir) {

        for (Request request : requests) {

            if (dir == Direction.UP &&
                    request.getFloor() >= floor) {

                if (request.getType() == RequestType.PICKUP_UP ||
                        request.getType() == RequestType.DESTINATION)
                    return true;
            }

            if (dir == Direction.DOWN &&
                    request.getFloor() <= floor) {

                if (request.getType() == RequestType.PICKUP_DOWN ||
                        request.getType() == RequestType.DESTINATION)
                    return true;
            }

        }

        return false;
    }

    public void step() {

        // Case 1 : No requests
        if (requests.isEmpty()) {
            direction = Direction.IDLE;
            return;
        }

        // Case 2 : Elevator is idle, decide initial direction
        if (direction == Direction.IDLE) {

            Request nearest = null;
            int minDistance = Integer.MAX_VALUE;

            for (Request request : requests) {

                int distance = Math.abs(request.getFloor() - currentFloor);

                if (distance < minDistance ||
                        (distance == minDistance &&
                                (nearest == null ||
                                        request.getFloor() < nearest.getFloor()))) {

                    minDistance = distance;
                    nearest = request;
                }
            }

            direction = nearest.getFloor() > currentFloor ? Direction.UP : Direction.DOWN;
        }

        // Case 3 : Stop if needed

        RequestType pickupType = direction == Direction.UP ? RequestType.PICKUP_UP : RequestType.PICKUP_DOWN;

        Request pickupRequest = new Request(currentFloor, pickupType);

        Request destinationRequest = new Request(currentFloor,
                RequestType.DESTINATION);

        if (requests.contains(pickupRequest) ||
                requests.contains(destinationRequest)) {

            requests.remove(pickupRequest);
            requests.remove(destinationRequest);

            if (requests.isEmpty()) {
                direction = Direction.IDLE;
            }

            System.out.println(
                    "Stopped at floor " + currentFloor);

            return;
        }

        // Case 4 : Reverse direction if nothing ahead

        if (!hasRequestsAhead(direction)) {

            direction = direction == Direction.UP ? Direction.DOWN : Direction.UP;

            return;
        }

        // Case 5 : Move one floor

        if (direction == Direction.UP) {

            currentFloor++;

        } else {

            currentFloor--;
        }

        System.out.println(
                "Moving " + direction +
                        " -> Floor " + currentFloor);
    }
}

class Request {
    int floor;
    RequestType type;

    public Request(int floor, RequestType type) {
        this.floor = floor;
        this.type = type;
    }

    public int getFloor() {
        return floor;
    }

    public RequestType getType() {
        return type;
    }
}

enum Direction {
    UP, DOWN, IDLE
}

enum RequestType {
    PICKUP_UP, PICKUP_DOWN, DESTINATION
}
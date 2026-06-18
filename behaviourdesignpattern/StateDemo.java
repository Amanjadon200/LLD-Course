package behaviourdesignpattern;

public class StateDemo {
    public static void main(String[] args) {
        TrafficLightContext trafficLight=new TrafficLightContext();
        System.out.println(trafficLight.color());
        trafficLight.next();
        System.out.println(trafficLight.color());
        trafficLight.next();
        System.out.println(trafficLight.color());
        trafficLight.next();
        System.out.println(trafficLight.color());
        trafficLight.next();
        System.out.println(trafficLight.color());

    }
}
interface TrafficLight{
        TrafficLight nextState();
        String color();
}
class RedLight implements TrafficLight{
    public TrafficLight nextState(){
        return new GreenLight();
    }
    public String color(){
        return "red";
    }
}
class GreenLight implements TrafficLight{
    public TrafficLight nextState(){
        return new YellowLight();
    }
    public String color(){
        return "green";
    }
}
class YellowLight implements TrafficLight{
    public TrafficLight nextState(){
        return new RedLight();
    }
    public String color(){
        return "yellow";
    }
}
class TrafficLightContext {
    private TrafficLight currentState;
    public TrafficLightContext(){
        currentState=new RedLight();
    }
    public void next(){
        currentState=currentState.nextState();
    }
    public String color(){
        return currentState.color();
    }
}

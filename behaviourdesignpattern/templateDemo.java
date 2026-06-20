package behaviourdesignpattern;

public class templateDemo {
    public static void main(String[] args) {
        Employee dev = new Developer("Aman Jadon");
        Employee tester = new Tester("raman jha");
        dev.dayWork();
        tester.dayWork();
    }
}

abstract class Employee {
    String name;

    public final void dayWork() { // workflow // final as nobody cna override the workflow
        login();
        doWork();
        logout();
    }

    public void login() {
        System.out.println("logged in");
    }

    abstract void doWork();

    public void logout() {
        System.out.println("logged out");
    }
}

class Developer extends Employee {
    public Developer(String name) {
        this.name = name;
    }

    public void doWork() {
        System.out.println(name + " is doing development");
    }
}

class Tester extends Employee {
    public Tester(String name) {
        this.name = name;
    }

    public void doWork() {
        System.out.println(name + " is doing testing");
    }
}
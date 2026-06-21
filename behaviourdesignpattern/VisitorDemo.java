package behaviourdesignpattern;

public class VisitorDemo {
    public static void main(String[] args) {
        ChildPatient childPatient = new ChildPatient("John Doe");
        AdultPatient adultPatient = new AdultPatient("Jane Smith");
        SeniorPatient seniorPatient = new SeniorPatient("Robert Johnson");
        DiagnosisVisitor diagnosisVisitor = new DiagnosisVisitor();
        childPatient.accept(diagnosisVisitor);
        adultPatient.accept(diagnosisVisitor);
        seniorPatient.accept(diagnosisVisitor);
        BillingVisitor billingVisitor = new BillingVisitor();
        childPatient.accept(billingVisitor);
        adultPatient.accept(billingVisitor);
        seniorPatient.accept(billingVisitor);
    }
}

interface Visitor {
    void visit(ChildPatient childPatient);

    void visit(AdultPatient adultPatient);

    void visit(SeniorPatient seniorPatient);

}

class ChildPatient {
    String name;

    public ChildPatient(String name) {
        this.name = name;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

class AdultPatient {
    String name;

    public AdultPatient(String name) {
        this.name = name;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

class SeniorPatient {
    String name;

    public SeniorPatient(String name) {
        this.name = name;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

class DiagnosisVisitor implements Visitor {
    @Override
    public void visit(ChildPatient childPatient) {
        System.out.println("Diagnosing child patient: " + childPatient.name);
    }

    @Override
    public void visit(AdultPatient adultPatient) {
        System.out.println("Diagnosing adult patient: " + adultPatient.name);
    }

    @Override
    public void visit(SeniorPatient seniorPatient) {
        System.out.println("Diagnosing senior patient: " + seniorPatient.name);
    }
}

class BillingVisitor implements Visitor {
    @Override
    public void visit(ChildPatient childPatient) {
        System.out.println("Calculating bill for child patient: " + childPatient.name);
    }

    @Override
    public void visit(AdultPatient adultPatient) {
        System.out.println("Calculating bill for adult patient: " + adultPatient.name);
    }

    @Override
    public void visit(SeniorPatient seniorPatient) {
        System.out.println("Calculating bill for senior patient: " + seniorPatient.name);
    }
}
package behaviourdesignpattern;

class PaymentProcessor {
    /*
     * if(credit){
     * creditCardPayment cs=new creditCardPayment();
     * cs.pay();
     * }
     * else if(debit){
     * }
     * else if(cash){
     * }
     * else....
     */
    // lets introduce strategy
    private PaymentStrategy paymentStrategy;// Since strategy shouldn't change after construction:

    public PaymentProcessor(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    void processPayment() {//interface methods are already public and abstract.
        paymentStrategy.pay();
    }
    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
    this.paymentStrategy = paymentStrategy;
}
}

interface PaymentStrategy {
    public void pay();
}

class CreditCardPayment implements PaymentStrategy {
    public void pay() {
        System.out.println("credit card payment processing");
    }
}

class DebitCardPayment implements PaymentStrategy {
    public void pay() {
        System.out.println("debit Card payment processing");
    }
}

class CashPayment implements PaymentStrategy {
    public void pay() {
        System.out.println("cash payment processing");
    }
}

public class Main {
    public static void main(String args[]) {
        PaymentStrategy ccp = new CreditCardPayment();
        PaymentProcessor paymentProcessor = new PaymentProcessor(ccp);
        paymentProcessor.processPayment();
    }
}
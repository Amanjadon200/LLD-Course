package behaviourdesignpattern;

/*
when should we use strategy design pattern?
1. when you have multiple algorithms for a specific task and you want to switch between them dynamically.
2. when you want to avoid using conditional statements for selecting different algorithms.
Real-world examples
Payment methods
Sorting algorithms
Compression algorithms
Authentication providers

Think:

"I want to choose HOW something is done."
*/
public class StrategyDemo {
    public static void main(String args[]) {
        // PaymentStrategy ccp = new CreditCardPayment();
        // PaymentProcessor paymentProcessor = new PaymentProcessor(ccp);
        PaymentProcessor paymentProcessor = new PaymentProcessor(new AdapterRazorPay(new RazorPayPaymentGW()));

        paymentProcessor.processPayment();
    }
}

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
    private PaymentStrategy paymentStrategy;

    public PaymentProcessor(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    void processPayment() {// interface methods are already public and abstract.
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

class RazorPayPaymentGW {
    public void makePayment() {
        System.out.println("payment processing at razor pay side");
    }
}

class AdapterRazorPay implements PaymentStrategy {
    private final RazorPayPaymentGW razorPay;

    public AdapterRazorPay(RazorPayPaymentGW razorPay) {
        this.razorPay = razorPay;
    }

    public void pay() {
        System.out.println("Adaptor adapting");
        razorPay.makePayment();
    }
}
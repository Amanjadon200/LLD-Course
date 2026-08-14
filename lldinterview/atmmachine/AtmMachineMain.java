package lldinterview.atmmachine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AtmMachineMain {
    public static void main(String[] args) {
        BankService bankService = new BankServiceImpl(List.of(
                new Account("1234567890", 1000),
                new Account("0987654321", 500)));
        WithDrawalService withdrawalService = new WithDrawalService(bankService);
        ATMMachine atmMachine = new ATMMachine(withdrawalService, bankService);
        atmMachine.insertCard();
        atmMachine.enterPin(1234);
        atmMachine.checkBalance();
    }
}

class ATMMachine {
    private ATMState state;
    private int totalCash;
    private WithDrawalService withdrawalService;
    BankService bankService;

    public ATMMachine(WithDrawalService withdrawalService, BankService bankService) {
        this.withdrawalService = withdrawalService;
        this.bankService = bankService;
        state = new Idle(this);
        totalCash = 10000; // Initialize with some cash
    }

    public ATMState getState() {
        return state;
    }

    public String getCard() {
        // logic to get card from card number
        return "123444";
    }

    protected void setState(ATMState state) {
        this.state = state;
    }

    public void insertCard() {
        state.insertCard();
    }

    public int getTotalCash() {
        return totalCash;
    }

    public void setTotalCash(int totalCash) {
        this.totalCash = totalCash;
    }

    public void enterPin(int pin) {
        // verify pin logic can be added here
        state.enterPin(pin);
    }

    public void withdraw(String accountNumber, int amount) {
        state.withdraw(amount);
    }

    public void checkBalance() {
        state.checkBalance();
    }

    public void ejectCard() {
        state.ejectCard();
    }

    public WithDrawalService getWithdrawalService() {
        return withdrawalService;
    }

    public BankService getBankService() {
        return bankService;
    }
}

interface ATMState {
    void insertCard();// public and abstract

    void enterPin(int pin);

    void withdraw(int amount);

    void checkBalance();

    void ejectCard();
}

class Idle implements ATMState {
    ATMMachine atmMachine;

    public Idle(ATMMachine atmMachine) {
        this.atmMachine = atmMachine;
    }

    public void insertCard() {
        System.out.println("Card inserted");
        atmMachine.setState(new Hascard(atmMachine));
    }

    public void enterPin(int pin) {
        throw new IllegalStateException("Insert card first");
    }

    public void withdraw(int amount) {
        throw new IllegalStateException("Insert card first");
    }

    public void checkBalance() {
        throw new IllegalStateException("Insert card first");
    }

    public void ejectCard() {
        throw new IllegalStateException("Insert card first");
    }
}

class Hascard implements ATMState {
    ATMMachine atmMachine;

    public Hascard(ATMMachine atmMachine) {
        this.atmMachine = atmMachine;
    }

    public void insertCard() {
        throw new IllegalStateException("Card already inserted");
    }

    public void enterPin(int pin) {
        System.out.println("Pin entered");
        if (pin == 1234) {
            System.out.println("Pin verified");
            atmMachine.setState(new Authenticated(atmMachine));
        } else {
            System.out.println("Invalid pin");
            atmMachine.setState(new Idle(atmMachine));
            return;
        }
    }

    public void withdraw(int amount) {
        throw new IllegalStateException("Authenticate first");
    }

    public void checkBalance() {
        throw new IllegalStateException("Authenticate first");
    }

    public void ejectCard() {
        throw new IllegalStateException("Authenticate first");
    }
}

class WithDrawalService {

    private BankService bankService;

    public WithDrawalService(BankService bankService) {
        this.bankService = bankService;
    }

    public boolean withdraw(String accountNumber, int amount) {                                                                                                     // be
        if (bankService.hasSufficientBalance(accountNumber, amount)) {
            bankService.debit(accountNumber, amount);
            return true;
        } else {
            System.out.println("Insufficient balance");
        }
        return false;
    }
}

class Authenticated implements ATMState {
    ATMMachine atmMachine;
    private WithDrawalService withdrawalService;

    public Authenticated(ATMMachine atmMachine) {
        this.atmMachine = atmMachine;
        this.withdrawalService = atmMachine.getWithdrawalService();
    }

    public void insertCard() {
        throw new IllegalStateException("Card already inserted");
    }

    public void enterPin(int pin) {
        throw new IllegalStateException("Pin already entered");
    }

    public void withdraw(int amount) {
        String cardNumber = atmMachine.getCard();// This should be replaced with actual card number
        String accountNumber = atmMachine.getBankService().getAccount(cardNumber).getAccountNumber(); // This should be
        if (atmMachine.getTotalCash() > amount) {
            System.out.println("ATM has sufficient cash");
            atmMachine.setTotalCash(atmMachine.getTotalCash() - amount);
            boolean isWithdrawalSuccessful = withdrawalService.withdraw(accountNumber,amount);
            if (isWithdrawalSuccessful) {
                System.out.println("Withdrawal successful");
            }
        } else {
            System.out.println("Withdrawal failed");
        }
        // validate account balance and deduct amount logic can be added here

        atmMachine.setState(new Idle(atmMachine));
    }

    public void checkBalance() {
        System.out.println("Balance checked");
        atmMachine.setState(new Idle(atmMachine));
    }

    public void ejectCard() {
        System.out.println("Card ejected");
        atmMachine.setState(new Idle(atmMachine));
    }
}

class Account {
    private String accountNumber;
    private int balance;

    public Account(String accountNumber, int balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}

interface BankService {

    Account getAccount(String cardNumber);

    boolean validateAccount(String accountNumber);

    boolean hasSufficientBalance(
            String accountNumber,
            int amount);

    boolean debit(
            String accountNumber,
            int amount);

    void credit(
            String accountNumber,
            int amount);
}

class BankServiceImpl implements BankService {
    Map<String, Account> accounts;

    public BankServiceImpl(List<Account> accounts) {
        this.accounts = new HashMap<>();
        for (Account account : accounts) {
            this.accounts.put(account.getAccountNumber(), account);
        }
    }

    @Override
    public Account getAccount(String cardNumber) {
        // logic to get account from card number
        return this.accounts.get(cardNumber);
    }

    @Override
    public boolean validateAccount(String accountNumber) {
        // logic to validate account
        return true;
    }

    @Override
    public boolean hasSufficientBalance(String accountNumber, int amount) {
        // logic to check sufficient balance
        return true;
    }

    @Override
    public boolean debit(String accountNumber, int amount) {
        // logic to debit amount from account
        return true;
    }

    @Override
    public void credit(String accountNumber, int amount) {
        // logic to credit amount to account
    }
}
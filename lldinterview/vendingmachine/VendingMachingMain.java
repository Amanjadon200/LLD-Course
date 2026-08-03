package lldinterview.vendingmachine;

import java.util.ArrayList;
import java.util.List;

public class VendingMachingMain {
    public static void main(String[] args) {
        Inventory inventory = new Inventory();
        List<Item> items1 = new ArrayList<Item>();
        items1.add(new Item(10, "Coke", ItemType.COKE));
        items1.add(new Item(10, "Coke", ItemType.COKE));
        items1.add(new Item(10, "Coke", ItemType.COKE));
        inventory.addShelf(new ItemShelf(items1, 1));

        List<Item> items2 = new ArrayList<Item>();
        items2.add(new Item(15, "Pepsi", ItemType.PEPSI));
        items2.add(new Item(15, "Pepsi", ItemType.PEPSI));
        inventory.addShelf(new ItemShelf(items2, 2));

        VendingMachine vendingMachine = new VendingMachine(inventory);
        vendingMachine.insertCoin(Coin.ten);
        vendingMachine.insertCoin(Coin.five);
        vendingMachine.selectItem(1);
        vendingMachine.dispenseItem(1);
    }
}
// entitites
enum Coin{
    one(1), two(2), five(5), ten(10);
    private int value;
    Coin(int value){
        this.value = value;
    }
    public int getValue(){
        return value;
    }
}
enum ItemType{
    COKE, PEPSI, SODA
}
class Item{
    int price;
    String name;
    ItemType type;
    public Item(int price, String name, ItemType type){
        this.price = price;
        this.name = name;
        this.type = type;
    }
}
class ItemShelf{
    List<Item> items;
    int code;
    public ItemShelf(List<Item> items, int code){
        this.items = items;
        this.code = code;
    }

}
class Inventory{
    List<ItemShelf> itemShelves;
    public Inventory(){
        this.itemShelves = new ArrayList<ItemShelf>();
    }
    public void addShelf(ItemShelf shelf){
        itemShelves.add(shelf);
    }
    public void addItem(Item item, int code){
        for(ItemShelf shelf : itemShelves){
            if(shelf.code == code){
                shelf.items.add(item);
                break;
            }
        }
    }
    public void dispenseItem(int code){
        for(ItemShelf shelf : itemShelves){
            if(shelf.code == code){
                shelf.items.remove(0);
                break;
            }
        }
    }
    public void isSoldOut(int code){
        for(ItemShelf shelf : itemShelves){
            if(shelf.code == code){
                if(shelf.items.size() == 0){
                    System.out.println("Item is sold out");
                }
                break;
            }
        }
    }
}
class VendingMachine{
    Inventory inventory;
    int currentBalance;
    State currentState;
    int selectedItemCode;
    public VendingMachine(Inventory inventory){
        this.inventory = inventory;
        this.currentBalance = 0;
        this.currentState = new IdleState(this);
        this.selectedItemCode = -1;
    }
    public void addItem(Item item, int code){
        inventory.addItem(item, code);
    }
    public void isSoldOut(int code){
        inventory.isSoldOut(code);
    }
    public void insertCoin(Coin coin){
        this.currentBalance += coin.getValue();
        currentState.insertCoin(coin);
    }
    public void selectItem(int code){
        currentState.selectItem(code);
    }
    public void dispenseItem(int code){
        inventory.dispenseItem(code);
    }
}
interface State{
    void insertCoin(Coin coin);
    void selectItem(int code);
    void dispenseItem(int code);
    void refund();
}
class IdleState implements State{
    private VendingMachine vendingMachine;
    public IdleState(VendingMachine vendingMachine){
        this.vendingMachine = vendingMachine;
    }
    public void insertCoin(Coin coin){
        System.out.println("Coin inserted");
        vendingMachine.currentState = new HasMoneyState(vendingMachine);
    }
    public void selectItem(int code){
        throw new IllegalStateException("Insert coin first");
    }

    public void dispenseItem(int code){
        throw new IllegalStateException("Insert coin first");
    }
    public void refund(){
        throw new IllegalStateException("Insert coin first");
    }
}

class HasMoneyState implements State{
    private VendingMachine vendingMachine;
    public HasMoneyState(VendingMachine vendingMachine){
        this.vendingMachine = vendingMachine;
    }
    public void insertCoin(Coin coin){
        System.out.println("Coin inserted");
    }
    public void selectItem(int code){
        System.out.println("Item selected");
        vendingMachine.selectedItemCode = code;
        vendingMachine.currentState = new DispenseState(vendingMachine);
    }
    public void dispenseItem(int code){
        throw new IllegalStateException("Item cannot be dispensed without selecting it");
    }
    public void refund(){
        System.out.println("Refunding money");
        vendingMachine.currentBalance = 0;
        vendingMachine.currentState = new IdleState(vendingMachine);
    }   
}
class DispenseState implements State{
    private VendingMachine vendingMachine;
    public DispenseState(VendingMachine vendingMachine){
        this.vendingMachine = vendingMachine;
    }
    public void dispenseItem(int code){
        System.out.println("Item dispensed");
        vendingMachine.dispenseItem(code);
        vendingMachine.currentBalance = 0;
        vendingMachine.currentState = new IdleState(vendingMachine);
        vendingMachine.selectedItemCode = -1;
    }
    public void insertCoin(Coin coin){
        throw new IllegalStateException("Item is being dispensed");
    }
    public void selectItem(int code){
        throw new IllegalStateException("Item is being dispensed");
    }
    public void refund(){
        throw new IllegalStateException("Item is being dispensed");
    }
}
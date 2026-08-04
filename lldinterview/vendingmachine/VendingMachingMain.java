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
        vendingMachine.dispenseItem();
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
    public Item dispenseItem(int code){
        for(ItemShelf shelf : itemShelves){
            if(shelf.code == code){
                if(shelf.items.size() == 0){
                    throw new IllegalStateException("Item is sold out");
                }
                Item item=shelf.items.remove(0);
                return item;
            }
        }
        throw new IllegalArgumentException("Invalid item code");
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
    public Item getItem(int code){
        for(ItemShelf shelf : itemShelves){
            if(shelf.code == code){
                if(shelf.items.size() == 0){
                    throw new IllegalStateException("Item is sold out");
                }
                return shelf.items.get(0);
            }
        }
        throw new IllegalArgumentException("Invalid item code");
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
        this.currentState = IdleState.instance;
        this.selectedItemCode = -1;
    }
    public Inventory getInventory(){
        return inventory;
    }
    public void addItem(Item item, int code){
        inventory.addItem(item, code);
    }
    public void isSoldOut(){
        inventory.isSoldOut(selectedItemCode);
    }
    public void insertCoin(Coin coin){
        this.currentBalance += coin.getValue();
        currentState.insertCoin(this, coin);
    }
    public void selectItem(int code){
        selectedItemCode = code;
        currentState.selectItem(this, code);
    }
    public void dispenseItem(){
        currentState.dispenseItem(this);
    }
    public void setState(State state){
        this.currentState = state;
    }
}
interface State{
    void insertCoin(VendingMachine vendingMachine, Coin coin);
    void selectItem(VendingMachine vendingMachine, int code);
    void dispenseItem(VendingMachine vendingMachine);
    void refund(VendingMachine vendingMachine);
}
class IdleState implements State{
    private IdleState(){}
    public static final IdleState instance = new IdleState();
    public void insertCoin(VendingMachine vendingMachine, Coin coin){
        System.out.println("Coin inserted");
        vendingMachine.setState(HasMoneyState.instance);
    }
    public void selectItem(VendingMachine vendingMachine, int code){
        throw new IllegalStateException("Insert coin first");
    }

    public void dispenseItem(VendingMachine vendingMachine){
        throw new IllegalStateException("Insert coin first");
    }
    public void refund(VendingMachine vendingMachine){
        throw new IllegalStateException("Insert coin first");
    }
}

class HasMoneyState implements State{
    private HasMoneyState(){}
    public static final HasMoneyState instance = new HasMoneyState();

    public void insertCoin(VendingMachine vendingMachine, Coin coin){
        System.out.println("Coin inserted");
    }
    public void selectItem(VendingMachine vendingMachine, int code){
        System.out.println("Item selected");
        vendingMachine.selectedItemCode = code;
        vendingMachine.setState( DispenseState.instance);
    }
    public void dispenseItem(VendingMachine vendingMachine){
        throw new IllegalStateException("Item cannot be dispensed without selecting it");
    }
    public void refund(VendingMachine vendingMachine){
        System.out.println("Refunding money");
        vendingMachine.currentBalance = 0;
        vendingMachine.setState(IdleState.instance);
    }   
}
class DispenseState implements State{
    public static final DispenseState instance = new DispenseState();
    private DispenseState(){}
    public void dispenseItem(VendingMachine vendingMachine){
        System.out.println("Item dispensed");
        Inventory inventory = vendingMachine.getInventory();
        Item item = inventory.getItem(vendingMachine.selectedItemCode);
        if(item.price > vendingMachine.currentBalance){
            throw new IllegalStateException("Insufficient balance");
        }
        inventory.dispenseItem(vendingMachine.selectedItemCode);
        vendingMachine.currentBalance = vendingMachine.currentBalance - item.price;
        vendingMachine.setState(IdleState.instance);
        vendingMachine.selectedItemCode = -1;
    }
    public void insertCoin(VendingMachine vendingMachine, Coin coin){
        throw new IllegalStateException("Item is being dispensed");
    }
    public void selectItem(VendingMachine vendingMachine, int code){
        throw new IllegalStateException("Item is being dispensed");
    }
    public void refund(VendingMachine vendingMachine){
        throw new IllegalStateException("Item is being dispensed");
    }
}
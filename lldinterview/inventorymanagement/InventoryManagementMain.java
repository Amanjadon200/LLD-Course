package lldinterview.inventorymanagement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class InventoryManagementMain {
    public static void main(String[] args) {

        ProductFactory productFactory = new ProductFactory();
        Product product1 = productFactory.create("sku1", "milk", 10.0, ProductCategory.GROCERY);
        Product product2 = productFactory.create("sku2", "bread", 5.0, ProductCategory.GROCERY);

        WareHouse wareHouse1 = new WareHouse(1);
        WareHouse wareHouse2 = new WareHouse(2);
        Map<Integer, WareHouse> wareHouses = new HashMap<>();
        wareHouses.put(wareHouse1.getId(), wareHouse1);
        wareHouses.put(wareHouse2.getId(), wareHouse2);

        InventoryManager inventoryManager = new InventoryManager(wareHouses, "inventory manager",
                new RandomWareHouseSelectionStrategy());
        int warehouseid1 = inventoryManager.addInventory(1, product1, 5, 3);
        int warehouseid2 = inventoryManager.addInventory(2, product2, 10, 5);
        int warehouseid3 = inventoryManager.addInventory(2, product1, 2, 3);
        System.out.println(
                "Inventory items in warehouse " + warehouseid1 + ": " + wareHouse1.fetchAllInventoryItems());
        System.out.println(
                "Inventory items in warehouse " + warehouseid2 + ": " + wareHouse2.fetchAllInventoryItems());

        inventoryManager.removeInventory("sku1", 5, wareHouse1);
        System.out.println(
                "Inventory items in warehouse " + warehouseid1 + ": " + wareHouse1.fetchAllInventoryItems());
        System.out.println(
                "Inventory items in warehouse " + warehouseid2 + ": " + wareHouse2.fetchAllInventoryItems());
        // System.out.println("Inventory items in warehouse " + warehouseid3 + "
        // after removal: " + wareHouse3.fetchAllInventoryItems());
    }
}

/*
 * Requirements:
 * 1: system can manage different different types of product .
 * 2 system shoud manage prodcuts across different ware houses
 * 3: system need to handle multiple ware house
 * 4: system should be able ot add or remove product
 * 5: system shold fire an alert when product or inventory for a specific
 * product goes less than some number.
 * 6: system should efficienlty use replishment strategy.
 * 7: system should handle damaged or return item.
 * 
 */
/*
 * entities:
 * 1: Product;
 * 2: warehouse
 * 3: inventory manager
 */
abstract class Product {
    private String sku;
    private String name;
    private double price;
    private ProductCategory category;

    public Product(String sku, String name, double price, ProductCategory category) {
        this.sku = sku;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public ProductCategory getCategory() {
        return category;
    }
}

enum ProductCategory {
    ELECTRONICS, CLOTHING, GROCERY, FURNITURE, OTHER
}

class WareHouse {
    private int id;
    private Map<String, InventoryItem> inventoryItems;

    public WareHouse(int id) {
        this.id = id;
        this.inventoryItems = new HashMap<>();
    }

    public void add(InventoryItem inventoryItem) {
        if (inventoryItems.containsKey(inventoryItem.getProduct().getSku())) {
            InventoryItem existingItem = inventoryItems.get(inventoryItem.getProduct().getSku());
            existingItem.setQuantity(existingItem.getQuantity() + inventoryItem.getQuantity());
        } else {
            inventoryItems.put(inventoryItem.getProduct().getSku(), inventoryItem);
        }
        System.out.println("Added " + inventoryItem.getQuantity() + " of " + inventoryItem.getProduct().getName()
                + " to warehouse " + id);
    }

    public void remove(String sku, int quantity) {
        if (inventoryItems.containsKey(sku)) {
            InventoryItem existingItem = inventoryItems.get(sku);
            int newQuantity = existingItem.getQuantity() - quantity;
            if (newQuantity <= 0) {
                inventoryItems.remove(sku);
            } else {
                existingItem.setQuantity(newQuantity);
            }
        }
        System.out.println("Removed " + quantity + " of " + sku + " from warehouse " + id);
        System.out.println(inventoryItems);
    }

    public int getId() {
        return id;
    }

    public List<String> fetchAllInventoryItems() {
        // return
        return List.copyOf(inventoryItems.keySet());
    }
}

class InventoryItem {
    private Product product;
    private int quantity;
    private int threshold;

    public InventoryItem(Product product, int quantity, int threshold) {
        this.product = product;
        this.quantity = quantity;
        this.threshold = threshold;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getThreshold() {
        return threshold;
    }
}

class InventoryManager {
    Map<Integer, WareHouse> wareHouses;
    String name;
    WareHouseSelectionStrategy wareHouseSelectionStrategy;
    Random random = new Random();

    public InventoryManager(Map<Integer, WareHouse> wareHouses, String name,
            WareHouseSelectionStrategy wareHouseSelectionStrategy) {
        this.name = name;
        this.wareHouses = wareHouses;
        this.wareHouseSelectionStrategy = wareHouseSelectionStrategy;
    }

    public int addInventory(int warehouseId, Product product, int quantity, int threshold) {
        InventoryItem inventoryItem = new InventoryItem(product, quantity, threshold);
        WareHouse warehouse = wareHouses.get(warehouseId);
        if (warehouse != null) {
            warehouse.add(inventoryItem);
        }
        return warehouse.getId();
    }

    public void removeInventory(String sku, int quantity, WareHouse warehouse) {

        if (warehouse != null) {
            warehouse.remove(sku, quantity);
        }

    }

    // public void removeInventory(InventoryItem inventoryItem){
    // WareHouse warehouse=findWareHouse(inventoryItem);
    // if(warehouse!=null){
    // warehouse.remove(inventoryItem);
    // if(inventoryItem.getQuantity()<3){
    // // fire an alert
    // }
    // }
    // }
    public WareHouse findWareHouse(InventoryItem inventoryItem) {
        for (WareHouse wareHouse : wareHouses.values()) {
            for (String sku : wareHouse.fetchAllInventoryItems()) {
                if (sku.equals(inventoryItem.getProduct().getSku()))
                    return wareHouse;
            }
        }
        return null;
    }

    public WareHouse matchWarehouseToPlace() {
        int warehouse = random.nextInt(wareHouses.size());
        return wareHouses.get(warehouse);
    }
}

class GroceryProduct extends Product {
    public GroceryProduct(String sku, String name, double price, ProductCategory category) {
        super(sku, name, price, category);
    }

}

class ProductFactory {
    public Product create(String sku, String name, double price, ProductCategory category) {
        switch (category) {
            case GROCERY:
                return new GroceryProduct(sku, name, price, category);
            // return pb;
            // case ProductCategory.ELECTRONICS:
            // // return new ElectronicsProduct(sku, name, price, quantity, threshold);
            // return new ElectronicsProduct.ElectronicsProductBuilder().setSku(sku)
            // .setName(name)
            // .setPrice(price)
            // .setQuantity(quantity)
            // .setThreshold(threshold)
            // .build();
            default:
                throw new IllegalArgumentException("Invalid product category: " + category);
        }

    }
}

interface ReplishmentStrategy {
    void replish();
}

class JustInTimeReplishmentStrategy implements ReplishmentStrategy {
    @Override
    public void replish() {
        // implement just in time replishment logic
    }
}

interface WareHouseSelectionStrategy {
    WareHouse selectWareHouse(List<WareHouse> wareHouses);
}

class RandomWareHouseSelectionStrategy implements WareHouseSelectionStrategy {
    private Random random = new Random();

    @Override
    public WareHouse selectWareHouse(List<WareHouse> wareHouses) {
        int warehouse = random.nextInt(wareHouses.size());
        return wareHouses.get(warehouse);
    }
}
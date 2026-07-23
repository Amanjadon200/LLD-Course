package lldinterview.inventorymanagement;

import java.lang.module.ModuleDescriptor.Builder;
import java.util.List;
import java.util.Random;

public class InventoryManagementMain {
    public static void main(String[] args) {

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
    private int quantity;
    private int threshold;
    private ProductCategory category;
    public Product(String sku, String name, double price, int quantity, int threshold, ProductCategory category) {
            this.sku = sku;
            this.name = name;
            this.price = price;
            this.quantity = quantity;
            this.threshold = threshold;
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
    public int getQuantity() {
        return quantity;
    }
    public int getThreshold() {
        return threshold;
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
    private List<Product> products;

    public WareHouse(int id, List<Product> products) {
        this.id = id;
        this.products = products;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void removeProduct(Product product) {
        products.remove(product);
    }
    public List<Product> fetchAllProducts(){
        return this.products;
    }
}

class InventoryManager {
    List<WareHouse> wareHouses;
    String name;
    Random random=new Random();
    public InventoryManager(List<WareHouse> wareHouses, String name) {
        this.name = name;
        this.wareHouses = wareHouses;
    }
    public void addProduct(Product product){
        WareHouse warehouse=matchWarehouseToPlace();
        warehouse.addProduct(product);
    }
    public void removeProduct(Product product){
        WareHouse warehouse=findWareHouse(product);
        if(warehouse!=null){
            warehouse.removeProduct(product);
            if(product.getQuantity()<3){
                // fire an alert 
            }
        }
    }
    public WareHouse findWareHouse(Product product){
        for (WareHouse wareHouse : wareHouses) {
            for(Product p: wareHouse.fetchAllProducts()){
                if(p==product)return wareHouse;
            }
        }
        return null;
    }
    public WareHouse matchWarehouseToPlace(){
        int warehouse=random.nextInt(wareHouses.size());
        return wareHouses.get(warehouse);
    }
}
class GroceryProduct extends Product{
    public GroceryProduct(String sku, String name, double price, int quantity, int threshold, ProductCategory category) {
        super(sku, name, price, quantity, threshold, category);
    }

}

class ProductFactory{
    public Product create(String sku,String name,double price,int quantity,int threshold,ProductCategory category){
        switch (category) {
            case ProductCategory.GROCERY:
                return new GroceryProduct(sku, name, price, quantity, threshold, category);
                // return pb;
            // case ProductCategory.ELECTRONICS:
            //     // return new ElectronicsProduct(sku, name, price, quantity, threshold);
            //     return new ElectronicsProduct.ElectronicsProductBuilder().setSku(sku)
            //             .setName(name)
            //             .setPrice(price)
            //             .setQuantity(quantity)
            //             .setThreshold(threshold)
            //             .build();
            default:
                return null;
        }
    
    }
}
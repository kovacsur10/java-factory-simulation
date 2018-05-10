package hq574o;

public abstract class Product {

    String name;
    ProductType type;
    
    Product(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public abstract String toString();
    
    public ProductType getType() {
        return this.type;
    }
    
}

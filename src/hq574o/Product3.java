package hq574o;

import java.util.function.Supplier;

public class Product3 extends Product {

    private String value;
    
    public Product3(String name, String value) {
        super(name);
        this.value = value;
        this.type = ProductType.PRODUCT_3;
    }
    
    public static final Supplier<Product3> create = () -> { return new Product3(RandomString.get(), "a"); };

    @Override
    public String toString() {
        return "Product3(name: "+this.name+", value="+this.value+")";
    }
}

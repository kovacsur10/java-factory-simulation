package hq574o;

import java.util.Random;
import java.util.function.Supplier;

public class Product2 extends Product {

    private double value;
    
    public Product2(String name, double value) {
        super(name);
        this.value = value;
        this.type = ProductType.PRODUCT_2;
    }
    
    public static final Supplier<Product2> create = () -> { return new Product2(RandomString.get(), new Random().nextDouble()); };
    
    @Override
    public String toString() {
        return "Product2(name: "+this.name+", value="+String.valueOf(this.value)+")";
    }

}

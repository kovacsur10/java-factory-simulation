package hq574o;

import java.util.Random;
import java.util.function.Supplier;

public class Product1 extends Product {

    private Integer value;
    
    public Product1(String name, Integer value) {
        super(name);
        this.value = value;
        this.type = ProductType.PRODUCT_1;
    }
    
    public static final Supplier<Product1> create = () -> { return new Product1(RandomString.get(), new Random().nextInt()); };
    
    @Override
    public String toString() {
        return "Product1(name: "+this.name+", value="+this.value.toString()+")";
    }

}

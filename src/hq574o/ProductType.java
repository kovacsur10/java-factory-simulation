package hq574o;

public enum ProductType {
    PRODUCT_1 (0),
    PRODUCT_2 (1),
    PRODUCT_3 (2);
    
    private final int value;
    
    private ProductType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    
    
}

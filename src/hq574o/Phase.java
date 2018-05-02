package hq574o;

public enum Phase {
    PHASE_1 (100, 200),
    PHASE_2 (1200, 1400),
    PHASE_3 (50, 990),
    PHASE_4 (10, 100),
    PHASE_5 (10, 30);
    
    public final Integer min;
    public final Integer max;
    
    Phase(Integer min, Integer max){
        this.min = min;
        this.max = max;
    }
}

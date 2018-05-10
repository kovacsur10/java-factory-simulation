package hq574o;

import java.util.Arrays;
import java.util.List;

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
    
    public static Phase next(Phase p) {
        List<Phase> l = Arrays.asList(Phase.values());
        int id = l.lastIndexOf(p)+1;
        return id == Phase.values().length ? l.get(0) : l.get(id);
    }
}

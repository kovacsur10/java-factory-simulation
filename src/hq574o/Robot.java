package hq574o;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Robot implements Runnable{
    
    private static final BiFunction<Integer, Integer, Integer> suppl_waittime = (min, max) -> {
        return new Random().nextInt(max - min) + min;
    };
    private Function<Phase, List<Integer>> needed_resources;
    private String name;
    private Phase phase;
    private List<Product> products;
    private Boolean produced;
    private Boolean working;
    private Function<List<Product>, Product> producer;
    
    @Override
    public void run() {
        this.working = true;
        while(this.working) {
            process();
            System.out.println("A robot ("+this.name+") lepihen, hamarosan újra próbálkozik a termeléssel!");
            try {
                TimeUnit.MILLISECONDS.sleep(suppl_waittime.apply(this.phase.min, this.phase.max));
            } catch (InterruptedException e) {
            }
        }
    }
    
    public Robot(String name, Function<List<Product>, Product> producer) {
        this.name = name;
        this.phase = Phase.PHASE_1;
        this.products = new ArrayList<Product>();
        this.produced = false;
        this.working = false;
        this.producer = producer;
    }
    
    public void setNeededResources(Function<Phase, List<Integer>> needed_res) {
        this.needed_resources = needed_res;
    }
    
    public synchronized void supply(Product pr) {
        System.out.println("A robot("+this.name+") kapott egy terméket: "+pr.toString());
        this.products.add(pr);
    }
    
    public synchronized Boolean produced() {
        return this.produced;
    }
    
    public synchronized Phase getPhase() {
        return this.phase;
    }
    
    public synchronized Boolean running() {
        return this.working;
    }
    
    public synchronized void nextPhase(Function<List<Product>, Product> new_producer) {
        this.phase = Phase.next(this.phase);
        this.produced = false;
        this.producer = new_producer;
        System.out.println("A robot("+this.name+") a közetkező fázisba lett áthelyezve a vezérlő által (az új fázis: "+this.phase.toString()+")!");
    }
    
    public void stop() {
        this.working = false;
        System.out.println("A robot("+this.name+") a vezérlő által ki lett kapcsolva!");
    }
    
    private synchronized void process() {
        if(!this.produced) {
            List<Integer> prod_counts = new ArrayList<Integer>();
            prod_counts.add((int) this.products.stream().filter(product -> product.getType() == ProductType.PRODUCT_1).count());
            prod_counts.add((int) this.products.stream().filter(product -> product.getType() == ProductType.PRODUCT_2).count());
            prod_counts.add((int) this.products.stream().filter(product -> product.getType() == ProductType.PRODUCT_3).count());
            List<Integer> l = this.needed_resources.apply(this.phase);
            Boolean canProduce = l.get(0) <= prod_counts.get(0) && l.get(1) <= prod_counts.get(1) && l.get(2) <= prod_counts.get(2);
            
            if(!canProduce) {
                System.out.println("A robot ("+this.name+") nem rendelkezik elég nyersanyaggal!");
                return;
            }
            List<Product> consumable = new ArrayList<Product>();
            System.out.println("A robot ("+this.name+") elérte a nyersanyagszükséglet határát, gyártásba kezd!");
            for(Iterator<Product> iter = this.products.listIterator(); iter.hasNext(); ) {
                Product a = iter.next();
                Integer index = a.getType().getValue();
                if(0 < prod_counts.get(index)){
                    consumable.add(a);
                    iter.remove();
                    prod_counts.set(index, prod_counts.get(index) - 1);
                }
            }
            this.products.add(this.producer.apply(consumable));
            this.produced = true;
        }else {
            System.out.println("A robot ("+this.name+") már termelt ebben a fázisban!");
        }
    }

    @Override
    public String toString() {
        return "Robot(name: "+this.name+", phase: "+this.phase+", "+this.products+")";
    }

}

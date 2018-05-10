package hq574o;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Controller {
    
    private final static int WAITTIME_MIN = 500; //t3
    private final static int WAITTIME_MAX = 1000; //t4
    private static final Supplier<Integer> suppl_waittime = () -> {
        return new Random().nextInt(WAITTIME_MAX - WAITTIME_MIN) + WAITTIME_MIN;
    };
    private static final Supplier<ProductType> suppl_productType = () -> {
        return Arrays.asList(ProductType.values()).get(new Random().nextInt(ProductType.values().length));
    };
    private static List<Robot> robots = new ArrayList<Robot>();
    private static Integer robotCount;
    private static Function<Phase, List<Integer>> phase_needed_resources;
    private static List<Function<List<Product>, Product>> producer_lambdas;
    
    public static void main(String[] args) {
        producer_lambdas = new ArrayList<>();
        producer_lambdas.add(list -> Product1.create.get());
        producer_lambdas.add(list -> {
            switch(Arrays.asList(ProductType.values()).get(new Random().nextInt(ProductType.values().length))) {
                case PRODUCT_1: return Product1.create.get();
                case PRODUCT_2: return Product2.create.get();
                default: return Product3.create.get();
            }
        });
        producer_lambdas.add(list -> {
            switch(Arrays.asList(ProductType.values()).get(list.size() % ProductType.values().length)) {
                case PRODUCT_1: return Product1.create.get();
                case PRODUCT_2: return Product2.create.get();
                default: return Product3.create.get();
            }
        });
        producer_lambdas.add(list -> {
            switch(Arrays.asList(ProductType.values()).get(producer_lambdas.size() % ProductType.values().length)) {
                case PRODUCT_1: return Product1.create.get();
                case PRODUCT_2: return Product2.create.get();
                default: return Product3.create.get();
            }
        });
        producer_lambdas.add(list -> {
            Integer val = 0;
            for(Product p : list) {
                val += p.type.getValue();
            }
            switch(Arrays.asList(ProductType.values()).get(val % ProductType.values().length)) {
                case PRODUCT_1: return Product1.create.get();
                case PRODUCT_2: return Product2.create.get();
                default: return Product3.create.get();
            }
        });
        
        // Read config file data
        Path filePath;
        if(args.length == 1) {
            filePath = Paths.get(args[0]);
        } else {
            filePath = Paths.get("config.txt");  
        }
        System.out.println("A vezérlő a konfigurációs fájlt olvassa be!");
        readConfigFile(filePath);
        System.out.println("A vezérlő a konfigurációs fájlt beolvasta!");
        
        // Cycle until done.
        ExecutorService robotPool = Executors.newFixedThreadPool(robotCount);
        for(Robot r : robots) {
            robotPool.execute(r);
        }
        
        Integer stoppedCount = 0;
        while(stoppedCount < robotCount) {
            System.out.println("A vezérlő a robotokat vizsgálja meg!");
            
            for(Robot r : robots) {
                if(r.running()) {
                    if(r.produced()) {
                        if(r.getPhase() == Phase.PHASE_5) {
                            r.stop();
                            stoppedCount++;
                        }else {
                            r.nextPhase(producer_lambdas.get(Arrays.asList(Phase.values()).indexOf(r.getPhase())));
                        }
                    }else {
                        Integer ph_id = Arrays.asList(Phase.values()).indexOf(r.getPhase()) + 1;
                        for(int i = 0; i < ph_id; i++) {
                            switch(suppl_productType.get()) {
                                case PRODUCT_1: r.supply(Product1.create.get());
                                    break;
                                case PRODUCT_2: r.supply(Product2.create.get());
                                    break;
                                case PRODUCT_3: r.supply(Product3.create.get());
                                    break;
                            }
                        }
                    }
                }
            }
            
            System.out.println("A vezérlő lepihen, hamarosan újra vizsgálódásba fog kezdeni!");
            try {
                TimeUnit.MILLISECONDS.sleep(suppl_waittime.get());
            } catch (InterruptedException e) {
            }
        }
        robotPool.shutdown();
        
        System.out.println("A robotok sikeresen végeztek a gyártással! A kontroller mély álomba szenderül...");
    }
    
    private static void readConfigFile(Path path) {
        List<String> list = new ArrayList<String>();
        try {
            list = Files.readAllLines(path);
        } catch (IOException e) {
            System.out.println("Ajajj, hiba jelentkezett a konfigurációs fájl beolvasásakor!");
            e.printStackTrace();
        }
        robotCount = Integer.parseInt(list.get(0));
        list.stream().skip(1).limit(robotCount).forEach(line -> {
            Scanner sc = new Scanner(line);
            Robot robot = new Robot(RandomString.get(), producer_lambdas.get(0));
            int limit = sc.nextInt();
            for(int i = 0; i < limit; i++) {
                robot.supply(Product1.create.get());
            }
            limit = sc.nextInt();
            for(int i = 0; i < limit; i++) {
                robot.supply(Product2.create.get());
            }
            limit = sc.nextInt();
            for(int i = 0; i < limit; i++) {
                robot.supply(Product3.create.get());
            }
            sc.close();
            robots.add(robot);
            System.out.println("Egy újabb robot állt készenlétbe!");
        });
        List<Supplier<List<Integer>>> tmp_lambdas = new ArrayList<>();
        list.stream().skip(robotCount + 1).forEach(line -> {
            List<Integer> l = Arrays.stream(line.split(" ")).map(Integer::parseInt).collect(Collectors.toList());
            tmp_lambdas.add(() -> l);
        });
        phase_needed_resources = ph -> tmp_lambdas.get(Arrays.asList(Phase.values()).lastIndexOf(ph)).get();
        System.out.println("A fázisok közötti átjárhatósági nyersanyagszükségletet elkészítettük, a robotok éppen most kapják meg ezeket!");
        robots.stream().forEach(robot -> { robot.setNeededResources(phase_needed_resources); });
    }
    
}

package hq574o;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Supplier;

public class Controller {
    
    private final static int WAITTIME_MIN = 500;
    private final static int WAITTIME_MAX = 1000;
    private static Supplier<Integer> suppl_Product1;
    private static Integer robotCount;

    public static void main(String[] args) {
        Boolean running = true;
        // Read config file data
        Path filePath;
        if(args.length == 1) {
            filePath = Paths.get(args[0]);
        } else {
            filePath = Paths.get("config.txt");  
        }
        readConfigFile(filePath);
        
        // Cycle until done.
        running = false; // TODO: remove
        while(running) {
            
            
        }
        
        //Printout done.
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
            
        });
        list.stream().skip(robotCount + 1).forEach(line -> {});
    }
    
}

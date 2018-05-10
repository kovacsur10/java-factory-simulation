package hq574o;

import java.util.UUID;

public class RandomString {

    public static String get() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}

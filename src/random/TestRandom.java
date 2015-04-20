package random;

import java.util.Random;

/**
 * @author huhai
 * @date 2015/4/20 10:55
 */
public class TestRandom {
    public static void main(String args[]) throws Throwable {
        for(int i=0;i<10;i++) {
            System.out.print((new Random(10)).nextInt(10));
        }
        System.out.println();
        for(int j=0;j<10;j++) {
            System.out.print((new Random()).nextInt(10));
        }
        System.out.println();
        Random random = new Random();
        for(int k=0;k<10;k++) {
            System.out.print(random.nextInt(10));;
        }
    }
}

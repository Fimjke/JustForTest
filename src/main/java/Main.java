import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Main {
    public static void main(String[] args) {

        Counter counter =  new Counter();
        counter.parser("++0(-)-++0-");
        System.out.println(counter.convertToTenSystemCount(counter.expressionA) + " Число А");
        System.out.println(counter.convertToTenSystemCount(counter.expressionB) + " Число Б");
        System.out.println(counter.actionForTest() + " какой дожден быть результат");
        counter.converterForAB();
        System.out.println(Arrays.toString(counter.conA) + " Данный массив всегда должен быть максимальным");
        System.out.println(Arrays.toString(counter.conB));
        System.out.println(Arrays.asList(counter.addition()));


//        System.out.println(Arrays.toString(counter.reverse(counter.conB)));
//        System.out.println("RESULT " + counter.getResult());
//        System.out.println("проверка:");
//        System.out.println(counter.convertToTenSystemCount(counter.getResult()));

//        counter.getSolution();
    }

}

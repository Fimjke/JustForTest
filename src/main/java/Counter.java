import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Counter {

    public String expressionA;
    public String expressionB;
    public char act;

    public int[] conA;
    public int[] conB;


    public void parser (String str){
        int f1EndIndex = str.indexOf("(");
        expressionA = str.substring(0, f1EndIndex);
        act = str.charAt(f1EndIndex+1);
        expressionB = str.substring(f1EndIndex+3, str.length());
        System.out.println("expressionA: "+ expressionA+ "\nact: "+ act + "\nexpressionB: " + expressionB);
    }


    /* приводим число к виду: [1 0 -1 0 1]
     для удобства вычислений */

    public int[] converterToMainSystem (String str){
        int[] array = new int[str.length()];
        for(int i = 0; i < str.length(); i++){
            if (str.charAt(i) == '+'){
                array[i] = 1;
            } else if(str.charAt(i) == '-'){
                array[i] = -1;
            } else {
                array[i] = 0;
            }
        }
        return array;
    }

    //приводим оба числа к единичному представлению для расчетов
    public void converterForAB (){
        conA = converterToMainSystem(expressionA);
        conB = converterToMainSystem(expressionB);
    }

    //метод для получения обратного числа, будем использоваить при вычитании если число положительное
    public int[] reverse (int[] a){
        int[] res = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            if (a[i] == -1){
                res[i] = 1;
            } else if (a[i] == 1){
                res[i] = -1;
            } else {
                res[i] = a[i];
            }
        }
        return res;
    }


    //собственно, само сложение
    public ArrayList<Integer> addition() {
        int maxIndex;
        int minIndex;

        if (conA.length > conB.length) {
            maxIndex = conA.length - 1;
            minIndex = conB.length - 1;
        } else {
            maxIndex = conB.length - 1;
            minIndex = conA.length - 1;
        }

        ArrayList<Integer> resultList = new ArrayList<Integer>();
        //сюда складываем превышенное значение на следующий разряд
        int tmp = 0;
        //смещение индексов (сложение столбиком), да-да лучше не придумала
        for (int i = maxIndex, j = minIndex; j >= 0; i--, j--) {
            int res;
            if (conA.length > conB.length) {
                res = conA[i] + conB[j] + tmp;
            } else {
                res = conA[j] + conB[i] + tmp;
            }
            // в случае +(+)+
            if (res == 2) {
                resultList.add(-1);
                tmp = 1;
                res = 0; //обнуляем рес для следующего разряда
            } else if (res == -2) {  //в случае -(+)-
                resultList.add(1);
                tmp = -1;
                res = 0;
            } else if (res == 3) { // в случае + (+) + (+) +
                resultList.add(0);
                tmp = 1;
                res = 0;
            } else if (res == -3) { // в случае - (+) - (+) -
                resultList.add(0);
                tmp = -1;
                res = 0;
            } else {
                resultList.add(res); //во всех остальных
                res = 0;
                tmp = 0;
            }
        }
        // если у нас одно число больше другого числа по разрядности
        //  очень не красивое дублирование кода, надо убрать!

        if (maxIndex != minIndex) {
            if (conA.length > conB.length) {
                for (int i = maxIndex - minIndex - 1; i >= 0; i--) {
                    int res = conA[i] + tmp;
                    if (res == 2) {
                        resultList.add(-1);
                        tmp = 1;
                        res = 0;
                    } else if (res == -2) {
                        resultList.add(1);
                        tmp = -1;
                        res = 0;
                    } else {
                        resultList.add(res);
                        tmp = 0;
                    }
                }
            } else {
                for (int i = maxIndex - minIndex - 1; i >= 0; i--) {
                    int res = conB[i] + tmp;
                    if (res == 2) {
                        resultList.add(-1);
                        tmp = 1;
                        res = 0;
                    } else if (res == -2) {
                        resultList.add(1);
                        tmp = -1;
                        res = 0;
                    } else {
                        resultList.add(res);
                        tmp = 0;
                    }
                }
            }
        }
        // при ситуации, когда у нас после сложения чисел формируется еще один разряд
        // (результат больше по разрядности чем слагаемые)
        if (tmp != 0){
            resultList.add(tmp);
        }
        Collections.reverse(resultList);
        return resultList;
    }

    // тут просто перевернем любое число и произведем вычисление
    public ArrayList<Integer> subtraction (){
        if (conB[0] == 1){
        conB = reverse(conB);
        } else if (conA[0]  == -1 & conB[0] == -1){
            if(convertToTenSystemCount(expressionA) > convertToTenSystemCount(expressionB)){
                conB = reverse(conB);
            } else {
                conA = reverse(conA);
            }
        } else if (conB[0] == -1){
            conB = reverse(conB);
        }
        return addition();
    }

    public String getResult (){
        StringBuilder res = new StringBuilder();
        ArrayList<Integer> result =  new ArrayList<Integer>();
        if(act == '+'){
            result = addition();
        } else {
            result = subtraction();
        }
        for(Integer znach : result){
            if (znach == 1){
                res.append('+');
            } else if (znach == -1){
                res.append('-');
            } else {
                res.append('0');
            }
        } return res.toString();
    }

    public void  getSolution (){
        System.out.println("Введите выражение в троично-симметричной системе счисления");
        Scanner scanner = new Scanner(System.in);
        String expression = scanner.next();
        parser(expression);
        converterForAB();
        System.out.println(convertToTenSystemCount(expressionA) + " Число А");
        System.out.println(convertToTenSystemCount(expressionB) + " Число Б");
        if (act == '+') {
            System.out.println("Результат вычисления в десятичной системе счисления ---> " + testPlus());
        } else {
            System.out.println("Результат вычисления в десятичной системе счисления ---> " + testMinus());
        }

        String result = getResult();
        while (result.charAt(0) == '0'){
            result = result.substring(1);
        }
        System.out.println("Результат: " + result);
        System.out.println("Проверка: переводим полученное число в десятичную систему счисления");
        System.out.println(convertToTenSystemCount(result));

    }

    //для проверки
    public int convertToTenSystemCount (String str){
        char[] array = str.toCharArray();
        int number = 0;
        for (int i = 0, j = array.length-1; i < array.length ; i++, j--) {
            if (array[i] == '+'){
                number  += 1*Math.pow(3,j);
            } else if (array[i] == '-') {
                number += -1*Math.pow(3,j);
            }
        } return number;
    }

    //для проверки
    public int testMinus(){
        int a = convertToTenSystemCount(expressionA);
        int b = convertToTenSystemCount(expressionB);
        return a - b;
    }
    public int testPlus(){
        int a = convertToTenSystemCount(expressionA);
        int b = convertToTenSystemCount(expressionB);
        return a + b;
    }



}

package com.company;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.math.BigInteger;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

    public static void main(String[] args) {
	MyBigInt a = new MyBigInt("925437632");
    MyBigInt b = new MyBigInt("4782561"); // 74612 * 990 results in leading 0
    MyBigInt c = new MyBigInt("8");
    MyBigInt d = new MyBigInt("17"); // 74612 * 990 results in leading 0
    resultTablesBigFib();





    }
    public static void resultTablesBigFib() {
        System.out.println("FibMatrixBig");
        System.out.format("%18s %18s %18s %18s %18s %18s %18s",
                "N(input size)", "X(input value)", "fib(X)",
                "Time", "10x Ratio","X Expected 10x Ratio", "N Expected 10x Ratio\n");

        int maxN = 9999999;
        MyBigInt operand1 = new MyBigInt("0");
        MyBigInt operand2 = new MyBigInt("0");
        MyBigInt result = new MyBigInt("0");
        long[] times = new long[maxN];
        long start = 0, stop = 0;
        int x = 0;

        for (int N = 1; N < maxN; N++) {

            for(int i = 1; i <= 9; i++){
                x = (int)Math.pow(10, N - 1) * i;
                operand1 = new MyBigInt(String.valueOf(x));

                // Take times and perform function
                start = System.nanoTime();
                result =  fibMatrixBig(operand1);
                stop = System.nanoTime();

                // Add time to time array and print out results from current round of testing
                times[x] = stop - start;
                System.out.format("%18s %18s %18s %18s %18.2f %18s %18s",
                        N, operand1.AbbreviatedValue(), result.AbbreviatedValue(),
                        times[x], (N >= 2) ? (float) times[x] / (float) times[x / 10] : 0,
                        "100", "100\n");


            }
        }
    }
    public static void resultTablesBigInt(){
        System.out.println("Multiplication");
        System.out.format("%18s %18s %18s %18s %18s %18s",
                            "N(input size)", "X1(input value)", "X2(input value)",
                            "Time", "Doubling Ratio", "Expected 2x Ratio\n");

        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        int min = 1, max = 9, maxN = 9999999;
        MyBigInt operand1 = new MyBigInt("0");
        MyBigInt operand2 = new MyBigInt("0");
        long[] times = new long[maxN];
        long start = 0, stop = 0;

        for(int N = 1; N < maxN; N = N * 2) {
            for (int i = 0; i < N; i++) {
                //Fill array with random int between min and max
                //Used top comment for random number generator https://stackoverflow.com/questions/363681/how-do-i-generate-random-integers-within-a-specific-range-in-java
                sb1.append(ThreadLocalRandom.current().nextInt(min, max));
                sb2.append(ThreadLocalRandom.current().nextInt(min, max));
            }
            operand1 = new MyBigInt(sb1.toString());
            operand2 = new MyBigInt(sb2.toString());

            // Take times and perform function
            start = System.nanoTime();
            operand1.Times(operand2);
            stop = System.nanoTime();

            // Add time to time array and print out results from current round of testing
            times[N] = stop - start;
            System.out.format("%18s %18s %18s %18s %18.2f %18s",
                    N, operand1.AbbreviatedValue(), operand2.AbbreviatedValue(),
                    times[N], (N >= 2) ? (float)times[N]/(float)times[N/2]: 0, "4\n");

            // Clear Stringbuilders
            sb1.setLength(0);
            sb2.setLength(0);
        }

    }

    public static MyBigInt fibMatrixBig(MyBigInt X){
        BigInteger bi = new BigInteger(X.Value());
        String binary = bi.toString(2); // Get binary string of X

        MyBigInt[] resultBI = {
                new MyBigInt("1"), new MyBigInt("0"),
                new MyBigInt("0"), new MyBigInt("1")
        };

        MyBigInt[] expBI = {
                new MyBigInt("1"), new MyBigInt("1"),
                new MyBigInt("1"), new MyBigInt("0")
        };

        // Identity matrix
        MyBigInt[][] resultMatrix = {
                {resultBI[0],resultBI[1]},
                {resultBI[2],resultBI[3]}
        };
        // Matrix to be exponentiated, starts at X=1 or M^1
        MyBigInt[][] expMatrix = {
                {expBI[0],expBI[1]},
                {expBI[2],expBI[3]}
        };
        int length = binary.length();

        // Loop from the end of the binary string to the beginning
        // expMatrix tracks the current exponentiated matrix (M^1, M^2, M^4, M^8, etc) for the binary digit being checked
        // If binary digit being checked is a '1', multiply resultMatrix with expMatrix
        for(int i = length - 1; i >= 0; i--){
            if(length > 1 && i != length - 1)
                expMatrix = matrixMult(expMatrix, expMatrix);

            if (binary.charAt(i) == '1')
                resultMatrix = matrixMult(resultMatrix,expMatrix);
        }
        // Return top right value of matrix
        // This is a slight variation from the notes, but the general idea is the same
        return resultMatrix[0][1];
    }

    public static MyBigInt[][] matrixMult(MyBigInt[][] a, MyBigInt[][] b){
        MyBigInt[][] c = new MyBigInt[2][2]; // Result

        // 2x2 Matrix Multiplication
        c[0][0] = a[0][0].Times(b[0][0]).Plus(a[0][1].Times(b[1][0]));
        c[0][1] = a[0][0].Times(b[0][1]).Plus(a[0][1].Times(b[1][1]));
        c[1][0] = a[1][0].Times(b[0][0]).Plus(a[1][1].Times(b[1][0]));
        c[1][1] = a[1][0].Times(b[0][1]).Plus(a[1][1].Times(b[1][1]));


        return c;
    }

    public static MyBigInt fibLoopBig(MyBigInt X){
        MyBigInt a = new MyBigInt("0"); // X - 2 (two fibonacci values behind X)
        MyBigInt b = new MyBigInt("1"); // X - 1 (one fibonacci value behind X)
        MyBigInt c = new MyBigInt("1"); // X

        MyBigInt plusOne = new MyBigInt("1"); // X

        // If X >= 2, loop through X-2 times
        for(MyBigInt i = new MyBigInt("1"); X.greaterThan(i) == 1; i = i.Plus(plusOne)){
            c = a.Plus(b); // Sum previous two fibonacci values
            a = b; // X - 2 variable becomes X - 1 value for next loop
            b = c; // X - 1 variable becomes X value for next loop
            //System.out.format("%s %s %d\n", X.value, i.value, X.greaterThan(i));
        }
        return c;
    }

}

class MyBigInt{
    String value; // String representation of the MyBigInt

    int greaterThan(MyBigInt b){
        MyBigInt a = new MyBigInt(this.value);
        int isGreater = 0;

        // If the number is longer than the other, it is greater
        // Otherwise, compare the largest place values to the smallest, the first with a larger value is greater
        if(a.value.length() > b.value.length()){
            isGreater = 1;
        }
        else if(a.value.length() == b.value.length()){
            for (int i = 0; i < a.value.length(); i++){
                if(a.value.charAt(i) > b.value.charAt(i))
                    return 1;
                else if(a.value.charAt(i) < b.value.charAt(i))
                    return 0;
            }
        }

        return isGreater;
    }


    MyBigInt Times(MyBigInt b){


        MyBigInt TimesCurrent = new MyBigInt("");
        MyBigInt TimesSum = new MyBigInt("0");
        int dA, dB, dC, carry = 0, j, k = 0;
        String c = "0".repeat(Math.max(this.value.length(),b.value.length())*2);
        StringBuilder sb = new StringBuilder(c);
        int aLength = this.value.length();
        int bLength = b.value.length();
        int cLength = c.length();

        for(int i = aLength - 1; i >=0; i--){

            for(j = bLength - 1; j >=0; j--){
                dA = convertToInt(this.value.charAt(i));
                dB = convertToInt(b.value.charAt(j));
                dC = dA * dB + carry;
                carry = 0;
                if(dC >= 10){
                    carry = dC / 10;
                    dC -= carry * 10;
                }
                // Prepend the result of the multiplication to the c string
                sb.setCharAt(cLength - 1 - (aLength - 1 - i) - k,convertToChar(dC));
                k++;
            }
            // Add the carry
            if(carry > 0)
                sb.setCharAt(cLength - 1 - (aLength - 1 - i) - k,convertToChar(carry));

            // Clear carry and k
            carry = 0;
            k = 0;

            // Set
            TimesCurrent.value = sb.toString();
            TimesSum = TimesSum.Plus(TimesCurrent);

            // Reset stringbuilder to the "zeroes" string c
            sb = new StringBuilder(c);

        }

        // Remove leading 0's while leaving at least 1 0
        // Source - Top comment: https://stackoverflow.com/questions/2800739/how-to-remove-leading-zeros-from-alphanumeric-text
        TimesSum.value = TimesSum.value.replaceFirst("^0+(?!$)", "");
        return TimesSum;
    }
    MyBigInt Plus(MyBigInt x){
        String a = this.value;
        String b = x.value;

        // Prepend "0"s to the beginning of the value with the smaller digits
        // to make both values the same # of digits long
        String zeroes = "0".repeat(Math.max(a.length() - b.length(),b.length() - a.length()));
        if(a.length() < b.length()){
            a = zeroes.concat(a);

        }
        else if(a.length() > b.length()){
            b = zeroes.concat(b);
        }



        int carry = 0;
        int dA, dB, dC;
        String c = "0".repeat(Math.max(a.length(),b.length())+1); // string with max length filled with 0's
        StringBuilder sb = new StringBuilder(c);
        int i;

        // Iterate through the two values adding the i-th characters and carrying the remainder forward
        for(i = a.length() - 1; i >= 0; i--){
            dA = convertToInt(a.charAt(i));
            dB = convertToInt(b.charAt(i));
            dC = dA + dB + carry;
            carry = 0;
            if(dC >= 10){
                carry = 1;
                dC -= 10;
            }
            // Prepend the result of the addition to the c string
            sb.setCharAt(i+1,convertToChar(dC));
        }
        // If the last addition from the above loop has a carry, prepend it
        if(carry > 0)
            sb.setCharAt(i+1,convertToChar(carry));

        // Create big int from result string c
        MyBigInt bigC = new MyBigInt(sb.toString());

        // Remove leading 0's while leaving at least 1 0
        // Source - Top comment: https://stackoverflow.com/questions/2800739/how-to-remove-leading-zeros-from-alphanumeric-text
        bigC.value = bigC.value.replaceFirst("^0+(?!$)", "");

        return bigC;

    }


    MyBigInt(String a){
        this.value = a;
    }

    String Value(){
        return this.value;
    }

    String AbbreviatedValue(){
        if(this.value.length() < 12)
            return this.value;
        else
            return this.value.substring(0,5) + "..." + this.value.substring(this.value.length()-5,this.value.length());

    }

    int convertToInt(char a){
        return a - 48;
    }

    char convertToChar(int a){
        return (char)(a + 48);
    }

}
/* TESTING CODE
    MyBigInt a = new MyBigInt("925437632");
    MyBigInt b = new MyBigInt("4782561"); // 74612 * 990 results in leading 0
    MyBigInt c = new MyBigInt("0");
    MyBigInt d = new MyBigInt("4"); // 74612 * 990 results in leading 0
    System.out.println(c.AbbreviatedValue());
            int j = 8341;

            for (int i = 200; i < 210; i++){
        MyBigInt mult1 = new MyBigInt(String.valueOf(i));
        //MyBigInt mult2 = new MyBigInt(String.valueOf(j));
        //a = a.Times(mult1);
        //b = b.Times(mult2);

        //BigInteger a1 = new BigInteger(a.Value());
        //BigInteger b1 = new BigInteger(b.Value());

        //System.out.format("X1: %s\nX2: %s\n", a.AbbreviatedValue(), b.AbbreviatedValue());

        //a1 = a1.add(b1);
        //d = new MyBigInt(a1.toString());
        c = fibLoopBig(mult1);
        d = fibMatrixBig(mult1);

        //System.out.format("MyBigInt: %s\nJava BI: %s\n---------\n", c.AbbreviatedValue(), d.AbbreviatedValue());
        System.out.format("X: %s\nFibLoopBig: %s\nFibMatrixBig: %s\n---------\n",
        mult1.AbbreviatedValue(), c.AbbreviatedValue(), d.AbbreviatedValue());
        j++;

        }*/

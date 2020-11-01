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
    d = d.Times(c);
    System.out.println(d.AbbreviatedValue());
    resultTablesBigInt();





    }
    public static void resultTablesBigInt(){
        System.out.println("Addition");
        System.out.format("%18s %18s %18s %18s %18s %18s",
                            "N(input size)", "X1(input value)", "X2(input value)",
                            "Time", "Doubling Ratio", "Expected 2x Ratio\n");

        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        int min = 1, max = 9, maxN = 9999999;
        MyBigInt operand1 = new MyBigInt("0");
        MyBigInt operand2 = new MyBigInt("0");
        MyBigInt result = new MyBigInt("0");
        long[] times = new long[maxN];
        long start = 0, stop = 0;

        for(int N = 1; N < maxN; N = N * 2) {
            for (int i = 0; i < N; i++) {
                //Fill array with random char between minV and maxV
                //Used top comment for random number generator https://stackoverflow.com/questions/363681/how-do-i-generate-random-integers-within-a-specific-range-in-java
                sb1.append(ThreadLocalRandom.current().nextInt(min, max));
                sb2.append(ThreadLocalRandom.current().nextInt(min, max));
            }
            operand1 = new MyBigInt(sb1.toString());
            operand2 = new MyBigInt(sb2.toString());


            //start = getCpuTime();
            start = System.nanoTime();
            operand1.Times(operand2);

            //stop = getCpuTime();
            stop = System.nanoTime();

            times[N] = stop - start;
            System.out.format("%18s %18s %18s %18s %18.2f %18s",
                    N, operand1.AbbreviatedValue(), operand2.AbbreviatedValue(),
                    times[N], (N >= 2) ? (float)times[N]/(float)times[N/2]: 0, "2\n");

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
    public static MyBigInt[][] matrixMult2(MyBigInt[][] x, MyBigInt[][] y){
        BigInteger[][] c = new BigInteger[2][2];
        BigInteger[][] a = {
                {new BigInteger(x[0][0].Value()),new BigInteger(x[0][1].Value())},
                {new BigInteger(x[1][0].Value()),new BigInteger(x[1][1].Value())}
        };
        BigInteger[][] b = {
                {new BigInteger(y[0][0].Value()),new BigInteger(y[0][1].Value())},
                {new BigInteger(y[1][0].Value()),new BigInteger(y[1][1].Value())}
        };


        // 2x2 Matrix Multiplication
        c[0][0] = a[0][0].multiply(b[0][0]).add(a[0][1].multiply(b[1][0]));
        c[0][1] = a[0][0].multiply(b[0][1]).add(a[0][1].multiply(b[1][1]));
        c[1][0] = a[1][0].multiply(b[0][0]).add(a[1][1].multiply(b[1][0]));
        c[1][1] = a[1][0].multiply(b[0][1]).add(a[1][1].multiply(b[1][1]));

        MyBigInt[][] cBI = {
                {new MyBigInt(c[0][0].toString()),new MyBigInt(c[0][1].toString())},
                {new MyBigInt(c[1][0].toString()),new MyBigInt(c[1][1].toString())}
        };

        return cBI;
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

        // If X >= 2, loop through X-2 times ??????????????
        for(MyBigInt i = new MyBigInt("1"); X.greaterThan(i) == 1; i = i.Plus(plusOne)){
            c = a.Plus(b); // Sum previous two fibonacci values
            a = b; // X - 2 variable becomes X - 1 value for next loop
            b = c; // X - 1 variable becomes X value for next loop
            //System.out.format("%s %s %d\n", X.value, i.value, X.greaterThan(i));
        }
        return c;
    }
    /** Get CPU time in nanoseconds since the program(thread) started. */
    /** from: http://nadeausoftware.com/articles/2008/03/java_tip_how_get_cpu_and_user_time_benchmarking#TimingasinglethreadedtaskusingCPUsystemandusertime **/
    public static long getCpuTime( ) {

        ThreadMXBean bean = ManagementFactory.getThreadMXBean( );

        return bean.isCurrentThreadCpuTimeSupported( ) ?

                bean.getCurrentThreadCpuTime( ) : 0L;

    }

}

class MyBigInt{
    String value; // String representation of the MyBigInt

    int greaterThan(MyBigInt b){
        MyBigInt a = new MyBigInt(this.value);
        int isGreater = 0;

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

    MyBigInt Times3(MyBigInt b){
        MyBigInt a = new MyBigInt(this.value);


        // Prepend "0"s to the beginning of the value with the smaller digits
        // to make both values the same # of digits long
        /*if(a.value.length() < b.value.length()){
            int diff = b.value.length() - a.value.length();

            for(int i = 0; i < diff; i++)
                a.value = "0"+ a.value;
        }
        else if(a.value.length() > b.value.length()){
            int diff = a.value.length() - b.value.length();

            for(int i = 0; i < diff; i++)
                b.value = "0"+ b.value;
        }*/

        MyBigInt TimesCurrent = new MyBigInt("");
        MyBigInt TimesSum = new MyBigInt("0");
        MyBigInt resultC = new MyBigInt("");
        int dA, dB, dC, carry = 0;


        for(int i = a.value.length() - 1; i >=0; i--){

            // Add zeroes for the place value of big int a at the i-th character
            for(int z = a.value.length() - 1 - i; z > 0; z--)
                TimesCurrent.value = "0" + TimesCurrent.value;

            for(int j = b.value.length() - 1; j >=0; j--){
                dA = convertToInt(a.value.charAt(i));
                dB = convertToInt(b.value.charAt(j));
                dC = dA * dB + carry;
                carry = 0;
                if(dC >= 10){
                    carry = dC / 10;
                    dC -= carry * 10;
                }
                // Prepend the result of the multiplication to the c string
                TimesCurrent.value = convertToChar(dC) + TimesCurrent.value;
            }
            TimesSum = TimesSum.Plus(TimesCurrent);
            TimesCurrent.value = "";
        }
        if(carry > 0)
            TimesSum.value = convertToChar(carry) + TimesSum.value;

        // Remove leading 0's while leaving at least 1 0
        // Source - Top comment: https://stackoverflow.com/questions/2800739/how-to-remove-leading-zeros-from-alphanumeric-text
        TimesSum.value = TimesSum.value.replaceFirst("^0+(?!$)", "");
        return TimesSum;
    }
    MyBigInt Times(MyBigInt b){
        //MyBigInt a = new MyBigInt(this.value);

        MyBigInt TimesCurrent = new MyBigInt("");
        MyBigInt TimesSum = new MyBigInt("0");
        int dA, dB, dC, carry = 0, j, k = 0;
        String c = "0".repeat(Math.max(this.value.length(),b.value.length())*2);
        StringBuilder sb = new StringBuilder(c);
        int aLength = this.value.length();
        int bLength = b.value.length();
        int cLength = c.length();

        for(int i = aLength - 1; i >=0; i--){
            // Add zeroes for the place value of big int a at the i-th character
            //for(int z = a.value.length() - 1 - i; z > 0; z--)
              //  TimesCurrent.value = "0" + TimesCurrent.value;

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
                //TimesCurrent.value = convertToChar(dC) + TimesCurrent.value;
                sb.setCharAt(cLength - 1 - (aLength - 1 - i) - k,convertToChar(dC));
                k++;
            }
            if(carry > 0)
                //TimesCurrent.value = convertToChar(carry) + TimesCurrent.value;
                sb.setCharAt(cLength - 1 - (aLength - 1 - i) - k,convertToChar(carry));
            carry = 0;
            k = 0;
            TimesCurrent.value = sb.toString();
            TimesSum = TimesSum.Plus(TimesCurrent);
            //BigInteger TS = new BigInteger(TimesSum.Value());
            //BigInteger TC = new BigInteger(TimesCurrent.Value());
            //TS = TS.add(TC);
            //TimesSum.value = TS.toString();
            //TimesCurrent.value = "";
            sb = new StringBuilder(c);

        }
        //if(carry > 0)
          //  TimesCurrent.value = convertToChar(carry) + TimesCurrent.value;

        // Remove leading 0's while leaving at least 1 0
        // Source - Top comment: https://stackoverflow.com/questions/2800739/how-to-remove-leading-zeros-from-alphanumeric-text
        //TimesSum.value = TimesSum.value.replaceFirst("^0+(?!$)", "");
        return TimesSum;
    }
    MyBigInt Plus(MyBigInt x){
        String a = this.value;
        String b = x.value;

        // Prepend "0"s to the beginning of the value with the smaller digits
        // to make both values the same # of digits long
        String zeroes = "0".repeat(Math.max(a.length() - b.length(),b.length() - a.length()));
        //StringBuilder sbZeroes = new StringBuilder()
        if(a.length() < b.length()){
            a = zeroes.concat(a);

        }
        else if(a.length() > b.length()){
            b = zeroes.concat(b);
        }

        /*if(a.length() < b.length()){
            int diff = b.length() - a.length();

            for(int i = 0; i < diff; i++)
                a = "0" + a;
        }
        else if(a.length() > b.length()){
            int diff = a.length() - b.length();

            for(int i = 0; i < diff; i++)
                b = "0" + b;
        }*/

        int carry = 0;
        int dA, dB, dC;
        String c = "0".repeat(Math.max(a.length(),b.length())+1);
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
            //c = convertToString(dC).concat(c);
            sb.setCharAt(i+1,convertToChar(dC));
        }
        // If the last addition from the above loop has a carry, prepend it
        if(carry > 0)
            //c = convertToString(carry).concat(c);
            sb.setCharAt(i+1,convertToChar(carry));

        // Create big int from result string c
        MyBigInt bigC = new MyBigInt(sb.toString());

        // Remove leading 0's while leaving at least 1 0
        // Source - Top comment: https://stackoverflow.com/questions/2800739/how-to-remove-leading-zeros-from-alphanumeric-text
        bigC.value = bigC.value.replaceFirst("^0+(?!$)", "");

        return bigC;

    }
    MyBigInt Plus2(MyBigInt x){
        String a = this.value;
        String b = x.value;

        BigInteger aBI = new BigInteger(a);
        BigInteger bBI = new BigInteger(b);
        BigInteger cBI = aBI.add(bBI);

        MyBigInt bigC = new MyBigInt(cBI.toString());

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
    String convertToString(int a){
        return String.valueOf(a + 48);
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

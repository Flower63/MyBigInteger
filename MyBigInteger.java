import java.util.Arrays;

/**
 * Created by Dennis on 9/18/2015.
 */
public class MyBigInteger {

    private int[] nums;
    private final int signum;

    /**
     * Ordinary public constructor, which take a value as a String
     * @param input
     */
    public MyBigInteger(String input) {

        if(input.startsWith("-")) {
            signum = -1;
            input = input.substring(1, input.length());
        } else { signum = 1; }

        nums = toIntArray(input);
        if (nums[nums.length - 1] == 0 && nums.length > 1) {
            nums = reduce(nums);
        }
    }

    public MyBigInteger(int value) {
        this(Integer.toString(value));
    }

    /**
     * Private constructor for inner purposes. The
     * @param nums should be in reverse order, as it take place in class
     * @param signum
     */
    private MyBigInteger(int[] nums, int signum) {

        if (nums[nums.length - 1] == 0) {
            nums = reduce(nums);
        }
        this.nums = nums;
        this.signum = signum;
    }

    /**
     * Inverts value's sign. Negative to positive and vice versa
     * @param operand
     * @return
     */
    public static MyBigInteger invert(MyBigInteger operand) {

        int signum = operand.getSignum() == 1 ? -1 : 1;

        return new MyBigInteger(operand.getNums(), signum);
    }

    /**
     * Static method to add values
     * @param operandA
     * @param operandB
     * @return
     */
    public static MyBigInteger add(MyBigInteger operandA, MyBigInteger operandB) {

        int signum;

        if (operandA.getSignum() == -1 && operandB.getSignum() == 1) {
            return subtract(operandB, invert(operandA));
        } else if (operandA.getSignum() == 1 && operandB.getSignum() == -1) {
            return subtract(operandA, invert(operandB));
        } else if (operandA.getSignum() == -1 && operandB.getSignum() == -1) {
            signum = -1;
        } else { signum = 1; }

        int[] aNums = operandA.getNums();
        int[] bNums = operandB.getNums();
        int maxLength = Math.max(aNums.length, bNums.length);
        int[] res = new int[maxLength + 1];
        int tempVal = 0;

        for (int i = 0; i < maxLength; i++) {

            if (i < aNums.length && i < bNums.length) {
                res[i] = tempVal + bNums[i] + aNums[i];
            } else if (i < aNums.length){
                res[i] = tempVal + aNums[i];
            } else if (i < bNums.length) {
                res[i] = tempVal + bNums[i];
            }
            tempVal = 0;

            if (res[i] > 9) {
                res[i] -= 10;
                tempVal = 1;
            }
        } res[maxLength] = tempVal;

        return new MyBigInteger(res, signum);
    }

    /**
     * Static method to subtract values
     * @param operandA
     * @param operandB
     * @return
     */
    public static MyBigInteger subtract(MyBigInteger operandA, MyBigInteger operandB) {

        if (operandA.equals(operandB)) {
            return new MyBigInteger("0");
        }

        int signum;

        if (operandA.getSignum() == 1 && operandB.getSignum() == -1) {
            return add(operandA, invert(operandB));
        } else if (operandA.getSignum() == -1 && operandB.getSignum() == 1) {
            return add(operandA, invert(operandB));
        } else if (operandA.getSignum() == -1 && operandB.getSignum() == -1) {
            return add(operandA, invert(operandB));
        }

        if (operandA.biggerThan(operandB)) {
            signum = 1;
        } else {
            signum = -1;
            MyBigInteger tmp = operandA;
            operandA = operandB;
            operandB = tmp;
        }

        int[] aNums = operandA.getNums();
        int[] bNums = operandB.getNums();
        int maxLength = Math.max(aNums.length, bNums.length);
        int[] res = new int[maxLength];
        int tempVal = 0;

        for (int i = 0; i < maxLength; i++) {

            if (i < aNums.length && i < bNums.length) {
                res[i] = aNums[i] - bNums[i] - tempVal;
            } else if (i < aNums.length){
                res[i] = aNums[i] - tempVal;
            } else if (i < bNums.length) {
                res[i] = bNums[i] - tempVal;
            }

            tempVal = 0;

            if (res[i] < 0) {
                res[i] += 10;
                tempVal = 1;
            }
        }

        return new MyBigInteger(res, signum);
    }

    private static int[] subtract(int[] aNums, int[] bNums) {
        int maxLength = Math.max(aNums.length, bNums.length);
        int[] res = new int[maxLength];
        int tempVal = 0;

        for (int i = 0; i < maxLength; i++) {

            if (i < aNums.length && i < bNums.length) {
                res[i] = aNums[i] - bNums[i] - tempVal;
            } else if (i < aNums.length){
                res[i] = aNums[i] - tempVal;
            } else if (i < bNums.length) {
                res[i] = bNums[i] - tempVal;
            }

            tempVal = 0;

            if (res[i] < 0) {
                res[i] += 10;
                tempVal = 1;
            }
        }

        res = reduce(res);
        return res;
    }

    private static int[] maxArray(int[] a, int[] b) {
        if (a.length > b.length) {
            return a;
        } else if (a.length < b.length) {
            return b;
        } else {

            for(int i = a.length - 1; i >= 0; i--) {
                if (a[i] > b[i]) {
                    return a;
                } else if (a[i] < b[i]) {
                    return b;
                }
            } return a;
        }
    }

    /**
     * Multiplies two values
     * @param operandA
     * @param operandB
     * @return
     */
    public static MyBigInteger multiply(MyBigInteger operandA, MyBigInteger operandB) {

        int signum;

        if(operandA.getSignum() != operandB.getSignum()) {
            signum = -1;
        } else {
            signum = 1;
        }

        int[] aNums = operandA.getNums();
        int[] bNums = operandB.getNums();
        int[] res = new int[aNums.length + bNums.length];
        int tempVal = 0;

        for(int i = 0; i < aNums.length; i++) {
            for (int j = 0; j < bNums.length; j++) {
                res[i + j] += aNums[i] * bNums[j] + tempVal;

                tempVal = 0;

                if (res[i + j] > 9) {
                    tempVal = res[i + j] / 10;
                    res[i + j] %= 10;
                }

            } res[bNums.length + i] += tempVal;
            tempVal = 0;
        }

        return new MyBigInteger(res, signum);
    }

    //TODO divide() is soooooo slow. Fix this
    public static MyBigInteger divide(MyBigInteger operandA, MyBigInteger operandB) {

        if (operandB.biggerThan(operandA))
        {
            return new MyBigInteger("0");
        } else if (Arrays.equals(operandA.getNums(), operandB.getNums())) {
            return new MyBigInteger("1");
        }

        int[] aNums = operandA.getNums();
        int[] bNums = operandB.getNums();
        int count = 0;

        while (aNums == maxArray(aNums, bNums)) {
            aNums = subtract(aNums, bNums);
            count++;
        }

        return new MyBigInteger(count);
    }

    /**
     * Returns minor value
     * @param operandA
     * @param operandB
     * @return
     */
    public static MyBigInteger min(MyBigInteger operandA, MyBigInteger operandB) {

        if (operandA.getSignum() != operandB.getSignum()) {
            return operandA.getSignum() == -1 ? operandA : operandB;
        }

        return operandA.biggerThan(operandB) ? operandB : operandA;
    }

    /**
     * Returns bigger value
     * @param operandA
     * @param operandB
     * @return
     */
    public static MyBigInteger max(MyBigInteger operandA, MyBigInteger operandB) {

        if (operandA.getSignum() != operandB.getSignum()) {
            return operandA.getSignum() == 1 ? operandA : operandB;
        }

        return operandA.biggerThan(operandB) ? operandA : operandB;
    }

    /**
     *
     * @param input
     * @return
     */
    private int[] toIntArray(String input) {

        char[] temp = input.toCharArray();
        int[] result = new int[temp.length];
        int length = temp.length;

        for (int i = 0; i < length; i++) {
            result[i] = Character.getNumericValue(temp[length - 1 - i]);
        }

        return result;
    }

    //Debug purposes, never mind, keep scrolling
    private static void showArray (int[] array) {
        for (int x : array) {
            System.out.print(x);
        }

        System.out.println();
    }

    /**
     * Defines if parameter value bigger than this one
     * @param operand
     * @return
     */
    private boolean biggerThan(MyBigInteger operand) {

        if (this.nums.length > operand.getNums().length) {
            return true;
        } else if (this.nums.length < operand.getNums().length) {
            return false;
        } else {
            int[] tmp = operand.getNums();

            for(int i = nums.length - 1; i >= 0; i--) {
                if (nums[i] > tmp[i]) {
                    return true;
                } else if (nums[i] < tmp[i]) {
                    return false;
                }
            } return false;
        }
    }

    /**
     * Finds and eliminates zeros before actually value
     * @param input
     * @return
     */
    private static int[] reduce(int[] input) {
        int index = 0;
        while (input[input.length - index - 1] == 0 && index < input.length - 1) {
            index++;
        }

        int[] res = new int[input.length - index];

        for (int i = 0; i < res.length; i++) {
            res[i] = input[i];
        }

        return res;
    }

    public int getSignum() { return signum; }

    public int[] getNums() { return nums; }

    @Override
    public String toString() {

        StringBuilder result = new StringBuilder();

        if(signum == -1) {
            result.append("-");
        }

        for (int i = nums.length - 1; i >= 0; i--) {
            result.append(nums[i]);
        }
        return result.toString();
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) return false;

        if (obj == this) return true;

        if (obj instanceof MyBigInteger) {
            MyBigInteger tmp = (MyBigInteger) obj;

            return tmp.getSignum() == this.getSignum() && Arrays.equals(tmp.getNums(), this.getNums());
        }
        return false;
    }
}

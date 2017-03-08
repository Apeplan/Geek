package com.simon.listeners;

/**
 * describe:
 *
 * @author Apeplan
 * @date 2017/2/17
 * @email hanzx1024@gmail.com
 */

public class aaa {

    public static int binSearch(int[] array, int key) {
        int mid = array.length >> 1;
        if (key == array[mid]) {
            return mid;
        }

        int start = 0;
        int end = array.length - 1;
        while (start <= end) {
            mid = (end - start) >> 1 + start;
            if (key < array[mid]) {
                end = mid - 1;
            } else if (key > array[mid]) {
                start = mid + 1;
            } else {
                return mid;
            }
        }

        return -1;
    }

    public static int binSearch(int[] array, int key, int start, int end) {
        int mid = (end - start) >> 1 + start;
        if (key == array[mid]) {
            return mid;
        }

        if (start >= end) {
            return -1;
        } else if (key > array[mid]) {
            return binSearch(array, key, mid + 1, end);
        } else if (key < array[mid]) {
            return binSearch(array, key, start, mid - 1);
        } else {
            return -1;
        }

    }

    /**
     * 冒泡排序基本概念是：
     * 依次比较相邻的两个数，将小数放在前面，大数放在后面。
     * 即在第一趟：首先比较第1个和第2个数，将小数放前，大数放后。
     * 然后比较第2个数和第3个数，将小数放前，大数放后，如此继续，
     * 直至比较最后两个数，将小数放前，大数放后。至此第一趟结束，
     * 将最大的数放到了最后。在第二趟：仍从第一对数开始比较
     * （因为可能由于第2个数和第3个数的交换，使得第1个数不再小于第2个数），
     * 将小数放前，大数放后，一直比较到倒数第二个数（倒数第一的位置上已经是最大的），
     * 第二趟结束，在倒数第二的位置上得到一个新的最大数
     * （其实在整个数列中是第二大的数）。如此下去，重复以上过程，直至最终完成排序。
     *
     * @param array 要排序的数组
     */
    public static void bubbleSort(int[] array) {
        int temp;
        int length = array.length;
        for (int i = 0; i < length - 1; i++) {
            for (int j = i + 1; j < length; j++) {
                if (array[i] < array[j]) {
                    temp = array[i];
                    array[i] = array[j];
                    array[j] = temp;
                }
            }
        }
    }

    /**
     * 快速排序：
     * 一趟快速排序的算法是：
     * 1）设置两个变量i、j，排序开始的时候：i=0，j=N-1；
     * 2）以第一个数组元素作为关键数据，赋值给key，即 key=A[0]；
     * 3）从j开始向前搜索，即由后开始向前搜索（j=j-1即j--），
     * 找到第一个小于key的值A[j]，A[i]与A[j]交换；
     * 4）从i开始向后搜索，即由前开始向后搜索（i=i+1即i++），
     * 找到第一个大于key的A[i]，A[i]与A[j]交换；
     * 5）重复第3、4、5步，直到 I=J；
     * (3,4步是在程序中没找到时候j=j-1，i=i+1，直至找到为止。
     * 找到并交换的时候i， j指针位置不变。
     * 另外当i=j这过程一定正好是i+或j-完成的最后令循环结束。）
     */
    public static void quickSort(int[] array, int start, int end) {
        if (start < end) {
            int base = array[start]; // 选定的基准值
            int temp; // 记录中间值
            int i = start, j = end;
            do {
                while ((array[i] < base) && (i < end)) {
                    i++;
                }
                while ((array[j] > base) && (j > start)) {
                    j--;
                }

                if (i <= j) {
                    temp = array[i];
                    array[i] = array[j];
                    array[j] = temp;
                    i++;
                    j--;
                }
            } while (i <= j);

            if (start < j) {
                quickSort(array, start, j);
            }
            if (end > i) {
                quickSort(array, i, end);
            }
        }
    }

}

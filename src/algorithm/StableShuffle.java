package algorithm;

import java.util.Arrays;
import java.util.Random;

/**
 * @author huhai
 * @date 2015/4/20 11:07
 */
public class StableShuffle<T> {

    /**
     * 随机打乱数组中的数据（洗牌）
     * @param arr Array 数组
     */
    public void shuffle(T[] arr){
        if(null == arr || arr.length == 0){
            return;
        }
        Random random = new Random();
        for(int i = 0; i < arr.length; i++){
            exchange(i, arr, random);
        }
    }

    /**
     * 在没交换过的元素中，随机获取一个位置，并与没有交换过的最后一个元素交换位置
     * @param index int
     * @param arr Array 数组
     * @param random Random 随机数生成器
     */
    private void exchange(int index, T[] arr, Random random){
        int surplusLength = arr.length - index;
        int lastIndex = surplusLength - 1;
        T last = arr[lastIndex];
        int r = random.nextInt(surplusLength);
        arr[lastIndex] = arr[r];
        arr[r] = last;
    }

    public static void main(String[] args){
        StableShuffle<Integer> stableShuffle = new StableShuffle<Integer>();
        Integer[] arr = {1, 2, 3, 4, 5};
        stableShuffle.shuffle(arr);
        System.out.println(Arrays.toString(arr));
    }
}

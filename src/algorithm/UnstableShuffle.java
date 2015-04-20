package algorithm;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author huhai
 * @date 2015/4/17 17:33
 */
public class UnstableShuffle {
    Set<Integer> indexLog = new HashSet<>();

    private void exchange(int i, Object[] arr, boolean logBoth){
        while (true){
            double random = Math.random();
            int newIndex = 0;
            if(arr.length < 10){
                newIndex = (int)(random * 10) % arr.length;
            }else{
                newIndex = (int)(random * arr.length % arr.length);
            }

            if(newIndex  != i && !indexLog.contains(newIndex)){
                Object temp = arr[i];
                arr[i] = arr[newIndex];
                arr[newIndex] = temp;
                if(logBoth){
                    indexLog.add(i);
                    indexLog.add(newIndex);
                }else{
                    if(random * 10 % 2 == 0){
                        indexLog.add(i);
                    }else {
                        indexLog.add(newIndex);
                    }
                }
                break;
            }
        }
    }

    public void shuffle(Object[] arr){
        indexLog.clear();
        if(null != arr){
            //如果牌的总数量是奇数，将最后一张随意放到其他位置
            if(arr.length % 2 != 0){
                exchange(arr.length -1 , arr, false);
            }
            for(int i = 0; i< arr.length && indexLog.size() != arr.length; i++){
                if(indexLog.contains(i)){
                    continue;
                }
                exchange(i, arr, true);
            }
        }
    }

    public static void main(String[] args){
        UnstableShuffle unstableShuffle = new UnstableShuffle();
        Object[] arr = {1, 2, 3, 4, 5};
        unstableShuffle.shuffle(arr);
        System.out.println(Arrays.toString(arr));
    }
}

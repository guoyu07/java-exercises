package algorithm;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * @author huhai
 * @date 2015/4/20 11:40
 */
public class StableRandomAccessArray<T> implements Iterable<T>{
    private T[] arr;
    /**
     * 保存当前索引位置
     */
    private int index = 0;
    private Random random;

    public StableRandomAccessArray(T[] arr){
        if(null != arr && arr.length > 0){
            this.arr = Arrays.copyOf(arr, arr.length);
            random = new Random();
        }
    }

    public T randomNext(){
        if(index == arr.length){
            throw new ArrayIndexOutOfBoundsException("all item have been retrieved");
        }
        if(null == arr){
            return  null;
        }
        int surplusLength = arr.length - index++;
        if(surplusLength == 1){
            return arr[surplusLength - 1];
        }
        int r = random.nextInt(surplusLength);
        T result = arr[r];
        arr[r] = arr[surplusLength - 1];
        return result;
    }

    class StableRandomAccessArrayIterator<T> implements Iterator<T>{
        private StableRandomAccessArray<T> stableRandomAccessArray;
        public StableRandomAccessArrayIterator(StableRandomAccessArray<T> stableRandomAccessArray){
            this.stableRandomAccessArray = stableRandomAccessArray;
        }

        @Override
        public boolean hasNext() {
            return stableRandomAccessArray.index < stableRandomAccessArray.arr.length;
        }

        @Override
        public T next() {
            return stableRandomAccessArray.randomNext();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void forEachRemaining(Consumer action) {
            for (int i = 0; i < stableRandomAccessArray.arr.length - index; i++){
                action.accept(stableRandomAccessArray.arr[i]);
            }
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new StableRandomAccessArrayIterator<>(this);
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        for(int i = 0; i < arr.length; i++){
            action.accept(randomNext());
        }
    }

    @Override
    public Spliterator<T> spliterator() {
        return null;
    }

    public static void main(String[] args){
        Integer[] arr = {1, 2, 3, 4, 5};
        StableRandomAccessArray<Integer> randomAccessArray = new StableRandomAccessArray<Integer>(arr);
        for(Integer item : randomAccessArray){
            System.out.print(item);
        }
    }
}

package seawargame.util;

import java.util.List;

public class Utils {

    /** Выбрать случайно один из элементов коллекции
     */
    public static <T> T chooseOne(T[] arr ) {
        if(arr.length == 0) {
            return null;
        }
        // любую по индексу
        int index = (int)(Math.random() * arr.length);

        return arr[index];
    }

    /** Выбрать случайно один из элементов коллекции
     */
    public static <T> T chooseOne(List<T> arr ) {
        if(arr.isEmpty()) {
            return null;
        }
        // любую по индексу
        int index = (int)(Math.random() * arr.size());

        return arr.get(index);
    }
}

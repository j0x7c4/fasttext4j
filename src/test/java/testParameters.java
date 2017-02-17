import com.ymatou.atc.fastText4j.Heap;
import com.ymatou.atc.fastText4j.Prediction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by fujie on 2017/2/17.
 */
public class testParameters {

    public static void main (String argv[]) {
        ArrayList<Prediction> arr = new ArrayList(Arrays.asList(new Prediction(20,1),
                new Prediction(5,2),new Prediction(30,1),new Prediction(20,1),new Prediction(15,1)));

        //Heap.adjustHeap(arr, 0, arr.size());
        Heap.heapSort(arr);
        for (Prediction p : arr) {
            System.out.println(p.getFirst());
        }
        System.out.println("**************");

//        Heap.heapSort(arr);
//        for (Prediction p : arr) {
//            System.out.println(p.getFirst());
//        }
//        System.out.println("**************");
    }
}

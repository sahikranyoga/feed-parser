package com.sahikran.hasher;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;

public class MajoritySearch {

    public static void main(String[] args){
        MajoritySearch majoritySearch = new MajoritySearch();
        int result = majoritySearch.findWordOccurringByPercentage(new int[]{4,2,3,4,4,5,4,4,5,4,4}, 1/2);
        System.out.println(result);
    }
    
    public int findWordOccurringByPercentage(int[] arr, int percentage){
        int result = 0;
        Map<Integer, Integer> frequencyMap = new HashMap<>();
        // [4,2,3,4,4,5,4,4,5,4,4], result is 4

        int count = 1;
        int candidate = arr[0];
        for(int i = 0; i < arr.length; i++){
            frequencyMap.put(arr[i], frequencyMap.getOrDefault(arr[i], 0) + 1);
            if(arr[i] == candidate){
                count++;
            } else if (count > 0){
                count--;
            } else {
                candidate = arr[i];
                count = 1;
            }
        }

        Optional<Entry<Integer, Integer>> optionalMaxEntry = frequencyMap.entrySet().stream()
                                                                    .max((e1, e2) -> e1.getValue().compareTo(e2.getValue()));
        result = optionalMaxEntry.get().getKey();

        return result == candidate ? result : -1;
    }
}

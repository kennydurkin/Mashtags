package com.example.android.mashtags;

import java.util.*;

/** Sorting a Map by Value */
public class MapValueSort {

    /**
     * inner class to do sorting of the map *
     */

    private static class ValueComparer implements Comparator {
        private final Map map;

        public ValueComparer(Map map) {
            this.map = map;
        }

        /**
         * Compare to values of a Map
         */
        public int compare(Object key1, Object key2) {
            Integer value1 = (Integer) this.map.get(key1);
            Integer value2 = (Integer) this.map.get(key2);
            int c = value2.compareTo(value1);
            if (c != 0) {
                return c;
            }
            Integer hashCode1 = key1.hashCode();
            Integer hashCode2 = key2.hashCode();
            return hashCode1.compareTo(hashCode2);
        }

    }

    /**
     * Sorts a given Map according to its values and returns a sortedmap
     */
    public static SortedMap getValueSortedMap(Map map) {
        Comparator vc = new MapValueSort.ValueComparer(map);
        SortedMap sm = new TreeMap(vc);
// add all Elements of unsorted Map, otherwise it is empty
        sm.putAll(map);
        return sm;
    }
}
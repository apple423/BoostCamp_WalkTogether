package com.example.han.boostcamp_walktogether.util;

import com.example.han.boostcamp_walktogether.data.FreeboardImageDTO;

import java.util.Comparator;

/**
 * Created by Han on 2017-08-11.
 */

public class ComparatorUtil  {

    public static Comparator<FreeboardImageDTO> imageDTOComparator = new Comparator<FreeboardImageDTO>() {
        @Override
        public int compare(FreeboardImageDTO o1, FreeboardImageDTO o2) {
            return o2.getFreeboard_key() - o1.getFreeboard_key();
        }
    };


}

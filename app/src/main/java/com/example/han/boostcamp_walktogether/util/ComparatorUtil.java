package com.example.han.boostcamp_walktogether.util;

import com.example.han.boostcamp_walktogether.data.CommentAveragePointDTO;
import com.example.han.boostcamp_walktogether.data.FreeboardImageDTO;
import com.example.han.boostcamp_walktogether.data.RecentCommentDTO;

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

    public static Comparator<CommentAveragePointDTO> commentAveragePointDTOComparator = new Comparator<CommentAveragePointDTO>() {
        @Override
        public int compare(CommentAveragePointDTO o1, CommentAveragePointDTO o2) {
            return o1.getPark_key() - o2.getPark_key();
        }
    };



}

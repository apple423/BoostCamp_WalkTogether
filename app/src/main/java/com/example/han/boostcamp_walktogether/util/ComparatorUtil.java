package com.example.han.boostcamp_walktogether.util;

import com.bumptech.glide.Glide;
import com.example.han.boostcamp_walktogether.data.CommentAveragePointDTO;
import com.example.han.boostcamp_walktogether.data.FreeboardCommentDTO;
import com.example.han.boostcamp_walktogether.data.FreeboardDTO;
import com.example.han.boostcamp_walktogether.data.FreeboardImageDTO;
import com.example.han.boostcamp_walktogether.data.ParkListDTO;
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

    public static Comparator<FreeboardDTO> likeDTOComparator = new Comparator<FreeboardDTO>() {
        @Override
        public int compare(FreeboardDTO o1, FreeboardDTO o2) {
            return o2.getFreeboard_key() - o1.getFreeboard_key();
        }
    };

    public static Comparator<FreeboardCommentDTO> commentDTOComparator = new Comparator<FreeboardCommentDTO>() {
        @Override
        public int compare(FreeboardCommentDTO o1, FreeboardCommentDTO o2) {
            return o2.getFreeboard_key() - o1.getFreeboard_key();
        }
    };

    public static Comparator<CommentAveragePointDTO> commentAveragePointDTOComparator = new Comparator<CommentAveragePointDTO>() {
        @Override
        public int compare(CommentAveragePointDTO o1, CommentAveragePointDTO o2) {
            return o1.getPark_key() - o2.getPark_key();
        }
    };

    public static Comparator<ParkListDTO> sortByFavInLocationList = new Comparator<ParkListDTO>() {
        @Override
        public int compare(ParkListDTO o1, ParkListDTO o2) {
            return Math.round(o2.getAvgStar() - o1.getAvgStar());
        }
    };

    public static Comparator<ParkListDTO> sortByAnimalLocationList = new Comparator<ParkListDTO>() {


        @Override
        public int compare(ParkListDTO o1, ParkListDTO o2) {
            return Math.round(o2.getAvgPet() - o1.getAvgPet());
        }
    };

    public static Comparator<ParkListDTO> sortByDistanceLocationList = new Comparator<ParkListDTO>() {
        @Override
        public int compare(ParkListDTO o1, ParkListDTO o2) {
            if(o1.getDistance()>o2.getDistance())
                return 1;
            else if (o1.getDistance() == o2.getDistance())
                return 0;

            else return -1;

        }
    };




}

package com.example.han.boostcamp_walktogether.interfaces;

import com.example.han.boostcamp_walktogether.data.WalkDiaryDTO;
import com.example.han.boostcamp_walktogether.data.WalkDiaryImageDTO;

import java.util.ArrayList;

/**
 * Created by Han on 2017-08-17.
 */

public interface OnClickWalkDiaryButtonInterface {

    void onClickAddDiaryButton(ArrayList<WalkDiaryDTO> walkDiaryDTOs, ArrayList<WalkDiaryImageDTO> walkDiaryImageDTOs);
}

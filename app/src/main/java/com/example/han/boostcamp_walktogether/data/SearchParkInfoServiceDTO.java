package com.example.han.boostcamp_walktogether.data;

import java.util.List;

/**
 * Created by Han on 2017-08-03.
 */

public class SearchParkInfoServiceDTO {

     int list_total_count;
     ResultDTO RESULT;
    List<ParkRowDTO> row;

    public List<ParkRowDTO> getRow() {
        return row;
    }

    public void setRow(List<ParkRowDTO> row) {
        this.row = row;
    }

    public int getList_total_count() {
        return list_total_count;
    }

    public void setList_total_count(int list_total_count) {
        this.list_total_count = list_total_count;
    }

    public ResultDTO getRESULT() {
        return RESULT;
    }

    public void setRESULT(ResultDTO RESULT) {
        this.RESULT = RESULT;
    }

}

package com.wssong.db;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WSS on 2017/4/25.
 */
@Component
public class CreateData {
    public List<String> getStrList(int size){
        if(size <= 0) return new ArrayList<String>();

        List<String> dataList = new ArrayList<String>();

        for (int i = 0 ; i <size; i++){
            dataList.add("第 【 "+i+" 】 条数据!");
        }
        return dataList;
    }
}

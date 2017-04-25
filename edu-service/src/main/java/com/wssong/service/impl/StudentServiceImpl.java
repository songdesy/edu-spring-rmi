package com.wssong.service.impl;

import com.wssong.db.CreateData;
import com.wssong.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by WSS on 2017/4/25.
 */
public class StudentServiceImpl implements StudentService{
    @Autowired
    CreateData createData;
    public List<String> getStrList(int size) {
        return createData.getStrList(size);
    }
}

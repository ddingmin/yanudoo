package com.example.forhackerton.service;

import com.example.forhackerton.data.KtoQListDto;
import com.example.forhackerton.data.QtoQListDto;
import com.example.forhackerton.repository.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SqlService {

    @Autowired
    public Dao dao;

    public List<KtoQListDto> getKtoQList(){
        List<KtoQListDto> List = dao.getKTopList();
        return List;
    }

    public List<QtoQListDto> getQtoQList(){
        List<QtoQListDto> List = dao.getQTopList();
        return List;
    }
}

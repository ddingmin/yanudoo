package com.example.forhackerton.service;

import com.example.forhackerton.data.KtoQListDto;
import com.example.forhackerton.data.KtoQSaveDto;
import com.example.forhackerton.data.QtoQListDto;
import com.example.forhackerton.data.QtoQSaveDto;
import com.example.forhackerton.repository.Dao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SqlService {

    Logger logger = LoggerFactory.getLogger(SqlService.class);
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

    public void QtoQSave(QtoQSaveDto saveDto){
        try{
            dao.QtoQSave(saveDto);
            logger.info("data successfully saved");
        }catch (Exception e){
            logger.info("error occurred in QtoQSave");
            e.printStackTrace();
        }
    }

    public void KtoQSave(KtoQSaveDto saveDto){
        try{
            dao.KtoQSave(saveDto);
            logger.info("data sucessfully saved");
        }catch (Exception e){
            logger.info("error occurred in KtoQ Save");
            e.printStackTrace();
        }
    }
}

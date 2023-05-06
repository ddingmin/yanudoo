package com.example.forhackerton.repository;

import com.example.forhackerton.data.KtoQListDto;
import com.example.forhackerton.data.KtoQSaveDto;
import com.example.forhackerton.data.QtoQListDto;
import com.example.forhackerton.data.QtoQSaveDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Dao {

    public List<QtoQListDto> getQTopList();

    public List<KtoQListDto> getKTopList();

    public void QtoQSave(QtoQSaveDto qtoQSaveDto);

    public void KtoQSave(KtoQSaveDto ktoQSaveDto);
}

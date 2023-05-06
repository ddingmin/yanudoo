package com.example.forhackerton.repository;

import com.example.forhackerton.data.KtoQListDto;
import com.example.forhackerton.data.QtoQListDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Dao {

    public List<QtoQListDto> getQTopList();

    public List<KtoQListDto> getKTopList();
}

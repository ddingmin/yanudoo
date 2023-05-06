package com.example.forhackerton.controller;

import com.example.forhackerton.common.BaseResponse;
import com.example.forhackerton.config.RegularResponseStatus;
import com.example.forhackerton.data.KtoQListDto;
import com.example.forhackerton.data.QtoQListDto;
import com.example.forhackerton.service.SqlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/getList")
public class DaoController {

    Logger logger = LoggerFactory.getLogger(DaoController.class);
    private SqlService sqlService;

    @Autowired
    public DaoController(SqlService sqlService) {
        this.sqlService = sqlService;
    }

    @GetMapping("/QtoQList")
    public BaseResponse<List<QtoQListDto>> getQList(){
        try{
            List<QtoQListDto> QList = sqlService.getQtoQList();
            return new BaseResponse<>(RegularResponseStatus.OK.getCode(), QList,  RegularResponseStatus.OK.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            logger.info("error 발생");
            return new BaseResponse<>(RegularResponseStatus.SERVICE_UNAVAILABLE.getCode(), null, RegularResponseStatus.SERVICE_UNAVAILABLE.getMessage());
        }
    }

    @GetMapping("/KtoQList")
    public BaseResponse<List<KtoQListDto>> getKList(){
        try{
            List<KtoQListDto> KList = sqlService.getKtoQList();
            return new BaseResponse<>(RegularResponseStatus.OK.getCode(), KList, RegularResponseStatus.OK.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            logger.info("error 발생");
            return new BaseResponse<>(RegularResponseStatus.SERVICE_UNAVAILABLE.getCode(), null, RegularResponseStatus.SERVICE_UNAVAILABLE.getMessage());
        }
    }
}

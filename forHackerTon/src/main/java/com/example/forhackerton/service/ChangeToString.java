package com.example.forhackerton.service;

import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import com.google.gson.JsonObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ChangeToString {

    /*
    Clova return 객체 -> 필요한 Text 데이터만 string 으로 Parsing Method
     */
    public String clovaToString(String k) throws JSONException {
        JSONObject jsonObject = new JSONObject(k);
        JSONArray images = jsonObject.getJSONArray("images");
        JSONObject firstImage = images.getJSONObject(0);
        JSONArray fields = firstImage.getJSONArray("fields");
        StringBuilder t = new StringBuilder();
        for (int i = 0; i < fields.length(); i++) {
            JSONObject field = fields.getJSONObject(i);
            String inferText = field.getString("inferText");
            t.append(inferText);
        }
        return t.toString();
    }

    public String makeBlank(String k){
        String koreanRegex = "[가-힣]+";
        Pattern pattern = Pattern.compile(koreanRegex);
        Matcher matcher = pattern.matcher(k);

        StringBuffer sb = new StringBuffer();
        while(matcher.find()){
            matcher.appendReplacement(sb, matcher.group() + " ");
        }
        matcher.appendTail(sb);

        return sb.toString();
    }
}

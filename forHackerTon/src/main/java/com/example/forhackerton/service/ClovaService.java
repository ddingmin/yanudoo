package com.example.forhackerton.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JsonParseException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;


@Service
@Component
public class ClovaService {

    @Value("${clova.apiURL}")
    private String apiUrL;

    @Value("${clova.secretKey}")
    private String secretKey;

    public String getClovaResponse(MultipartFile file) throws IOException, JSONException {
        try{
            byte[] fileBytes = file.getBytes();
            URL url = new URL(apiUrL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setUseCaches(false);
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            con.setRequestProperty("X-OCR-SECRET", secretKey);

            JSONObject json = new JSONObject();
            json.put("version", "V2");
            json.put("requestId", UUID.randomUUID().toString());
            json.put("timestamp", System.currentTimeMillis());
            JSONObject image = new JSONObject();
            image.put("format", getFileExtension(file.getOriginalFilename()));
            String base64Encoded = Base64.getEncoder().encodeToString(fileBytes);
            image.put("data", base64Encoded);
            image.put("name", "demo");
            JSONArray images = new JSONArray();
            images.put(image);
            json.put("images", images);
            String postParams = json.toString();
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            StringBuilder k = new StringBuilder();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode fields = objectMapper.readTree(response.toString()).get("images").get(0).get("fields");
            for(JsonNode field : fields){
                k.append(field.get("inferText").asText());

            }
            System.out.println(k);
            br.close();
            return response.toString();
        }catch (Exception e){
            e.printStackTrace();
            String answer = "";
            return answer;
        }
    }

    private static String getFileExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf('.');
        return lastIndex == -1 ? "" : fileName.substring(lastIndex + 1);
    }
}

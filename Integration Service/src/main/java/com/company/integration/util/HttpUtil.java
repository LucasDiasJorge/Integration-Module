package com.company.integration.util;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.core.util.Json;

public class HttpUtil {

    public static Object sendGetRequest(String url) throws JsonProcessingException{
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        return jsonToMap(response.getBody());
    }

    public static Object sendGetRequest(String url, Map<String,String> header) throws JsonProcessingException{
        MultiValueMap<String,String> multivalueHeader = new LinkedMultiValueMap<>();
        multivalueHeader.setAll(header);
        HttpHeaders headers = new HttpHeaders(multivalueHeader);
        HttpEntity<Object> entity = new HttpEntity<Object>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return jsonToMap(response.getBody());
    }

    public static <T> Object sendPostRequest(String url, T requestBody){
        return null;
    }

    public static <T> Map<String,Object> entityToMap(T entity){
        try {
            return new ObjectMapper().convertValue(entity, new TypeReference<Map<String,Object>>() {});
        } catch (Exception e) {
            System.out.println(e);
            throw e;
        }
    }

    /**
    * Converts a json string to a Map
    * @param String json
    * @return A Map or the original String if an error occurs
    **/
    public static Object jsonToMap(String json) throws JsonProcessingException{
        try {
            return Json.mapper().readValue(json, new TypeReference<Map<String,Object>>() {});
        } catch (JsonProcessingException e) {
            return json;
        }
    }
   
}

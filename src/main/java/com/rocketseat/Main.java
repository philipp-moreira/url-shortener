package com.rocketseat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main implements RequestHandler<Map<String, Object>, Map<String, String>> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Map<String, String> handleRequest(Map<String, Object> input, Context context) {

        String bodyRequest = input.get("body").toString();
        Map<String, String> bodyMap = convertBody(bodyRequest);

        String originalUrl = bodyMap.get("originalUrl");
        String timeExpiration = bodyMap.get("timeExpiration");

        String codeUrlShorteded = UUID.randomUUID().toString().substring(0, 8);
        Map<String, String> response = new HashMap<>();

        response.put("code", codeUrlShorteded);

        return response;

    }

    private Map<String, String> convertBody(String valueToMapping) {
        try {
            return objectMapper.readValue(valueToMapping, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("Error to parsing body JSON " + e.getMessage(),
                    e);
        }
    }

}
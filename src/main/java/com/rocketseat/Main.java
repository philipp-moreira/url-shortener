package com.rocketseat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

public class Main implements RequestHandler<Map<String, Object>, Map<String, String>> {

    private static String BUCKET_NAME = "url-shortener-bucket-philipp2";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final S3Client s3Client = S3Client.builder().build();

    @Override
    public Map<String, String> handleRequest(Map<String, Object> input, Context context) {

        String bodyRequest = input.get("body").toString();
        Map<String, String> bodyMap = convertBody(bodyRequest);

        String originalUrl = bodyMap.get("originalUrl");
        String timeExpiration = bodyMap.get("timeExpiration");
        long timeExpirationParsed = Long.parseLong(timeExpiration);

        String codeUrlShorteded = UUID.randomUUID().toString().substring(0, 8);
        Map<String, String> response = new HashMap<>();
        UrlData urlData = new UrlData(originalUrl, timeExpirationParsed);
        String keyBucket = codeUrlShorteded + ".json";
        String urlDataJson;

        try {
            urlDataJson = objectMapper.writeValueAsString(urlData);
        } catch (Exception e) {
            throw new RuntimeException("Error on convert values to object to persist in S3 " + e.getMessage(), e);
        }

        try {
            PutObjectRequest request = PutObjectRequest
                    .builder()
                    .bucket(BUCKET_NAME)
                    .key(keyBucket)
                    .build();

            s3Client.putObject(request, RequestBody.fromString(urlDataJson));
        } catch (Exception e) {
            throw new RuntimeException("Error on persist in S3 " + e.getMessage(), e);
        }

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
package com.vizen.util;

import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.extern.log4j.Log4j2;

import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Log4j2
public class SecretsRetriever {

    public static Map<String, String> getSecret(String awsRegion, String secretName) {
        log.info("Fetching secrets for " + secretName);
        Map<String, String> secretsMap = Collections.emptyMap();
        try {
            // Create a Secrets Manager client
            AWSSecretsManager client = AWSSecretsManagerClientBuilder.standard()
                    .withRegion(awsRegion)
                    .build();

            GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest()
                    .withSecretId(secretName);
            GetSecretValueResult getSecretValueResult =
                    getSecretValueResult = client.getSecretValue(getSecretValueRequest);

            String secret = null, decodedBinarySecret = null;
            // Decrypts secret using the associated KMS key.
            // Depending on whether the secret is a string or binary, one of these fields will be populated.
            if (getSecretValueResult.getSecretString() != null) {
                secret = getSecretValueResult.getSecretString();
            } else {
                decodedBinarySecret = new String(Base64.getDecoder().decode(getSecretValueResult.getSecretBinary()).array());
            }
            String secretToUse = secret != null ? secret : decodedBinarySecret;
            secretsMap = (new ObjectMapper()).
                    readValue(secretToUse, TypeFactory.defaultInstance().constructMapType(HashMap.class, String.class, String.class));

        } catch (Exception e) {
            log.error(" Error fetching secret: " + secretName + " due to " + e.getMessage());
        }
        return secretsMap;
    }
}

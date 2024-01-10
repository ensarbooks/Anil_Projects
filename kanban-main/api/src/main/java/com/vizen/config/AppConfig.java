package com.vizen.config;

import com.vizen.util.SecretsRetriever;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.Map;

@Configuration
@Log4j2
public class AppConfig {

    @Value("${spring.ds.username:a}")
    private String dbUser;

    @Value("${spring.ds.password:a}")
    private String dbPassword;

    @Value("${spring.ds.url:a}")
    private String dbUrl;

    @Value("${AWS_REGION:us-west-2}")
    private String awsRegion;

    @Value("${AWS_DB_SECRET_NAME:a}")
    private String awsDbSecretsName;

    @Value("${app.jwt.secret.key:a}")
    private String jwtSecretKey;

    @Value("${EMAIL_USER_NAME:a}")
    private String emailUserName;

    @Value("${EMAIL_USER_PWD:a}")
    private String emailUserPwd;

    @Value("${AWS_ACCESS_KEY:a}")
    private String quickSightAccessKey;

    @Value("${AWS_SECRET_KEY:a}")
    private String quickSightSecretKey;

    @Value("${AWS_VIZEN_SECRET_NAME:a}")
    private String awsVizenSecretsName;


    private Map<String, String> secretsMap = Collections.emptyMap();

    @Bean
    public DataSource dataSource() {
        Map<String, String > dbSecrets = SecretsRetriever.getSecret(awsRegion,
                awsDbSecretsName);
        if(!dbSecrets.isEmpty()) {
            log.info("****** Using DB Secrets from AWS .... ");
            dbUser = dbSecrets.get("username");
            dbPassword = dbSecrets.get("password");
        }
        log.info("#### DbUser "  + dbUser);
        log.info("#### dbUrl "  + dbUrl);
        return DataSourceBuilder.create().driverClassName("com.mysql.jdbc.Driver")
                .url(dbUrl)
                .username(dbUser)
                .password(dbPassword).build();
    }

    @Bean
    public VizenSecrets vizenSecrets() {
        VizenSecrets vizenSecrets = new VizenSecrets();
        secretsMap = SecretsRetriever.getSecret(awsRegion,
                awsVizenSecretsName);

        vizenSecrets.setJwtSecretKey(getSecret(jwtSecretKey, "jwtSecretKey"));
        vizenSecrets.setEmailUserName(getSecret(emailUserName, "emailUserName"));
        vizenSecrets.setEmailUserPwd(getSecret(emailUserPwd, "emailUserPwd"));
        vizenSecrets.setQuickSightAccessKey(getSecret(quickSightAccessKey, "quickSightAccessKey"));
        vizenSecrets.setQuickSightSecretKey(getSecret(quickSightSecretKey, "quickSightSecretKey"));

        //log.info("#### " + vizenSecrets);
        return vizenSecrets;
    }

    private String getSecret(String val, String key) {
        return secretsMap.getOrDefault(key, val);
    }
}

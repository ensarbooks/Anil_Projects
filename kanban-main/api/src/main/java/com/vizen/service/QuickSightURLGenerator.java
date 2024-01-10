package com.vizen.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.quicksight.AmazonQuickSight;
import com.amazonaws.services.quicksight.AmazonQuickSightClientBuilder;
import com.amazonaws.services.quicksight.model.GenerateEmbedUrlForRegisteredUserRequest;
import com.amazonaws.services.quicksight.model.GenerateEmbedUrlForRegisteredUserResult;
import com.amazonaws.services.quicksight.model.RegisteredUserDashboardEmbeddingConfiguration;
import com.amazonaws.services.quicksight.model.RegisteredUserEmbeddingExperienceConfiguration;
import com.vizen.config.VizenSecrets;

import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;



//https://docs.aws.amazon.com/quicksight/latest/user/embedded-dashboards-for-authenticated-users-step-1.html

@Component
@Log4j2
public class QuickSightURLGenerator {

    @Autowired
    VizenSecrets vizenSecrets;

    @Value("${AWS_ACCOUNT_ID:}")
    private String account_id;

    @Value("${AWS_USER_ARN:}")
    private String user_arn;

    private AmazonQuickSight quickSightClient;

    DemoQuickSightURLGenerator demoQuickSightURLGenerator;

    public QuickSightURLGenerator(DemoQuickSightURLGenerator demoQuickSightURLGenerator) {
        this.demoQuickSightURLGenerator = demoQuickSightURLGenerator;
    }

    @PostConstruct
    public void init() {
        quickSightClient = AmazonQuickSightClientBuilder
                .standard()
                .withRegion(Regions.US_WEST_2.getName())
                .withCredentials(new AWSCredentialsProvider() {
                                     @Override
                                     public AWSCredentials getCredentials() {
                                         // provide actual IAM access key and secret key here
                                         return new BasicAWSCredentials(vizenSecrets.getQuickSightAccessKey(), vizenSecrets.getQuickSightSecretKey());
                                     }

                                     @Override
                                     public void refresh() {
                                     }
                                 }
                )
                .build();
    }

    public String getQuicksightEmbedUrl(final String dashboardId) {
        if(demoQuickSightURLGenerator.isDemoDashBoard(dashboardId)) {
            log.info("Generating quicksight url for demo dashboard " + dashboardId);
            return demoQuickSightURLGenerator.getQuicksightEmbedUrl(dashboardId);
        }

        final RegisteredUserEmbeddingExperienceConfiguration experienceConfiguration = new RegisteredUserEmbeddingExperienceConfiguration()
                .withDashboard(new RegisteredUserDashboardEmbeddingConfiguration().withInitialDashboardId(dashboardId));
        final GenerateEmbedUrlForRegisteredUserRequest generateEmbedUrlForRegisteredUserRequest = new GenerateEmbedUrlForRegisteredUserRequest();
        generateEmbedUrlForRegisteredUserRequest.setAwsAccountId(account_id);
        generateEmbedUrlForRegisteredUserRequest.setUserArn(user_arn);
        generateEmbedUrlForRegisteredUserRequest.setSessionLifetimeInMinutes(60L);
        generateEmbedUrlForRegisteredUserRequest.setExperienceConfiguration(experienceConfiguration);

        final GenerateEmbedUrlForRegisteredUserResult generateEmbedUrlForRegisteredUserResult = quickSightClient.generateEmbedUrlForRegisteredUser(generateEmbedUrlForRegisteredUserRequest);

        return generateEmbedUrlForRegisteredUserResult.getEmbedUrl();
    }
}


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

import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

//https://docs.aws.amazon.com/quicksight/latest/user/embedded-dashboards-for-authenticated-users-step-1.html

@Log4j2
@Component
public class DemoQuickSightURLGenerator {

    @Value("${DEMO_AWS_ACCESS_KEY:}")
    private String access_key;

    @Value("${DEMO_AWS_SECRET_KEY:}")
    private String secret_key;

    @Value("${DEMO_AWS_ACCOUNT_ID:}")
    private String account_id;

    @Value("${DEMO_AWS_USER_ARN:}")
    private String user_arn;

    @Value("${DEMO_DASHBOARD_LIST:}")
    private String demo_dash_board_list_str;

    private List<String> demo_dash_board_list;

    private AmazonQuickSight quickSightClient;

    @PostConstruct
    public void init() {
        quickSightClient = AmazonQuickSightClientBuilder
                .standard()
                .withRegion(Regions.US_WEST_2.getName())
                .withCredentials(new AWSCredentialsProvider() {
                                     @Override
                                     public AWSCredentials getCredentials() {
                                         // provide actual IAM access key and secret key here
                                         return new BasicAWSCredentials(access_key, secret_key);
                                     }

                                     @Override
                                     public void refresh() {
                                     }
                                 }
                )
                .build();
        if(StringUtils.hasText(demo_dash_board_list_str))
        {
            demo_dash_board_list = Arrays.stream(demo_dash_board_list_str.split(",")).map(a->a.trim()).collect(Collectors.toList());
        }
        log.info("Demo dashboard list : " + demo_dash_board_list);
    }

    public boolean isDemoDashBoard(String dashBoardId) {
        return demo_dash_board_list.contains(dashBoardId);
    }
    public String getQuicksightEmbedUrl(final String dashboardId) {
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


package com.vizen.controller;

import com.vizen.entity.User;
import com.vizen.entity.UserPasswordResetRequest;
import com.vizen.request.dto.ForgotPasswordDto;
import com.vizen.request.dto.ResetPasswordDto;
import com.vizen.response.dto.SuccessResponse;
import com.vizen.service.EmailSender;
import com.vizen.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.vizen.util.Constants.*;


@Api(tags = "Password Reset")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/v1/auth")
@Log4j2
public class ResetPasswordController {

    private final UserService userService;

    private final ApplicationEventPublisher eventPublisher;

    public ResetPasswordController(
            UserService userService,
            ApplicationEventPublisher eventPublisher,
            EmailSender emailSender
    ) {
        this.userService = userService;
        this.eventPublisher = eventPublisher;
    }

    @ApiOperation(value = SWG_RESPWD_FORGOT_OPERATION, response = SuccessResponse.class)
    @PostMapping(value = "/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@Valid @RequestBody ForgotPasswordDto forgotPasswordDto)
            throws ResourceNotFoundException {
        User user = userService.getUserByEmail(forgotPasswordDto.getEmail());
        Map<String, String> result = new HashMap<>();

        if (user == null) {
            result.put(MESSAGE_KEY, NO_USER_FOUND_WITH_EMAIL_MESSAGE);
            return ResponseEntity.badRequest().body(result);
        }

        UserPasswordResetRequest userPasswordResetRequest = userService.createPasswordResetRequest(user.getEmail(), false);
        result.put(MESSAGE_KEY, PASSWORD_LINK_SENT_MESSAGE);
        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@Valid @RequestBody ResetPasswordDto passwordResetDto)
            throws ResourceNotFoundException {

        UserPasswordResetRequest userPasswordResetRequest = userService.getPasswordResetRequest(passwordResetDto.getToken());
        Map<String, String> result = new HashMap<>();

        if (userPasswordResetRequest.getExpireDateTime().toLocalDateTime().isBefore(LocalDateTime.now())) {
            result.put(MESSAGE_KEY, TOKEN_EXPIRED_MESSAGE);
            return ResponseEntity.badRequest().body(result);
        }

        User user = userService.updatePassword(passwordResetDto.getToken(), passwordResetDto.getPassword());

        result.put(MESSAGE_KEY, RESET_PASSWORD_SUCCESS_MESSAGE);

        return ResponseEntity.ok(result);
    }
}

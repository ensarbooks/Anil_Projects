package com.vizen.util;

public class Constants {
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_SUPER_ADMIN = "ROLE_SUPER_ADMIN";
    public static final String JWT_ILLEGAL_ARGUMENT_MESSAGE = "An error occured during getting username from token";
    public static final String JWT_EXPIRED_MESSAGE = "The token is expired and not valid anymore";
    public static final String JWT_SIGNATURE_MESSAGE = "Authentication Failed. Username or Password not valid.";
    public static final String MESSAGE_KEY = "message";
    public static final String DATA_KEY = "message";
    public static final String TOKEN_EXPIRED_MESSAGE = "You token has been expired!";
    public static final String ACCOUNT_DEACTIVATED_MESSAGE = "Your account has been deactivated!";
    public static final String NO_USER_FOUND_WITH_EMAIL_MESSAGE = "No user found with this email!";
    public static final String PASSWORD_LINK_SENT_MESSAGE = "A password reset link has been sent to your email box!";
    public static final String RESET_PASSWORD_SUCCESS_MESSAGE = "Your password has been resetted successfully!";
    public static final String SWG_RESPWD_FORGOT_OPERATION = "Request a link to reset the password";
    static final String AUTHORITIES_KEY = "scopes";

    public static final String UUID_PATTERN = "^[0-9A-Fa-f]{8}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{12}$";
}

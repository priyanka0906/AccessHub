package com.priyanka.accesshub.constant;

import org.springframework.beans.factory.annotation.Value;

public interface UserConstants {
    String ERROR_MSG = "Username already exist";
    String SUCCESS_MSG = "User registered successfully";
    String INVALID_CREDENTIALS = "Invalid Response";
    String INVALID_USERNAME = "Invalid username";
    String INVALID_PASSWORD = "Invalid Password";
    String EXPIRED = "Refresh token expired";


    String BEARER = "Bearer";

}

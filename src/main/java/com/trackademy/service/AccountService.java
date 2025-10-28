package com.trackademy.service;

import com.trackademy.dto.LoginStatusDto;

public interface AccountService {
    LoginStatusDto getLoginStatus(String subject, String email);
}


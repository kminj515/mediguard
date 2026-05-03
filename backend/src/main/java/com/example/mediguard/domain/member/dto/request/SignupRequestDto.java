package com.example.mediguard.domain.member.dto.request;


import lombok.Getter;
import lombok.Setter;

//
@Getter
@Setter
public class SignupRequestDto {

    private String email;
    private String password;
    private String nickname;
    private String confirmPassword;

}

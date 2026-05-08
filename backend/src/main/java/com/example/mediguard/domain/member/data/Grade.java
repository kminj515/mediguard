package com.example.mediguard.domain.member.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Grade {
    주의_필요("주의 필요"),
    복약_보통("복약 보통"),
    복약_우수("복약 우수"),
    복약_달인("복약 달인");

    private final String description;
}
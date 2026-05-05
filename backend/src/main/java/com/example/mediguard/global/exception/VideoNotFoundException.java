package com.example.mediguard.global.exception;

import com.example.mediguard.domain.video.enums.VideoErrorCode;

public class VideoNotFoundException extends CustomException {
    public VideoNotFoundException() {
        super(VideoErrorCode.VIDEO_NOT_FOUND);
    }
}
package com.example.mediguard.domain.video.exception;

import com.example.mediguard.domain.video.enums.VideoErrorCode;
import com.example.mediguard.global.exception.CustomException;

public class VideoException extends CustomException {

    public VideoException(VideoErrorCode errorCode) {
        super(errorCode);
    }

}

package com.example.mediguard.domain.chatbot.dto.req;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public record GeminiRequest(
        List<Content> contents,
        GenerationConfig generationConfig,
        List<SafetySetting> safetySettings
) {
    public record Content(List<Part> parts) {}

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Part(String text, InlineData inlineData) {
        public static Part ofText(String text) {
            return new Part(text, null);
        }
        public static Part ofImage(String mimeType, String data) {
            return new Part(null, new InlineData(mimeType, data));
        }
    }

    public record InlineData(String mimeType, String data) {}
    public record GenerationConfig(double temperature, int maxOutputTokens) {}
    public record SafetySetting(String category, String threshold) {}

    private static List<SafetySetting> defaultSafetySettings() {
        return List.of("HARM_CATEGORY_HARASSMENT", "HARM_CATEGORY_HATE_SPEECH",
                        "HARM_CATEGORY_SEXUALLY_EXPLICIT", "HARM_CATEGORY_DANGEROUS_CONTENT")
                .stream().map(c -> new SafetySetting(c, "BLOCK_NONE")).toList();
    }

    // 텍스트 전용 요청
    public static GeminiRequest of(String prompt) {
        return new GeminiRequest(
                List.of(new Content(List.of(Part.ofText(prompt)))),
                new GenerationConfig(0.7, 8192),
                defaultSafetySettings()
        );
    }

    // 이미지 + 텍스트 요청 (Vision)
    public static GeminiRequest ofImage(String base64Image, String mimeType, String prompt) {
        return new GeminiRequest(
                List.of(new Content(List.of(
                        Part.ofImage(mimeType, base64Image),
                        Part.ofText(prompt)
                ))),
                new GenerationConfig(0.2, 1024),
                defaultSafetySettings()
        );
    }
}
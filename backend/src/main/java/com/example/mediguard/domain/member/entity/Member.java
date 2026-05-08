package com.example.mediguard.domain.member.entity;

import com.example.mediguard.domain.member.data.Grade;
import com.example.mediguard.domain.member.enums.MemberErrorCode;
import com.example.mediguard.domain.member.exception.MemberException;
import com.example.mediguard.global.config.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor

public class Member extends BaseEntity {

@Id
@GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long memberId;


    @Column(unique = true)
    private String email; // 이메일

    @Column(nullable = false)
    private String password; // 비밀번호

    @Column(nullable = false, length = 50)
    private String nickname; // 닉네임

    @Column(name = "points")
    private int points; // 포인트

    @Enumerated(EnumType.STRING)
    @Column(name = "grade")
    private Grade grade;

    @Column(length = 20)
    private String status; // 회원상태

    private int exp; // 경험치

    @Column(name = "font_size")
    private int fontSize; // 폰트크기


    @Column(length = 20)
    private String provider; // 소셜 로그인 제공자

    @Column(name = "provider_id")
    private String providerId; // 소셜ID

    @Column(length = 20)
    private String role; // 사용자 권한

    @Column(length = 512)
    private String profileImageUrl;



    @Builder
    public Member(String email, String password, String nickname, String role, String provider, String providerId) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.grade = Grade.주의_필요;
        this.status = "ACTIVE"; // 예: 활성 상태를 기본값으로 지정
        this.fontSize = 16; // 예: 기본 폰트 크기
        this.role = "ROLE_USER";
        this.provider = provider;
        this.providerId = providerId;
        this.points = 100000;
    }



    public void addPoints(int points) {
        this.points += points;
    }

    public void checkBalance(int amount) {
        if (this.points < amount) {
            throw new MemberException(MemberErrorCode.INSUFFICIENT_BALANCE);
        }
    }


    //소셜 회원가입 메소드
    public static Member createSocialMember(String email, String nickname, String provider, String providerId) {
        Member member = new Member();
        member.email = email;
        member.nickname = nickname;

        member.provider = provider;
        member.providerId = providerId;

        // 소셜 로그인 사용자는 비밀번호를 사용하지 않으므로, 보안을 위해 임의의 값을 할당
        member.password = UUID.randomUUID().toString();
        member.role = "ROLE_USER"; // 기본 권한 부여
        member.points = 0;
        member.grade = Grade.주의_필요;
        member.status = "ACTIVE"; // 예: 활성 상태를 기본값으로 지정
        member.exp = 0;
        member.fontSize = 16;

        return member;
    }

    // 경험치를 더할 때마다 등급을 자동으로 판별합니다.
    public void addExp(int expToAdd) {
        this.exp += expToAdd;
        this.points += expToAdd;
        updateGradeByExp(); // << 추가된 자동 등급 갱신 로직
    }

    private void updateGradeByExp() {
        if (this.exp >= 500) {
            this.grade = Grade.복약_달인;
        } else if (this.exp >= 300) {
            this.grade = Grade.복약_우수;
        } else if (this.exp >= 100) {
            this.grade = Grade.복약_보통;
        } else {
            this.grade = Grade.주의_필요;
        }
    }

    public Member updateSocialProfile(String nickname) {
        this.nickname = nickname;
        return this;
    }
    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }


    public void updatePassword(String newPasswordHash) {
        this.password = newPasswordHash;
    }


    // 프로필 일괄 수정
    public void updateProfile(String nickname, String passwordHash, Grade grade) {
        if (nickname != null && !nickname.trim().isEmpty()) {
            this.nickname = nickname;
        }
        if (passwordHash != null && !passwordHash.trim().isEmpty()) {
            this.password = passwordHash;
        }
    }

    // 프로필 이미지만 업데이트
    public void updateProfileImage(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void setGrade(String finalGrade) {
        this.grade = Grade.valueOf(finalGrade.replace(" ", "_"));
    }



}

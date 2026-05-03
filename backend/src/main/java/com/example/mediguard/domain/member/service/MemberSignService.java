package com.example.mediguard.domain.member.service;


import com.example.mediguard.domain.member.converter.MemberConverter;
import com.example.mediguard.domain.member.dto.request.SignupRequestDto;
import com.example.mediguard.domain.member.dto.response.SignupResponseDto;
import com.example.mediguard.domain.member.entity.Member;
import com.example.mediguard.domain.member.enums.MemberErrorCode;
import com.example.mediguard.domain.member.exception.MemberException;
import com.example.mediguard.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberSignService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberConverter memberConverter;

    @Transactional
    public SignupResponseDto signUp(SignupRequestDto requestDto) {

        validateDuplicateMember(requestDto);

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        Member member = memberConverter.toEntity(requestDto, encodedPassword);
        Member savedMember = memberRepository.save(member);

        return SignupResponseDto.builder()
                .memberId(savedMember.getMemberId())
                .email(savedMember.getEmail())
                .nickname(savedMember.getNickname())
                .build();
    }




    private void validateDuplicateMember(SignupRequestDto requestDto) {

        if (memberRepository.existsByEmail(requestDto.getEmail())) {
            throw new MemberException(MemberErrorCode.EMAIL_ALREADY_EXISTS);
        }

//        if (memberRepository.existsByNickname(requestDto.getNickname())) {
//            throw new MemberException(MemberErrorCode.NICKNAME_ALREADY_EXISTS);
//        }

    }
}
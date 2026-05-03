package com.example.mediguard.domain.quiz.service;

import com.example.mediguard.domain.member.entity.Member;
import com.example.mediguard.domain.member.enums.MemberErrorCode;
import com.example.mediguard.domain.member.exception.MemberException;
import com.example.mediguard.domain.member.repository.MemberRepository;
import com.example.mediguard.domain.quiz.dto.req.QuizSubmitRequest;
import com.example.mediguard.domain.quiz.dto.res.*;
import com.example.mediguard.domain.quiz.entity.*;
import com.example.mediguard.domain.quiz.enums.QuizErrorCode;
import com.example.mediguard.domain.quiz.exception.QuizException;
import com.example.mediguard.domain.quiz.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class QuizService {
    private final QuizRepository quizRepository;
    private final QuizOptionRepository quizOptionRepository;
    private final MemberRepository memberRepository;
    private final QuizHistoryService historyService;

    public QuizSubmitResponse submitAnswer(Long memberId, Long quizId, QuizSubmitRequest request) {
        Quiz quiz = findQuiz(quizId);
        QuizOption selected = findOption(request.selectedOptionId());
        Member member = findMember(memberId);

        boolean isCorrect = selected.getIsCorrect();
        int gainExp = isCorrect ? quiz.getPoint() : 0;

        boolean isFirstTimeCorrect = isCorrect && !historyService.isAlreadySolved(memberId, quizId);

        if (isFirstTimeCorrect) {
            member.addExp(gainExp);
            memberRepository.save(member);
            historyService.updateProgress(member, quiz);
        }

        historyService.saveQuizHistory(member, quiz, gainExp);

        return new QuizSubmitResponse(
                isCorrect, gainExp,
                isCorrect ? "정답입니다!" : "오답입니다."
        );
    }

    private Quiz findQuiz(Long id) {
        return quizRepository.findById(id).orElseThrow(() -> new QuizException(QuizErrorCode.QUIZ_NOT_FOUND));
    }

    private QuizOption findOption(Long id) {
        return quizOptionRepository.findById(id).orElseThrow(() -> new QuizException(QuizErrorCode.OPTION_NOT_FOUND));
    }

    private Member findMember(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
    }
}
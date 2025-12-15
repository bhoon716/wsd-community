package wsd.community.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import wsd.community.common.response.CustomException;
import wsd.community.common.error.ErrorCode;
import wsd.community.domain.user.entity.User;
import wsd.community.domain.user.repository.UserRepository;
import wsd.community.domain.user.response.UserResponse;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse getUser(Long userId) {
        log.info("회원 정보 조회 요청: userId={}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        return UserResponse.from(user);
    }

    @Transactional
    public void updatePassword(Long userId, String newPassword) {
        log.info("비밀번호 변경 요청: userId={}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        if (StringUtils.hasText(newPassword)) {
            String encodedPassword = passwordEncoder.encode(newPassword);
            user.updatePassword(encodedPassword);
        }
        log.info("비밀번호 변경 완료: userId={}", userId);
    }

    @Transactional
    public void withdraw(Long userId) {
        log.info("회원 탈퇴 요청: userId={}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        userRepository.delete(user);
        log.info("회원 탈퇴 완료: userId={}", userId);
    }
}

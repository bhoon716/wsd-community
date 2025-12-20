package wsd.community.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wsd.community.common.error.CustomException;
import wsd.community.common.error.ErrorCode;
import wsd.community.domain.user.entity.User;
import wsd.community.domain.user.entity.UserRole;
import wsd.community.domain.user.repository.UserRepository;
import wsd.community.domain.user.response.UserResponse;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public UserResponse getUser(Long userId) {
        log.info("회원 정보 조회 요청: userId={}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        return UserResponse.from(user);
    }

    @Transactional
    public void withdraw(Long userId) {
        log.info("회원 탈퇴 요청: userId={}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        userRepository.delete(user);
        log.info("회원 탈퇴 완료: userId={}", userId);
    }

    @Transactional
    public void changeRole(Long requesterId, Long targetUserId, UserRole newRole) {
        log.info("회원 역할 변경 요청: requesterId={}, targetUserId={}, newRole={}", requesterId, targetUserId, newRole);

        // 요청자 및 OWNER 권한 검증
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        validateRequester(requester);

        // 역할 변경 파라미터 유효성 검증
        validateRequest(requesterId, targetUserId, newRole);

        // 대상 사용자 및 변경 제약 조건 검증
        User target = userRepository.findById(targetUserId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        validateTarget(target, newRole);

        target.updateRole(newRole);
        log.info("회원 역할 변경 완료: targetUserId={}, newRole={}", targetUserId, newRole);
    }

    private void validateRequester(User requester) {
        // 요청자 권한 검증
        if (requester.getRole() != UserRole.OWNER) {
            throw new CustomException(ErrorCode.NOT_OWNER);
        }
    }

    private void validateRequest(Long requesterId, Long targetUserId, UserRole newRole) {
        // 요청자와 대상자 일치 검증
        if (requesterId.equals(targetUserId)) {
            throw new CustomException(ErrorCode.CANNOT_CHANGE_SELF_ROLE);
        }
        // OWNER 권한 변경 방지
        if (newRole == UserRole.OWNER) {
            throw new CustomException(ErrorCode.CANNOT_SET_OWNER_ROLE);
        }
    }

    private void validateTarget(User target, UserRole newRole) {
        // OWNER 권한 변경 방지
        if (target.getRole() == UserRole.OWNER) {
            throw new CustomException(ErrorCode.CANNOT_MODIFY_OWNER);
        }

        // 관리자가 1명뿐인 경우 강등 방지
        if (target.getRole() == UserRole.ADMIN && newRole != UserRole.ADMIN) {
            long adminCount = userRepository.countByRole(UserRole.ADMIN);
            if (adminCount <= 1) {
                throw new CustomException(ErrorCode.CANNOT_DEMOTE_LAST_ADMIN);
            }
        }
    }
}

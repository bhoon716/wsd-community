package wsd.community.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wsd.community.common.error.CustomException;
import wsd.community.common.error.ErrorCode;
import wsd.community.domain.user.entity.User;
import wsd.community.domain.user.entity.UserRole;
import wsd.community.domain.user.repository.UserRepository;
import wsd.community.domain.user.response.UserResponse;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Nested
    @DisplayName("역할 변경")
    class ChangeRole {

        @Test
        @DisplayName("성공: OWNER가 USER를 ADMIN으로 승격")
        void success_userToAdmin() {
            // given
            Long ownerId = 1L;
            Long targetId = 2L;
            User owner = createUser(ownerId, UserRole.OWNER);
            User target = createUser(targetId, UserRole.USER);

            given(userRepository.findById(ownerId)).willReturn(Optional.of(owner));
            given(userRepository.findById(targetId)).willReturn(Optional.of(target));
            // No count check needed for promotion to ADMIN

            // when
            userService.changeRole(ownerId, targetId, UserRole.ADMIN);

            // then
            assertThat(target.getRole()).isEqualTo(UserRole.ADMIN);
        }

        @Test
        @DisplayName("성공: OWNER가 ADMIN을 USER로 강등")
        void success_adminToUser() {
            // given
            Long ownerId = 1L;
            Long targetId = 2L;
            User owner = createUser(ownerId, UserRole.OWNER);
            User target = createUser(targetId, UserRole.ADMIN);

            given(userRepository.findById(ownerId)).willReturn(Optional.of(owner));
            given(userRepository.findById(targetId)).willReturn(Optional.of(target));
            given(userRepository.countByRole(UserRole.ADMIN)).willReturn(2L); // Not last admin

            // when
            userService.changeRole(ownerId, targetId, UserRole.USER);

            // then
            assertThat(target.getRole()).isEqualTo(UserRole.USER);
        }

        @Test
        @DisplayName("실패: ADMIN이 역할 변경 시도")
        void fail_requesterNotOwner() {
            // given
            Long adminId = 1L;
            Long targetId = 2L;
            User admin = createUser(adminId, UserRole.ADMIN);

            given(userRepository.findById(adminId)).willReturn(Optional.of(admin));

            // when & then
            assertThatThrownBy(() -> userService.changeRole(adminId, targetId, UserRole.USER))
                    .isInstanceOf(CustomException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.NOT_OWNER);
        }

        @Test
        @DisplayName("실패: OWNER 역할로 변경 시도")
        void fail_setOwnerRole() {
            // given
            Long ownerId = 1L;
            Long targetId = 2L;
            User owner = createUser(ownerId, UserRole.OWNER);

            given(userRepository.findById(ownerId)).willReturn(Optional.of(owner));

            // when & then
            assertThatThrownBy(() -> userService.changeRole(ownerId, targetId, UserRole.OWNER))
                    .isInstanceOf(CustomException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.CANNOT_SET_OWNER_ROLE);
        }

        @Test
        @DisplayName("실패: 본인 역할 변경 시도")
        void fail_selfChange() {
            // given
            Long ownerId = 1L;
            User owner = createUser(ownerId, UserRole.OWNER);

            given(userRepository.findById(ownerId)).willReturn(Optional.of(owner));

            // when & then
            assertThatThrownBy(() -> userService.changeRole(ownerId, ownerId, UserRole.ADMIN))
                    .isInstanceOf(CustomException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.CANNOT_CHANGE_SELF_ROLE);
        }

        @Test
        @DisplayName("실패: 마지막 ADMIN 강등 시도")
        void fail_lastAdminDemotion() {
            // given
            Long ownerId = 1L;
            Long targetId = 2L;
            User owner = createUser(ownerId, UserRole.OWNER);
            User target = createUser(targetId, UserRole.ADMIN);

            given(userRepository.findById(ownerId)).willReturn(Optional.of(owner));
            given(userRepository.findById(targetId)).willReturn(Optional.of(target));
            given(userRepository.countByRole(UserRole.ADMIN)).willReturn(1L); // Last admin

            // when & then
            assertThatThrownBy(() -> userService.changeRole(ownerId, targetId, UserRole.USER))
                    .isInstanceOf(CustomException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.CANNOT_DEMOTE_LAST_ADMIN);
        }

        @Test
        @DisplayName("실패: 타 OWNER 역할 변경 시도")
        void fail_modifyOwner() {
            // given
            Long ownerId = 1L;
            Long targetId = 2L;
            User owner = createUser(ownerId, UserRole.OWNER);
            User targetOwner = createUser(targetId, UserRole.OWNER);

            given(userRepository.findById(ownerId)).willReturn(Optional.of(owner));
            given(userRepository.findById(targetId)).willReturn(Optional.of(targetOwner));

            // when & then
            assertThatThrownBy(() -> userService.changeRole(ownerId, targetId, UserRole.USER))
                    .isInstanceOf(CustomException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.CANNOT_MODIFY_OWNER);
        }

        @Test
        @DisplayName("실패: 요청자를 찾을 수 없음")
        void fail_requesterNotFound() {
            // given
            Long ownerId = 1L;
            Long targetId = 2L;

            given(userRepository.findById(ownerId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> userService.changeRole(ownerId, targetId, UserRole.USER))
                    .isInstanceOf(CustomException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.NOT_FOUND_USER);
        }

        @Test
        @DisplayName("실패: 대상 사용자를 찾을 수 없음")
        void fail_targetNotFound() {
            // given
            Long ownerId = 1L;
            Long targetId = 2L;
            User owner = createUser(ownerId, UserRole.OWNER);

            given(userRepository.findById(ownerId)).willReturn(Optional.of(owner));
            given(userRepository.findById(targetId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> userService.changeRole(ownerId, targetId, UserRole.USER))
                    .isInstanceOf(CustomException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.NOT_FOUND_USER);
        }
    }

    @Nested
    @DisplayName("회원 정보 조회")
    class GetUser {

        @Test
        @DisplayName("성공")
        void success() {
            // given
            Long userId = 1L;
            User user = createUser(userId, UserRole.USER);

            given(userRepository.findById(userId)).willReturn(Optional.of(user));

            // when
            UserResponse response = userService.getUser(userId);

            // then
            assertThat(response.getEmail()).isEqualTo(user.getEmail());
            assertThat(response.getName()).isEqualTo(user.getName());
        }

        @Test
        @DisplayName("실패: 사용자 없음")
        void fail_userNotFound() {
            // given
            Long userId = 1L;

            given(userRepository.findById(userId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> userService.getUser(userId))
                    .isInstanceOf(CustomException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.NOT_FOUND_USER);
        }
    }

    @Nested
    @DisplayName("회원 탈퇴")
    class Withdraw {

        @Test
        @DisplayName("성공")
        void success() {
            // given
            Long userId = 1L;
            User user = createUser(userId, UserRole.USER);

            given(userRepository.findById(userId)).willReturn(Optional.of(user));

            // when
            userService.withdraw(userId);

            // then
            verify(userRepository).delete(user);
        }

        @Test
        @DisplayName("실패: 사용자 없음")
        void fail_userNotFound() {
            // given
            Long userId = 1L;

            given(userRepository.findById(userId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> userService.withdraw(userId))
                    .isInstanceOf(CustomException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.NOT_FOUND_USER);
        }
    }

    private User createUser(Long id, UserRole role) {
        User user = User.builder()
                .id(id)
                .name("test")
                .email("test@test.com")
                .role(role)
                .build();
        return user;
    }
}

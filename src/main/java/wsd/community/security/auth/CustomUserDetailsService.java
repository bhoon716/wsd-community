package wsd.community.security.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import wsd.community.common.error.CustomException;
import wsd.community.common.error.ErrorCode;
import wsd.community.domain.user.entity.User;
import wsd.community.domain.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("사용자 인증 정보 로드 요청: email={}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        return new CustomUserDetails(user);
    }
}

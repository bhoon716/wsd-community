package wsd.community.security.oauth;

import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wsd.community.domain.user.entity.User;
import wsd.community.domain.user.entity.UserRole;
import wsd.community.domain.user.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = getDelegateUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        String providerId = oAuth2User.getName();

        log.info("OAuth2 사용자 로드 요청: provider={}, email={}", registrationId, email);
        User user = saveOrUpdate(email, name, registrationId, providerId);
        log.info("OAuth2 사용자 로드 완료: userId={}", user.getId());

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().getKey())),
                attributes,
                userNameAttributeName);
    }

    private User saveOrUpdate(String email, String name, String provider, String providerId) {
        User user = userRepository.findByEmail(email)
                .map(entity -> entity)
                .orElse(User.builder()
                        .email(email)
                        .name(name)
                        .role(UserRole.USER)
                        .provider(provider)
                        .providerId(providerId)
                        .build());

        User savedUser = userRepository.save(user);
        log.info("사용자 저장/업데이트: userId={}, email={}", savedUser.getId(), email);
        return savedUser;
    }

    protected OAuth2User getDelegateUser(OAuth2UserRequest userRequest) {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        return delegate.loadUser(userRequest);
    }
}

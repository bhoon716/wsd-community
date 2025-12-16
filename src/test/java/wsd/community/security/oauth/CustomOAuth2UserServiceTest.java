package wsd.community.security.oauth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import wsd.community.domain.user.entity.User;
import wsd.community.domain.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class CustomOAuth2UserServiceTest {

    @InjectMocks
    private CustomOAuth2UserService customOAuth2UserService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OAuth2UserRequest userRequest;

    @Mock
    private OAuth2User oAuth2User;

    @Mock
    private OAuth2AccessToken accessToken;

    @Test
    @DisplayName("Google OAuth2 로그인 성공")
    void loadUser_createsNewUser() {
        // Given
        String registrationId = "google";
        String userNameAttributeName = "sub";
        Map<String, Object> attributes = Map.of(
                "sub", "1234567890",
                "name", "Test User",
                "email", "test@example.com");

        ClientRegistration clientRegistration = ClientRegistration.withRegistrationId(registrationId)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .clientId("clientId")
                .redirectUri("redirectUri")
                .authorizationUri("authorizationUri")
                .tokenUri("tokenUri")
                .userInfoUri("userInfoUri")
                .userNameAttributeName(userNameAttributeName)
                .build();

        CustomOAuth2UserService spyService = spy(customOAuth2UserService);
        doReturn(oAuth2User).when(spyService).getDelegateUser(userRequest);

        given(userRequest.getClientRegistration()).willReturn(clientRegistration);
        given(oAuth2User.getAttributes()).willReturn(attributes);
        given(oAuth2User.getName()).willReturn("1234567890");

        given(userRepository.findByEmail("test@example.com")).willReturn(Optional.empty());
        given(userRepository.save(any(User.class))).willAnswer(invocation -> {
            User user = invocation.getArgument(0);
            return User.builder()
                    .id(1L)
                    .email(user.getEmail())
                    .name(user.getName())
                    .role(user.getRole())
                    .provider(user.getProvider())
                    .providerId(user.getProviderId())
                    .build();
        });

        // When
        OAuth2User result = spyService.loadUser(userRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getAttributes()).containsAllEntriesOf(attributes);
    }
}

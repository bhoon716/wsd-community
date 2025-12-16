package wsd.community.security.oauth;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class OAuth2FailureHandlerTest {

    @InjectMocks
    private OAuth2FailureHandler oAuth2FailureHandler;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private AuthenticationException exception;

    @Test
    @DisplayName("OAuth2 로그인 실패 시 설정된 URL로 리다이렉트한다")
    void onAuthenticationFailure_redirectsToFailureUrl() throws IOException, Exception {
        // Given
        String failureUri = "http://localhost:3000/login?error=true";
        ReflectionTestUtils.setField(oAuth2FailureHandler, "redirectUri", failureUri);

        given(response.encodeRedirectURL(anyString())).willAnswer(invocation -> invocation.getArgument(0));

        // When
        oAuth2FailureHandler.onAuthenticationFailure(request, response, exception);

        // Then
        verify(response).sendRedirect(failureUri);
    }
}

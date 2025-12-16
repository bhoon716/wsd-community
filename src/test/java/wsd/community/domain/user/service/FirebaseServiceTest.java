package wsd.community.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wsd.community.common.response.CustomException;
import wsd.community.common.error.ErrorCode;

@ExtendWith(MockitoExtension.class)
class FirebaseServiceTest {

    @Mock
    private FirebaseAuth firebaseAuth;

    @InjectMocks
    private FirebaseService firebaseService;

    @Test
    @DisplayName("토큰 검증 성공")
    void verifyToken_Success() throws FirebaseAuthException {
        // given
        String idToken = "valid-token";
        FirebaseToken firebaseToken = mock(FirebaseToken.class);
        when(firebaseAuth.verifyIdToken(idToken)).thenReturn(firebaseToken);

        // when
        FirebaseToken result = firebaseService.verifyToken(idToken);

        // then
        assertThat(result).isEqualTo(firebaseToken);
    }

    @Test
    @DisplayName("토큰 검증 실패 - FirebaseAuthException 발생")
    void verifyToken_Failure() throws FirebaseAuthException {
        // given
        String idToken = "invalid-token";
        when(firebaseAuth.verifyIdToken(anyString())).thenThrow(mock(FirebaseAuthException.class));

        // when & then
        assertThatThrownBy(() -> firebaseService.verifyToken(idToken))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_TOKEN);
    }
}

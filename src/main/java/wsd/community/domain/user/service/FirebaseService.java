package wsd.community.domain.user.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import wsd.community.common.error.ErrorCode;
import wsd.community.common.response.CustomException;

@Slf4j
@Service
@RequiredArgsConstructor
public class FirebaseService {

    private final FirebaseAuth firebaseAuth;

    public FirebaseToken verifyToken(String idToken) {
        try {
            return firebaseAuth.verifyIdToken(idToken);
        } catch (FirebaseAuthException e) {
            log.error("Firebase 토큰 검증 실패: {}", e.getMessage());
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
    }
}

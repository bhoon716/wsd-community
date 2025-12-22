package wsd.community.common.firebase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;

import wsd.community.common.firebase.TranslationService;

import com.google.cloud.translate.Translation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class TranslationServiceTest {

    @InjectMocks
    private TranslationService translationService;

    @Mock
    private GoogleCredentials credentials;

    private Translate translate;

    @BeforeEach
    void setUp() {
        translate = mock(Translate.class);
        ReflectionTestUtils.setField(translationService, "translate", translate);
    }

    @Test
    @DisplayName("성공: 텍스트 번역")
    void translate_Success() {
        // given
        String originalText = "안녕하세요";
        String translatedText = "Hello";
        String targetLang = "en";

        Translation translation = mock(Translation.class);
        given(translation.getTranslatedText()).willReturn(translatedText);
        given(translate.translate(any(String.class), any(TranslateOption.class), any(TranslateOption.class)))
                .willReturn(translation);

        // when
        String result = translationService.translate(originalText, targetLang);

        // then
        assertThat(result).isEqualTo(translatedText);
    }

    @Test
    @DisplayName("성공: 텍스트가 비어있으면 원문 반환")
    void translate_Success_EmptyText() {
        // given
        String text = "";
        String targetLang = "en";

        // when
        String result = translationService.translate(text, targetLang);

        // then
        assertThat(result).isEqualTo("");
    }

    @Test
    @DisplayName("성공: 타겟 언어가 비어있으면 원문 반환")
    void translate_Success_EmptyLang() {
        // given
        String text = "Hello";
        String targetLang = "";

        // when
        String result = translationService.translate(text, targetLang);

        // then
        assertThat(result).isEqualTo(text);
    }

    @Test
    @DisplayName("성공: API 호출 실패 시 Fail-open (원문 반환)")
    void translate_Success_ApiFailure() {
        // given
        String text = "Original Text";
        String targetLang = "en";

        given(translate.translate(any(String.class), any(TranslateOption.class), any(TranslateOption.class)))
                .willThrow(new RuntimeException("API Error"));

        // when
        String result = translationService.translate(text, targetLang);

        // then
        assertThat(result).isEqualTo(text);
    }
}

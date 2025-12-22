package wsd.community.common.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TranslationService {

    private final GoogleCredentials credentials;
    private Translate translate;

    public String translate(String text, String targetLang) {
        if (text == null || text.isBlank() || targetLang == null || targetLang.isBlank()) {
            return text;
        }

        try {
            if (translate == null) {
                translate = TranslateOptions.newBuilder()
                        .setCredentials(credentials)
                        .build()
                        .getService();
            }

            Translation translation = translate.translate(
                    text,
                    Translate.TranslateOption.targetLanguage(targetLang),
                    Translate.TranslateOption.format("text"));

            return translation.getTranslatedText();

        } catch (Exception e) {
            log.error("Google Translation API 호출 실패: {}", e.getMessage());
            return text;
        }
    }
}

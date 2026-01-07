package org.example;

import com.microsoft.cognitiveservices.speech.*;
import com.microsoft.cognitiveservices.speech.audio.AudioConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;

@Service
public class AzureSpeechToTextService {

    private static final String KEY = System.getenv("AZURE_SPEECH_KEY");
    private static final String REGION = System.getenv("AZURE_SPEECH_REGION");

    public String transcribe(MultipartFile audioFile) throws Exception {

        if (KEY == null || REGION == null) {
            throw new RuntimeException("Azure Speech KEY or REGION missing");
        }

        File tempFile = File.createTempFile("azure-", ".wav");
        audioFile.transferTo(tempFile);

        SpeechConfig config = SpeechConfig.fromSubscription(KEY, REGION);
        config.setSpeechRecognitionLanguage("en-US");

        AudioConfig audioConfig = AudioConfig.fromWavFileInput(
                tempFile.getAbsolutePath()
        );

        SpeechRecognizer recognizer = new SpeechRecognizer(config, audioConfig);
        SpeechRecognitionResult result =
                recognizer.recognizeOnceAsync().get();

        recognizer.close();
        tempFile.delete();

        if (result.getReason() != ResultReason.RecognizedSpeech) {
            throw new RuntimeException("Azure STT failed: " + result.getReason());
        }

        return result.getText();
    }
}

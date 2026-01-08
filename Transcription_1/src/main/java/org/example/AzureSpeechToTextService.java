package org.example;

import com.microsoft.cognitiveservices.speech.*;
import com.microsoft.cognitiveservices.speech.audio.AudioConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.concurrent.CountDownLatch;

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

        AudioConfig audioConfig =
                AudioConfig.fromWavFileInput(tempFile.getAbsolutePath());

        SpeechRecognizer recognizer =
                new SpeechRecognizer(config, audioConfig);

        StringBuilder fullTranscript = new StringBuilder();
        CountDownLatch latch = new CountDownLatch(1);

        recognizer.recognized.addEventListener((s, e) -> {
            if (e.getResult().getReason() == ResultReason.RecognizedSpeech) {
                fullTranscript.append(e.getResult().getText()).append(" ");
            }
        });

        recognizer.canceled.addEventListener((s, e) -> {
            System.err.println("CANCELED: " + e.getReason());
            latch.countDown();
        });

        recognizer.sessionStopped.addEventListener((s, e) -> {
            System.out.println("Session stopped.");
            latch.countDown();
        });

        // 🚀 Start full audio processing
        recognizer.startContinuousRecognitionAsync().get();

        // ⏳ Wait until Azure finishes reading the WAV file
        latch.await();

        recognizer.stopContinuousRecognitionAsync().get();
        recognizer.close();
        tempFile.delete();

        return fullTranscript.toString().trim();
    }
}

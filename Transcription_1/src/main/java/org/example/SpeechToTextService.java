package org.example;

import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class SpeechToTextService {

    public String transcribe(MultipartFile audioFile) throws Exception {

        try (SpeechClient speechClient = SpeechClient.create()) {

            // Read audio bytes
            ByteString audioBytes = ByteString.readFrom(audioFile.getInputStream());

            // Configure audio
            RecognitionConfig config = RecognitionConfig.newBuilder()
                    .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                    .setLanguageCode("en-US")
                    .build();

            RecognitionAudio audio = RecognitionAudio.newBuilder()
                    .setContent(audioBytes)
                    .build();


            // Call Google STT
            RecognizeResponse response = speechClient.recognize(config, audio);

            // Extract transcription
            StringBuilder transcription = new StringBuilder();
            for (SpeechRecognitionResult result : response.getResultsList()) {
                transcription.append(
                        result.getAlternativesList().get(0).getTranscript()
                );
            }

            return transcription.toString();
        }
    }
}

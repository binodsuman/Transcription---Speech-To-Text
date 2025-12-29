package org.example;

import org.example.SpeechToTextService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class AudioTranscriptionController {

    private final SpeechToTextService speechToTextService;

    public AudioTranscriptionController(SpeechToTextService speechToTextService) {
        this.speechToTextService = speechToTextService;
    }

    @PostMapping("/transcribe")
    public ResponseEntity<String> transcribeAudio(
            @RequestParam("file") MultipartFile audioFile) {

        if (audioFile.isEmpty()) {
            return ResponseEntity.badRequest().body("Audio file is empty");
        }

        try {
            String transcription = speechToTextService.transcribe(audioFile);
            return ResponseEntity.ok("\n"+transcription+"\n");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Failed to transcribe audio: " + e.getMessage());
        }
    }
}

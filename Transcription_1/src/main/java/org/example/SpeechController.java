package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/stt")
public class SpeechController {

    @Autowired
    private AzureSpeechToTextService sttService;

    @PostMapping("/azure")
    public ResponseEntity<String> transcribe(
            @RequestParam("file") MultipartFile file) throws Exception {

        String text = sttService.transcribe(file);
        return ResponseEntity.ok(text);
    }
}

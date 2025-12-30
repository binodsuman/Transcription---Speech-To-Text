# Transcription---Speech-To-Text

To Test the code:

export GOOGLE_APPLICATION_CREDENTIALS="/Users/binod/Downloads/google-stt-key.json" <br>
gcloud auth application-default print-access-token

<br><br>
mvn clean install<br>
mvn spring-boot:run<br>
curl -X POST http://localhost:8080/api/transcribe   -F "file=@Binod_Audio_Test.wav"
<br>

<br>
For Audio file transfer<br>
brew install ffmpeg  # if not installed<br>
ffmpeg -i input.m4a -ar 22050 -ac 1 -c:a pcm_s16le output.wav<br>

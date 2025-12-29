# Transcription---Speech-To-Text

To Test the code:

export GOOGLE_APPLICATION_CREDENTIALS="/Users/binod/Downloads/google-stt-key.json"
gcloud auth application-default print-access-token

mvn clean install
curl -X POST http://localhost:8080/api/transcribe   -F "file=@Binod_Audio_Test.wav"


For Audio file transfer
brew install ffmpeg  # if not installed
ffmpeg -i input.m4a -ar 22050 -ac 1 -c:a pcm_s16le output.wav

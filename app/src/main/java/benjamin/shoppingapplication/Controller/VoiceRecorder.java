package benjamin.shoppingapplication.Controller;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.MediaRecorder;
import android.speech.RecognizerIntent;

import java.util.Locale;

/**
 * Created by Benjamin on 12/5/2016.
 */

public class VoiceRecorder {

    private MediaRecorder mediaRecorder;

    public VoiceRecorder() {

    }

    public void speechRecognizerIntent() {
        Intent speech = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speech.putExtra(RecognizerIntent.ACTION_RECOGNIZE_SPEECH,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speech.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
    }
}

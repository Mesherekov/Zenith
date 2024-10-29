package com.example.zenith;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {
    private FirebaseAuth mfirebaseAuth;
    private EditText edemail, edpassword;
    private Button bsignin;
    private SoundPool mSoundPool;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        init();
        bsignin.setOnClickListener(view -> {
            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            float curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            float maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            float leftVolume = curVolume / maxVolume;
            float rightVolume = curVolume / maxVolume;
            int priority = 1;
            int no_loop = 0;
            float normal_playback_rate = 1f;
            int mStreamId = mSoundPool.play(1, leftVolume, rightVolume, priority, no_loop, normal_playback_rate);
            if(!TextUtils.isEmpty(edemail.getText().toString()) && !TextUtils.isEmpty(edpassword.getText().toString())) {
                mfirebaseAuth.signInWithEmailAndPassword(edemail.getText().toString(), edpassword.getText().toString()).addOnCompleteListener(SignInActivity.this, task -> {
                    if(task.isSuccessful()){
                        FirebaseUser user = mfirebaseAuth.getCurrentUser();
                        assert user != null;
                        if(user.isEmailVerified()){
                            Intent intent = new Intent(SignInActivity.this, ChartListActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else Toast.makeText(this, "Please confirm your email", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(SignInActivity.this, "Sign In failed", Toast.LENGTH_SHORT).show();
                });
            } else Toast.makeText(SignInActivity.this, "Please fill in the fields", Toast.LENGTH_SHORT).show();
        });
    }
    private void init(){
        edemail = findViewById(R.id.emailin);
        edpassword = findViewById(R.id.passin);
        bsignin = findViewById(R.id.enterac);
        mSoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
        mSoundPool.load(this, R.raw.mouse, 1);
        mfirebaseAuth = FirebaseAuth.getInstance();
    }
}
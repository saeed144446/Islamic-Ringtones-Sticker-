package com.example.islamicringtones.Activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.example.islamicringtones.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RingtonesPlay extends AppCompatActivity {


    private static final int WRITE_SETTINGS_PERMISSION_CODE = 100;

    private ImageView backbtn, setting, volumeButton, previous, play, next;
    private CircleImageView img;
    private TextView songTitle, durationSong;
    private MediaPlayer mediaPlayer;
    private ArrayList<Integer> ringtoneUris;
    private ArrayList<String> ringtoneTitles;
    private ArrayList<Integer> ringtoneImages;
    private int currentSoundIndex = 0;
    private AudioManager audioManager;
    private LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ringtones_play);



        // Find views by ID
        backbtn = findViewById(R.id.backbtn);
        setting = findViewById(R.id.setting);
        img = findViewById(R.id.img);
        songTitle = findViewById(R.id.songtitle);
        durationSong = findViewById(R.id.duraton_song);
        volumeButton = findViewById(R.id.volume_btn);
        previous = findViewById(R.id.previus);
        play = findViewById(R.id.play);
        next = findViewById(R.id.next);
        lottieAnimationView=findViewById(R.id.lottie_layer_name);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        Intent intent = getIntent();
        ringtoneUris = intent.getIntegerArrayListExtra("ringtoneUris");
        ringtoneTitles = intent.getStringArrayListExtra("ringtoneTitles");
        ringtoneImages = intent.getIntegerArrayListExtra("ringtoneImages");
        currentSoundIndex = intent.getIntExtra("currentIndex", 0);

        updateImageAndTitle(currentSoundIndex);
        initializeMediaPlayer(currentSoundIndex);

        // Adjust volume button handling
        volumeButton.setOnClickListener(v -> adjustVolume(true));

        backbtn.setOnClickListener(v -> onBackPressed());

        // Handle settings button click
        setting.setOnClickListener(v -> {
            requestPermissions();
            showPopup();
        });

        play.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                play.setImageResource(R.drawable.ic_playbtn);
                lottieAnimationView.pauseAnimation();

            } else {
                mediaPlayer.start();
                updateDuration();
                play.setImageResource(R.drawable.ic_pausebtn);
                lottieAnimationView.playAnimation();
            }
        });

        next.setOnClickListener(v -> {
            if (currentSoundIndex < ringtoneUris.size() - 1) {
                currentSoundIndex++;
                changeRingtone(currentSoundIndex);
                lottieAnimationView.playAnimation();

            } else {
                Toast.makeText(RingtonesPlay.this, "No more next sounds", Toast.LENGTH_SHORT).show();
            }
        });

        previous.setOnClickListener(v -> {
            if (currentSoundIndex > 0) {
                currentSoundIndex--;
                changeRingtone(currentSoundIndex);
                lottieAnimationView.playAnimation();
            } else {
                Toast.makeText(RingtonesPlay.this, "No previous sounds", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initializeMediaPlayer(int index) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        mediaPlayer = MediaPlayer.create(this, ringtoneUris.get(index));
        play.setImageResource(R.drawable.ic_pausebtn);

        int totalDuration = mediaPlayer.getDuration();
        durationSong.setText(formatDuration(totalDuration));

        mediaPlayer.start();
        updateDuration();

        mediaPlayer.setOnCompletionListener(mp -> {
            if (currentSoundIndex < ringtoneUris.size() - 1) {
                currentSoundIndex++;
                changeRingtone(currentSoundIndex);
            } else {
                Toast.makeText(RingtonesPlay.this, "No more sounds", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateImageAndTitle(int index) {
        if (ringtoneImages != null && ringtoneTitles != null) {
            img.setImageResource(ringtoneImages.get(index));
            songTitle.setText(ringtoneTitles.get(index));
        }
    }

    private String formatDuration(int durationInMillis) {
        int minutes = (durationInMillis / 1000) / 60;
        int seconds = (durationInMillis / 1000) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private void updateDuration() {
        final android.os.Handler handler = new android.os.Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    durationSong.setText(formatDuration(currentPosition));
                    handler.postDelayed(this, 1000);
                }
            }
        }, 1000);
    }

    private void changeRingtone(int index) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        initializeMediaPlayer(index);
        updateImageAndTitle(index);
    }

    private void showPopup() {
        PopupMenu popupMenu = new PopupMenu(this, setting);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            int id = menuItem.getItemId();
            if (id == R.id.ringtone) {
                setRingtone();
            } else if (id == R.id.alarm) {
                setAlarm();
            }else if (id == R.id.notification) {
                setNotificationSound();
            }
            return true;
        });
        popupMenu.show();
    }

    private void setAlarm() {
        String filename = ringtoneTitles.get(currentSoundIndex);

        // Create ContentValues for MediaStore
        ContentValues values = new ContentValues();
        values.put(MediaStore.Audio.Media.TITLE, filename);
        values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/mp3");
        values.put(MediaStore.Audio.Media.IS_ALARM, true);
        values.put(MediaStore.Audio.Media.DISPLAY_NAME, filename + ".mp3");
        values.put(MediaStore.Audio.Media.RELATIVE_PATH, Environment.DIRECTORY_ALARMS + "/ringtone/");

        // Insert into MediaStore
        Uri alarmUri = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        Uri newUri = getContentResolver().insert(alarmUri, values);

        if (newUri != null) {
            try (OutputStream os = getContentResolver().openOutputStream(newUri);
                 InputStream is = getResources().openRawResource(ringtoneUris.get(currentSoundIndex))) {

                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }

            } catch (IOException e) {
                Log.e("RingtonePlayActivity", "Error writing file: " + e.getMessage());
                Toast.makeText(this, "Error writing file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }

            // Set alarm
            RingtoneManager.setActualDefaultRingtoneUri(this, RingtoneManager.TYPE_ALARM, newUri);
            Toast.makeText(this, "Alarm set successfully", Toast.LENGTH_SHORT).show();
            Log.d("RingtonePlayActivity", "Alarm set successfully with URI: " + newUri.toString());

        } else {
            Log.e("RingtonePlayActivity", "Failed to insert into MediaStore");
            Toast.makeText(this, "Failed to set alarm", Toast.LENGTH_SHORT).show();
        }
    }

    private void setNotificationSound() {
        String filename = ringtoneTitles.get(currentSoundIndex);

        // Create ContentValues for MediaStore
        ContentValues values = new ContentValues();
        values.put(MediaStore.Audio.Media.TITLE, filename);
        values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/mp3");
        values.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
        values.put(MediaStore.Audio.Media.DISPLAY_NAME, filename + ".mp3");
        values.put(MediaStore.Audio.Media.RELATIVE_PATH, Environment.DIRECTORY_NOTIFICATIONS + "/");

        // Insert into MediaStore
        Uri notificationUri = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        Uri newUri = getContentResolver().insert(notificationUri, values);

        if (newUri != null) {
            try (OutputStream os = getContentResolver().openOutputStream(newUri);
                 InputStream is = getResources().openRawResource(ringtoneUris.get(currentSoundIndex))) {

                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }

            } catch (IOException e) {
                Log.e("RingtonePlayActivity", "Error writing file: " + e.getMessage());
                Toast.makeText(this, "Error writing file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }

            // Set notification sound
            RingtoneManager.setActualDefaultRingtoneUri(this, RingtoneManager.TYPE_NOTIFICATION, newUri);
            Toast.makeText(this, "Notification sound set successfully", Toast.LENGTH_SHORT).show();
            Log.d("RingtonePlayActivity", "Notification sound set successfully with URI: " + newUri.toString());

        } else {
            Log.e("RingtonePlayActivity", "Failed to insert into MediaStore");
            Toast.makeText(this, "Failed to set notification sound", Toast.LENGTH_SHORT).show();
        }
    }

    private void setRingtone() {
        String filename = ringtoneTitles.get(currentSoundIndex);

        // Create ContentValues for MediaStore
        ContentValues values = new ContentValues();
        values.put(MediaStore.Audio.Media.TITLE, filename);
        values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/mp3");
        values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
        values.put(MediaStore.Audio.Media.DISPLAY_NAME, filename + ".mp3");
        values.put(MediaStore.Audio.Media.RELATIVE_PATH, Environment.DIRECTORY_RINGTONES + "/");

        // Insert into MediaStore
        Uri ringtoneUri = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        Uri newUri = getContentResolver().insert(ringtoneUri, values);

        if (newUri != null) {
            try (OutputStream os = getContentResolver().openOutputStream(newUri);
                 InputStream is = getResources().openRawResource(ringtoneUris.get(currentSoundIndex))) {

                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }

            } catch (IOException e) {
                Log.e("RingtonePlayActivity", "Error writing file: " + e.getMessage());
                Toast.makeText(this, "Error writing file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }

            // Set ringtone
            RingtoneManager.setActualDefaultRingtoneUri(this, RingtoneManager.TYPE_RINGTONE, newUri);
            Toast.makeText(this, "Ringtone set successfully", Toast.LENGTH_SHORT).show();
            Log.d("RingtonePlayActivity", "Ringtone set successfully with URI: " + newUri.toString());

        } else {
            Log.e("RingtonePlayActivity", "Failed to insert into MediaStore");
            Toast.makeText(this, "Failed to set ringtone", Toast.LENGTH_SHORT).show();
        }
    }






    private void adjustVolume(boolean increase) {
        if (increase) {
            audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
        } else {
            audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
        }
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, WRITE_SETTINGS_PERMISSION_CODE);
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }
}
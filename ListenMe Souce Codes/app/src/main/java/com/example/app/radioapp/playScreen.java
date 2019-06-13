package com.example.app.radioapp;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.MacAddress;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class playScreen extends AppCompatActivity {

    Button playButton;
    Button recordButton;
    Button playRecordButton;
    com.like.LikeButton likeButton;
    TextView tv,currentChannel;
    MediaPlayer mediaPlayer;
    MediaPlayer recordPlayer;
    FileOutputStream outputStream;

    Recorder recorder;


    boolean prepared = false;
    boolean started = false;
    boolean replay = false;
    boolean liked=false;
    Radio stream;

    int i=0;

    @SuppressLint("ObsoleteSdkInt")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_screen);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //  PLAY BUTTON
        playButton = findViewById(R.id.playButton);
        playButton.setEnabled(false);
        playButton.setText(getString(R.string.loading));

        //LIKE BUTTON
        likeButton=findViewById(R.id.heart_button);



        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(likeButton.isLiked()){
                    likeButton.setLiked(false);
                    MainScreen.radioArrayList.remove(stream.getId());
                    stream.setFaved(false);
                    MainScreen.radioArrayList.add(stream.getId(),stream);


                }else{
                    likeButton.setLiked(true);
                    MainScreen.radioArrayList.remove(stream.getId());
                    stream.setFaved(true);
                    MainScreen.radioArrayList.add(stream.getId(),stream);


                }
            }
        });



        //  RECORD BUTTON
        recordButton = findViewById(R.id.recordButton);
        recordButton.setEnabled(true);
        recordButton.setText(getString(R.string.recordOff));

        //  PLAY RECORD BUTTON
        playRecordButton = findViewById(R.id.playRecordButton);
        playRecordButton.setEnabled(false);
        //playRecordButton.setText(getString(R.string.playRecord));

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        recordPlayer = new MediaPlayer();

        Intent fromMain = getIntent();
        Bundle b = fromMain.getExtras();

        if(b != null){
            stream = (Radio) fromMain.getSerializableExtra("channel_url");
        }
        likeButton.setLiked(stream.isFaved());
        new PlayerTask().execute(stream.getUrl());

        //textView pf Current Channel
        currentChannel= findViewById(R.id.current);
        currentChannel.setText(stream.getChannel());
        //  PLAY BUTTON LISTENER
        playButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view){
                if(started){
                    started = false;
                    mediaPlayer.pause();
                    playButton.setText(getString(R.string.play));
                }else{
                    started = true;
                    mediaPlayer.start();
                    playButton.setText(getString(R.string.pause));
                }

            }
        });

        //  RECORD BUTTON LISTENER
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recordButton.getText().toString().equals("RECORD OFF")){

                    try {
                        outputStream = new FileOutputStream(
                                new File(getFilesDir(), "My_Record.mp3"), false);
                        //newRecord = getFilesDir(), "My_Record" + i + ".mp3");
                        //i++;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    recordButton.setText(getString(R.string.recordOn));
                    recorder = new Recorder(outputStream, stream.getUrl());
                    new Thread(recorder).start();
                    playRecordButton.setEnabled(false);

                }else{
                    try {
                        recorder.terminate();
                        Thread.currentThread().join(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    recordButton.setText(getString(R.string.recordOff));
                    playRecordButton.setEnabled(true);
                }
            }
        });

        //  PLAY RECORD BUTTON LISTENER
        playRecordButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                //  RECORD STARTS TO PLAYBACK
                if(replay){
                    started = false;
                    mediaPlayer.pause();
                    playButton.setText(getString(R.string.play));
                    replay = false;
                    readFile();
                    playRecordButton.setText(getString(R.string.pauseRecord));
                    recordButton.setEnabled(false);
                }else{
                    recordPlayer.pause();
                    playRecordButton.setText(getString(R.string.playRecord));
                    replay = true;
                    recordButton.setEnabled(true);
                }

            }
        });

    }

    //  PLAY THE SAVED RECORD
    protected void readFile(){

        Uri soundUri = Uri.fromFile(new File(getFilesDir(), "My_Record.mp3"));
        recordPlayer = MediaPlayer.create(this, soundUri);
        recordPlayer.setLooping(true);
        recordPlayer.start();



        /*try {

            File file = new File(getFilesDir(),"My_Record.mp3");

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                Log.d("oku", line + "\n");
            }
            br.close() ;
        }catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    @SuppressLint("StaticFieldLeak")
    class PlayerTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {

            try {
                mediaPlayer.setDataSource(strings[0]);
                mediaPlayer.prepare();
                prepared = true;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            playButton.setEnabled(true);
            playButton.setText(getString(R.string.play));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(started)
            mediaPlayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(started)
            mediaPlayer.start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(prepared)
            mediaPlayer.release();
    }

}

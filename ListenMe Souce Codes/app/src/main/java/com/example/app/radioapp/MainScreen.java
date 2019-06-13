package com.example.app.radioapp;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
//
public class MainScreen extends AppCompatActivity {
    private ListView lv;
    public static ArrayList<Radio> radioArrayList;
    private CustomAdapter customAdapter;
    private Button btnnext;
    private String[] radioList = new String[]{"Max FM", "Joy FM", "Maydanoz FM",
            "Dream FM","Power FM","Metro FM","Pal FM"};
    private String[] radioListURL=new String[]{
            "http://live.radyotvonline.com:8090/",
            "https://playerservices.streamtheworld.com/api/livestream-redirect/JOY_FM.mp3",
            "https://playerservices.streamtheworld.com/api/livestream-redirect/RADIO_MYDONOSE.mp3",
            "http://media.netd.com.tr/S2/HLS_LIVE/radyodream/index.m3u8",
            "http://powerfm.listenpowerapp.com/powerfm/mpeg/icecast.audio",
            "http://stream.radioreklama.bg:80/radio1rock128",
            "http://shoutcast.radyogrup.com:1030/"};
    private TextView txtSpeechInput;
    private ImageButton btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    public static ArrayList<String>  result = new ArrayList<>();
    private View view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        lv =  findViewById(R.id.lv);
        btnnext =  findViewById(R.id.next);
        radioArrayList = getRadioList();
        customAdapter = new CustomAdapter(this);
        customAdapter.notifyDataSetChanged();
        lv.setAdapter(customAdapter);
        txtSpeechInput =  findViewById(R.id.txtSpeechInput);
        btnSpeak =  findViewById(R.id.btnSpeak);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sendMessage(view,(Radio)parent.getItemAtPosition(position));
            }
        });

        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextActivity(v);
            }
        });
        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtSpeechInput.setText(result.get(0));

                    for (Radio radios : radioArrayList){
                        if (radios.getChannel().equals(result.get(0))){

                            sendMessage(view,radios);
                        }
                    }
                }
                break;
            }

        }
    }


    public void sendMessage(View view,Radio radio) {
        Intent intent = new Intent(this, playScreen.class);
        //channel_url = URL.getText().toString();
        intent.putExtra("channel_url", radio);
        startActivity(intent);
    }
    private ArrayList<Radio> getRadioList(){
        ArrayList<Radio> list = new ArrayList<>();
        for(int i=0;i<radioList.length;i++){

            Radio radio = new Radio();
            radio.setChannel(radioList[i]);
            radio.setUrl(radioListURL[i]);
            radio.setId(i);
            list.add(radio);
        }
        return list;
    }
    public void nextActivity(View view){
        Intent intent = new Intent(this, NextActivity.class);
        startActivity(intent);
    }


}

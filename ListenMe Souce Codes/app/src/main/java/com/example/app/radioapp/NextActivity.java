package com.example.app.radioapp;

import android.content.Intent;
import android.net.MacAddress;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class NextActivity extends AppCompatActivity {
    private ListView nextListView;
    private NextCustomAdapter nextCustomAdapter;
    public static ArrayList<Radio> favs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        nextListView= findViewById(R.id.nextList);
        favs=getFavsList();
        nextCustomAdapter = new NextCustomAdapter(this);
        nextListView.setAdapter(nextCustomAdapter);
        if(favs.get(0).isFaved()){
            nextListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    sendMessage(view,(Radio)parent.getItemAtPosition(position));
                }
            });
        }


    }
    public void sendMessage(View view,Radio radio) {
        Intent intent = new Intent(this, playScreen.class);
        //channel_url = URL.getText().toString();
        intent.putExtra("channel_url", radio);
        startActivity(intent);
    }

    private ArrayList<Radio> getFavsList(){
        ArrayList<Radio> list=new ArrayList<>();
        for(Radio rad: MainScreen.radioArrayList){
            if(rad.isFaved()){
                list.add(rad);
            }
        }
        if(list.isEmpty()){
            Radio temp=new Radio();
            temp.setChannel("There is no Favorite Channel");
            temp.setUrl("");
            list.add(temp);
        }
        return list;
    }
}

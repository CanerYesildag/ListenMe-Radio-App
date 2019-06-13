package com.example.app.radioapp;

import android.annotation.SuppressLint;
import android.app.Application;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@SuppressLint("Registered")
public class Recorder extends Application implements Runnable  {

    InputStream inputStream;

    //    long start,end;
    String radioLink;
    FileOutputStream outputStream;

    volatile boolean running = true;

    protected Recorder(FileOutputStream outputStream, String radioLink){

        this.radioLink = radioLink;
        this.outputStream = outputStream;
    }

    protected void terminate(){
        running = false;
    }

    @Override
    public void run() {

        //startRecord(strings[0]);
        int b;
        byte[] buffer = new byte[1024];

        try {
            URL url = new URL(radioLink);
            inputStream = url.openStream();
            //Log.d("TAG", "Radio Link: " + radioLink);


            while (((b = inputStream.read(buffer)) > 0) && running == true){
                outputStream.write(buffer, 0, b);
                Log.d("thread", "run: " + b);
            }


            outputStream.flush();
            outputStream.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
        Calling url.openStream() initiates a new TCP connection to the server that the URL resolves to.
        An HTTP GET request is then sent over the connection. If all goes right (i.e., 200 OK),
        the server sends back the HTTP response message that carries the data payload
        that is served up at the specified URL. You then need to read the bytes
        from the InputStream that the openStream() method returns in order to retrieve
        the data payload into your program.

        Note: The request does not go through your browser. It is executed by a Java class
        that acts as an HTTP client running in your JVM.
        */

    }   //  end of run method
}   //  end of class

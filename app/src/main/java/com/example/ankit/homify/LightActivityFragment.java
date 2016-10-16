package com.example.ankit.homify;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

/**
 * A placeholder fragment containing a simple view.
 */
public class LightActivityFragment extends Fragment {

    private final String lightOne = "1";
    private final String lightTwo = "2";
    private final String normalOne = "3";
    private final String romanticOne = "4";
    private final String partyOne = "5";
    private final String normalTwo = "6";
    private final String romanticTwo = "7";
    private final String partyTwo = "8";
    private final String apocalypse = "9";

    public static Button button1;
    public static Button button2;
    public static Button button9;

    private Socket socket;

    public LightActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_light, container, false);

        // button listener
        button1 = (Button) view.findViewById(R.id.light1);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //System.out.println("Button Clicked");
                new sendMessage().execute(lightOne);
            }
        });

        button2 = (Button) view.findViewById(R.id.light2);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //System.out.println("Button Clicked");
                new sendMessage().execute(lightTwo);
            }
        });

        final Button button3 = (Button) view.findViewById(R.id.normal1);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //System.out.println("Button Clicked");
                new sendMessage().execute(normalOne);
            }
        });

        final Button button4 = (Button) view.findViewById(R.id.romantic1);
        button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //System.out.println("Button Clicked");
                new sendMessage().execute(romanticOne);
            }
        });

        final Button button5 = (Button) view.findViewById(R.id.party1);
        button5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //System.out.println("Button Clicked");
                new sendMessage().execute(partyOne);
            }
        });

        final Button button6 = (Button) view.findViewById(R.id.normal2);
        button6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //System.out.println("Button Clicked");
                new sendMessage().execute(normalTwo);
            }
        });

        final Button button7 = (Button) view.findViewById(R.id.romantic2);
        button7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //System.out.println("Button Clicked");
                new sendMessage().execute(romanticTwo);
            }
        });

        final Button button8 = (Button) view.findViewById(R.id.party2);
        button8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //System.out.println("Button Clicked");
                new sendMessage().execute(partyTwo);
            }
        });

        button9 = (Button) view.findViewById(R.id.terror);
        button9.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new sendMessage().execute(apocalypse);
            }
        });

        try {
            socket = IO.socket("http://192.168.1.129:81");
        } catch (URISyntaxException e) {}

        socket.connect();
        return view;
    }

    public class sendMessage extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            //System.out.println("background");
            String message = params[0];
            //System.out.println("before emit");
            socket.emit("my event", message);
            //System.out.println("after emit");
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        socket.disconnect();
    }
}

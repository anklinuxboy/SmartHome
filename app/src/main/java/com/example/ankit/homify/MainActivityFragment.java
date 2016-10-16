package com.example.ankit.homify;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {


    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        // button listener
        final Button button1 = (Button) view.findViewById(R.id.slight);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), LightActivity.class);
                startActivity(intent);
            }
        });

        final Button button2 = (Button) view.findViewById(R.id.sthermo);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent thermointent = new Intent(getActivity().getApplicationContext(), ThermoActivity.class);
                startActivity(thermointent);
            }
        });
        return view;
    }

}

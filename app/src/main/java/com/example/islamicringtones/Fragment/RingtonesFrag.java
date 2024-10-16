package com.example.islamicringtones.Fragment;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.islamicringtones.Adapters.RingTonesListAdapter;
import com.example.islamicringtones.R;
import com.example.islamicringtones.Utils.RingTonesItem;

import java.util.ArrayList;


public class RingtonesFrag extends Fragment {


    private RingTonesListAdapter listAdapter;
    private RecyclerView recyclerView;
    private ImageView back_btn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_ringtones, container, false);

        recyclerView = view.findViewById(R.id.recyclerview);

        // Set up RecyclerView with a linear layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Set up the adapter with the ringtone list
        listAdapter = new RingTonesListAdapter(getContext(), getRingtoneList());
        recyclerView.setAdapter(listAdapter);
        return view;
    }

    // Method to create and return the list of ringtones
    private ArrayList<RingTonesItem> getRingtoneList() {
        ArrayList<RingTonesItem> ringTones = new ArrayList<>();

        // Add ringtone items from the raw folder
        int[] ringtoneResIds = {R.raw.ring1, R.raw.ring1, R.raw.ring1,R.raw.ring1,R.raw.ring1,R.raw.ring1,
                R.raw.ring1,R.raw.ring1,R.raw.ring1,R.raw.ring1,R.raw.ring1,R.raw.ring1,R.raw.ring1,R.raw.ring1}; // Replace with correct resource IDs
        String[] titles = {"Bear", "Camel", "Deer","Dog", "Donkey", "Elephant","Horse", "Lion", "Monkey",
                "Pandas", "RedWolf", "Sheep","Squirrel", "Zebra"};
        String[] categories = {"Animal RingTones", "Animal RingTones","Animal RingTones","Animal RingTones","Animal RingTones","Animal RingTones","Animal RingTones",
                "Animal RingTones","Animal RingTones","Animal RingTones","Animal RingTones","Animal RingTones","Animal RingTones","Animal RingTones"};
        int[] images = {R.drawable.ic_listiconimg, R.drawable.ic_listiconimg, R.drawable.ic_listiconimg,R.drawable.ic_listiconimg,R.drawable.ic_listiconimg,R.drawable.ic_listiconimg,R.drawable.ic_listiconimg,R.drawable.ic_listiconimg,
                R.drawable.ic_listiconimg,R.drawable.ic_listiconimg,R.drawable.ic_listiconimg,R.drawable.ic_listiconimg,R.drawable.ic_listiconimg,R.drawable.ic_listiconimg}; // Replace with correct image resource IDs

        for (int i = 0; i < ringtoneResIds.length; i++) {
            // Create a MediaPlayer to get the duration of each audio file
            MediaPlayer mediaPlayer = MediaPlayer.create(getContext(), ringtoneResIds[i]);
            int duration = mediaPlayer.getDuration();
            String durationStr = convertDuration(duration);

            // Add each ringtone to the list
            RingTonesItem item = new RingTonesItem(images[i], titles[i], categories[i], durationStr, ringtoneResIds[i]);
            ringTones.add(item);

            // Release the MediaPlayer resource
            if (mediaPlayer != null) {
                mediaPlayer.release();
            }
        }

        return ringTones;
    }

    // Method to convert milliseconds to mm:ss format
    private String convertDuration(int duration) {
        int seconds = duration / 1000;
        int minutes = seconds / 60;
        seconds = seconds % 60;

        return String.format("%02d:%02d", minutes, seconds);
    }
}
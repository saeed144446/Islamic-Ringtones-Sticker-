package com.example.islamicringtones.Adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.islamicringtones.Activities.RingtonesPlay;
import com.example.islamicringtones.R;
import com.example.islamicringtones.Utils.RingTonesItem;

import java.util.ArrayList;

public class RingTonesListAdapter extends RecyclerView.Adapter<RingTonesListAdapter.viewHolder> {

    private Context context;
    private ArrayList<RingTonesItem> ringTonesItems;

    public RingTonesListAdapter(Context context, ArrayList<RingTonesItem> ringTonesItems) {
        this.context = context;
        this.ringTonesItems = ringTonesItems;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.ringtoneslist,parent,false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        RingTonesItem ringTones = ringTonesItems.get(position);

        holder.imageView.setImageResource(ringTones.getImage());
        holder.titleTextView.setText(ringTones.getTitle());
        holder.categoriesTextView.setText(ringTones.getCategories());
        holder.lengthTextView.setText(ringTones.getTotalLengthOfSound());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RingtonesPlay.class);
                intent.putExtra("image", ringTones.getImage());
                intent.putExtra("title", ringTones.getTitle());
                intent.putExtra("categories", ringTones.getCategories());

                // Pass the ringtone resource ID for the selected ringtone
                intent.putExtra("ringtoneUri", ringTones.getRingtoneResId());

                // Pass the full list of ringtone URIs, titles, and images
                ArrayList<Integer> ringtoneResIds = new ArrayList<>();
                ArrayList<String> ringtoneTitles = new ArrayList<>();
                ArrayList<Integer> ringtoneImages = new ArrayList<>();
                for (RingTonesItem item : ringTonesItems) {
                    ringtoneResIds.add(item.getRingtoneResId());
                    ringtoneTitles.add(item.getTitle());
                    ringtoneImages.add(item.getImage());
                }

                // Pass the list and the current index
                intent.putIntegerArrayListExtra("ringtoneUris", ringtoneResIds);
                intent.putStringArrayListExtra("ringtoneTitles", ringtoneTitles);
                intent.putIntegerArrayListExtra("ringtoneImages", ringtoneImages);
                intent.putExtra("currentIndex", position);

                context.startActivity(intent);
            }
        });


    }


    @Override
    public int getItemCount() {
        return ringTonesItems.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView titleTextView, categoriesTextView, lengthTextView;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageview);
            titleTextView = itemView.findViewById(R.id.title_text);
            categoriesTextView = itemView.findViewById(R.id.catagories_text);
            lengthTextView = itemView.findViewById(R.id.duration_text);
        }
    }
}

package com.iutbm.example.iutbm.couchot.meetit_1;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class CharacterViewHolder extends RecyclerView.ViewHolder{
    public TextView firstNameView, familyNameView, latitudeView, longitudeView;
    public ImageView pictureView;


    public CharacterViewHolder(View itemView) {
        super(itemView);
        firstNameView = (TextView) itemView.findViewById(R.id.text_view_character_first_name);
        familyNameView = (TextView) itemView.findViewById(R.id.text_view_character_familiy_name);
        latitudeView = (TextView) itemView.findViewById(R.id.text_view_character_latitude);
        longitudeView = (TextView) itemView.findViewById(R.id.text_view_character_longitude);
        pictureView = (ImageView) itemView.findViewById(R.id.image_view_character_picture);
    }
}
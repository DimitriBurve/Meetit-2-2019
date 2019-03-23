package com.iutbm.example.iutbm.couchot.meetit_1;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class CharacterListAdapter extends RecyclerView.Adapter<CharacterViewHolder> {

    private List<Character> characterList;

    private Context ctxt;

    public CharacterListAdapter(List<Character> characterList,Context ctxt) {
        this.characterList = characterList;
        this.ctxt = ctxt;
    }

    @Override
    public CharacterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_character, parent, false);
        return new CharacterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CharacterViewHolder holder, int position) {
        Character teacher = characterList.get(position);
        holder.firstNameView.setText(teacher.getFirstname());
        holder.familyNameView.setText(teacher.getFamilyname());
        holder.latitudeView.setText("" + teacher.getLatitude());
        holder.longitudeView.setText("" + teacher.getLongitude());

        try {
            String imageName=teacher.getBmppath();
            // get input stream
            InputStream ims = ctxt.getAssets().open(imageName);
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);
            // set image to ImageView
            holder.pictureView.setImageDrawable(d);
            ims .close();
        }
        catch(IOException ex) { return;}
    }

    @Override
    public int getItemCount() {
        return characterList.size();
    }
}
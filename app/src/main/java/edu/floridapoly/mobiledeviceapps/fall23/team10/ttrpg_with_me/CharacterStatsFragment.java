package edu.floridapoly.mobiledeviceapps.fall23.team10.ttrpg_with_me;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class CharacterStatsFragment extends Fragment {
    String name;
    String description;
    String image_url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getActivity().getIntent();
        name = intent.getStringExtra("CharacterName");
        description = intent.getStringExtra("CharacterDesc");
        image_url = intent.getStringExtra("CharacterImage");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_character_stats, container, false);

        TextView name_view = (TextView) view.findViewById(R.id.charstats_text_name);
        name_view.setText(name);
        TextView desc_view = (TextView) view.findViewById(R.id.charstats_text_desc);
        desc_view.setText(description);

        ImageView icon_view = (ImageView) view.findViewById(R.id.charstats_image_icon);
        Glide.with(view.getContext())
                .load(image_url)
                .placeholder(R.drawable.ic_loading_image)
                .into(icon_view);

        return view;
    }
}
package edu.floridapoly.mobiledeviceapps.fall23.team10.ttrpg_with_me;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.concurrent.atomic.AtomicBoolean;

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

        TextView name_view = view.findViewById(R.id.charstats_text_name);
        name_view.setText(name);
        TextView desc_view = view.findViewById(R.id.charstats_text_desc);
        desc_view.setText(description);

        ImageView icon_view = view.findViewById(R.id.charstats_image_icon);
        Glide.with(view.getContext())
                .load(image_url)
                .placeholder(R.drawable.ic_loading_image)
                .into(icon_view);

        setupStatBlocks(view.findViewById(R.id.statblocks_linear_1));
        setupStatBlocks(view.findViewById(R.id.statblocks_linear_2));

        setupStatBlocks(view.findViewById(R.id.savethrows_linear_1));
        setupStatBlocks(view.findViewById(R.id.savethrows_linear_2));
        setupStatBlocks(view.findViewById(R.id.savethrows_linear_3));

        setupSkillSelects(view.findViewById(R.id.skill_linear_container));

        return view;
    }

    private void setupStatBlocks(LinearLayout row) {
        final int elementCount = row.getChildCount();
        for (int i = 0; i < elementCount; i++) {
            View element = row.getChildAt(i);
            if (element instanceof CardView) {
                element.setOnClickListener(v -> Toast.makeText(getContext(), "TODO: Popup dialog to edit stat!", Toast.LENGTH_SHORT).show());
            }
        }
    }

    private void setupSkillSelects(LinearLayout container) {
        final int elementCount = container.getChildCount();
        for (int i = 0; i < elementCount; i++) {
            View element = container.getChildAt(i);
            if (element instanceof CardView) {
                RadioButton radio = element.findViewById(R.id.skillblock_radio_select);
                AtomicBoolean isChecked = new AtomicBoolean(false);
                radio.setOnClickListener(view -> {
                    isChecked.set(!isChecked.get());
                    radio.setChecked(isChecked.get());
                });

                TextView valueText = element.findViewById(R.id.skillblock_text_value);
                valueText.setOnClickListener(view -> Toast.makeText(getContext(), "TODO: Popup dialog to edit stat!", Toast.LENGTH_SHORT).show());
            }
        }
    }
}
package edu.floridapoly.mobiledeviceapps.fall23.team10.ttrpg_with_me;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.databinding.ObservableField;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import edu.floridapoly.mobiledeviceapps.fall23.team10.ttrpg_with_me.databinding.FragmentCharacterStatsBinding;
import edu.floridapoly.mobiledeviceapps.fall23.team10.ttrpg_with_me.databinding.StatBlockBinding;

public class CharacterStatsFragment extends Fragment {
    static final List<String> statNameArr = Arrays.asList("STR", "CON", "DEX", "INT", "WIS", "CHA");

    Character character;
    FragmentCharacterStatsBinding binding;

    List<View> StatBlocks;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getActivity().getIntent();
        int characterId = intent.getIntExtra("CharacterId", -1);
        if (characterId < 0) {
            getActivity().finish();
        }
        character = (Character) Character.getObject(Character.class, characterId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCharacterStatsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        TextView name_view = view.findViewById(R.id.charstats_text_name);
        name_view.setText(character.name);
        TextView desc_view = view.findViewById(R.id.charstats_text_desc);
        desc_view.setText(character.getDescription());

        ImageView icon_view = view.findViewById(R.id.charstats_image_icon);
        Glide.with(view.getContext())
                .load(character.image_url)
                .placeholder(R.drawable.ic_loading_image)
                .into(icon_view);

        StatBlocks = new ArrayList<>();
        setupStatBlocks(view.findViewById(R.id.statblocks_linear_1));
        setupStatBlocks(view.findViewById(R.id.statblocks_linear_2));

        int statIndex = 0;
        for (View block : StatBlocks) {
            String name = statNameArr.get(statIndex);
            StatBlockBinding blockBinding = StatBlockBinding.bind(block);
            blockBinding.setName(name);
            ObservableField<Integer> stat = character.stats.get(name);
            blockBinding.setStat(stat);
        }

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
                StatBlocks.add(element);
                element.setOnClickListener(v -> Toast.makeText(getContext(), "TODO: Popup dialog to edit stat!", Toast.LENGTH_SHORT).show());
            }
        }
    }

    private void setupSkillSelects(LinearLayout container) {
        final int elementCount = container.getChildCount();
        for (int i = 0; i < elementCount; i++) {
            View element = container.getChildAt(i);
            if (element instanceof CardView) {
                RadioButton radio = element.findViewById(R.id.skillblock_radio_select2);
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
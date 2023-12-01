package edu.floridapoly.mobiledeviceapps.fall23.team10.ttrpg_with_me;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.databinding.ObservableField;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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
        character = (Character) Character.getObject(Character.class, characterId - 1);
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
            ((TextView) block.findViewById(R.id.statblock_text_name)).setText(name);

            block.setOnClickListener(v -> promptValueChange(block, name));
            statIndex++;
        }

        setupStatBlocks(view.findViewById(R.id.savethrows_linear_1));
        setupStatBlocks(view.findViewById(R.id.savethrows_linear_2));
        setupStatBlocks(view.findViewById(R.id.savethrows_linear_3));

        setupSkillSelects(view.findViewById(R.id.skill_linear_container));

        return view;
    }

    private void promptValueChange(View element, String valueName) {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_value_change);
        Objects.requireNonNull(dialog.getWindow()).addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

        TextView nameText = dialog.findViewById(R.id.valuedialog_text_name);
        nameText.setText(valueName);
        EditText valueInput = dialog.findViewById(R.id.valuedialog_text_name);
        AppCompatButton saveButton = dialog.findViewById(R.id.valuedialog_button_save);

        TextView statText = element.findViewById(R.id.statblock_text_stat);
        TextView bonusText = element.findViewById(R.id.statblock_text_bonus);

        ObservableField<Integer> stat = character.stats.get(valueName);
        saveButton.setOnClickListener(v -> {
            int value = Integer.valueOf(valueInput.getText().toString());
            assert stat != null;

            statText.setText(String.valueOf(value));
            bonusText.setText(String.valueOf((value - 10) / 2));
            stat.set(value);
        });

        dialog.show();
    }

    private void setupStatBlocks(LinearLayout row) {
        final int elementCount = row.getChildCount();
        for (int i = 0; i < elementCount; i++) {
            View element = row.getChildAt(i);
            if (element instanceof CardView) {
                StatBlocks.add(element);
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
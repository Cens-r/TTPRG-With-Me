package edu.floridapoly.mobiledeviceapps.fall23.team10.ttrpg_with_me;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import edu.floridapoly.mobiledeviceapps.fall23.team10.ttrpg_with_me.databinding.FragmentCharacterStatsBinding;

public class CharacterStatsFragment extends Fragment {
    static final List<String> statNameArr = Arrays.asList("STR", "CON", "DEX", "INT", "WIS", "CHA");

    Character character;
    FragmentCharacterStatsBinding binding;

    List<View> StatBlocks;
    List<View> SaveThows;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCharacterStatsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        Intent intent = getActivity().getIntent();
        int characterId = intent.getIntExtra("CharacterId", -1);
        if (characterId < 0) {
            getActivity().finish();
        }
        character = (Character) Character.getObject(Character.class, characterId - 1);

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
        setupStatElements(StatBlocks, view.findViewById(R.id.statblocks_linear_1));
        setupStatElements(StatBlocks, view.findViewById(R.id.statblocks_linear_2));

        int statIndex = 0;
        for (View block : StatBlocks) {
            String name = statNameArr.get(statIndex);
            ((TextView) block.findViewById(R.id.statblock_text_name)).setText(name);
            TextView statText = block.findViewById(R.id.statblock_text_stat);
            TextView bonusText = block.findViewById(R.id.statblock_text_bonus);

            int value = character.stats.get(name);
            statText.setText(String.valueOf(value));
            bonusText.setText(String.valueOf((value - 10) / 2));

            block.setOnClickListener(v -> promptValueChange(block, name));
            statIndex++;
        }

        SaveThows = new ArrayList<>();
        setupStatElements(SaveThows, view.findViewById(R.id.savethrows_linear_1));
        setupStatElements(SaveThows, view.findViewById(R.id.savethrows_linear_2));
        setupStatElements(SaveThows, view.findViewById(R.id.savethrows_linear_3));

        statIndex = 0;
        for (View element: SaveThows) {
            String name = statNameArr.get(statIndex);
            ((TextView) element.findViewById(R.id.savethrow_text_name)).setText(name);

            Boolean saveBool = character.saveBools.get(name);
            UpdateSaveThrow(element, name, saveBool);

            RadioButton radiobutton = element.findViewById(R.id.savethrow_radio_button);

            AtomicBoolean isChecked = new AtomicBoolean(false);
            radiobutton.setOnClickListener(v -> {
                isChecked.set(!isChecked.get());
                radiobutton.setChecked(isChecked.get());
                UpdateSaveThrow(element, name, isChecked.get());
                character.saveBools.put(name, isChecked.get());
            });
            statIndex++;
        }

        setupSkillSelects(view.findViewById(R.id.skill_linear_container));

        return view;
    }

    private void UpdateSaveThrow(View element, String throwName, Boolean hasProf) {
        int pBonus = character.pBonus;
        int stat = character.stats.get(throwName);

        int incr = hasProf ? pBonus : 0;
        int value = stat + incr;
        character.savethrow.put(throwName, value);

        TextView valueText = element.findViewById(R.id.savethrow_text_value);
        valueText.setText(String.valueOf(value));
    }

    private void promptValueChange(View element, String valueName) {
        Dialog dialog = CreateDialog();

        EditText valueInput = dialog.findViewById(R.id.valuedialog_edittext_value);
        AppCompatButton saveButton = dialog.findViewById(R.id.valuedialog_button_save);
        TextView statText = element.findViewById(R.id.statblock_text_stat);
        TextView bonusText = element.findViewById(R.id.statblock_text_bonus);

        saveButton.setOnClickListener(v -> {
            int value = Integer.valueOf(valueInput.getText().toString());
            statText.setText(String.valueOf(value));
            bonusText.setText(String.valueOf((value - 10) / 2));
            character.stats.put(valueName, value);
            dialog.dismiss();
        });

        dialog.show();
    }

    private Dialog CreateDialog() {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_value_change);
        Objects.requireNonNull(dialog.getWindow()).addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        return dialog;
    }

    private void setupStatElements(List<View> list, LinearLayout row) {
        final int elementCount = row.getChildCount();
        for (int i = 0; i < elementCount; i++) {
            View element = row.getChildAt(i);
            if (element instanceof CardView) {
                list.add(element);
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
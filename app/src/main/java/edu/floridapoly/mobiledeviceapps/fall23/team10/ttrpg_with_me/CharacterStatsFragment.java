package edu.floridapoly.mobiledeviceapps.fall23.team10.ttrpg_with_me;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import edu.floridapoly.mobiledeviceapps.fall23.team10.ttrpg_with_me.databinding.FragmentCharacterStatsBinding;
import edu.floridapoly.mobiledeviceapps.fall23.team10.ttrpg_with_me.databinding.SavingThrowBlockBinding;
import edu.floridapoly.mobiledeviceapps.fall23.team10.ttrpg_with_me.databinding.SkillBlockBinding;
import edu.floridapoly.mobiledeviceapps.fall23.team10.ttrpg_with_me.databinding.StatBlockBinding;

public class CharacterStatsFragment extends Fragment {
    static final List<String> statArr = Arrays.asList("STR", "CON", "DEX", "INT", "WIS", "CHA");
    static final List<String> skillArr = Collections.list(Character.profMap.keys());
    static final HashMap<String, String> statMap = new HashMap<String, String>() {{
        put("STR", "Strength"); put("CON", "Constitution"); put("DEX", "Dexterity");
        put("INT", "Intelligence"); put("WIS", "Wisdom"); put("CHA", "Charisma");
    }};

    DatabaseManager db;
    FragmentCharacterStatsBinding binding;
    Character character;
    Hashtable<String, List<ViewDataBinding>> StatDependentViews;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCharacterStatsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        db = new DatabaseManager(getContext());
        Collections.sort(skillArr);

        Intent intent = getActivity().getIntent();
        int characterId = intent.getIntExtra("CharacterId", -1);
        if (characterId < 0) {
            getActivity().finish();
        }
        Log.d("Character", "Intent: " + characterId);
        character = (Character) Character.getObject(Character.class, characterId);

        StatDependentViews = new Hashtable<String, List<ViewDataBinding>>() {{
            put("STR", new ArrayList<>()); put("CON", new ArrayList<>()); put("DEX", new ArrayList<>());
            put("INT", new ArrayList<>()); put("WIS", new ArrayList<>()); put("CHA", new ArrayList<>());
        }};

        TextView name_view = view.findViewById(R.id.charstats_text_name);
        name_view.setText(character.name);
        TextView desc_view = view.findViewById(R.id.charstats_text_desc);
        desc_view.setText(character.getDescription());

        ImageView icon_view = view.findViewById(R.id.charstats_image_icon);
        Glide.with(view.getContext())
                .load(character.image_url)
                .placeholder(R.drawable.ic_loading_image)
                .into(icon_view);

        /*
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
         */



        SetupStatBlock(binding.statblocksLinear1, 0);
        SetupStatBlock(binding.statblocksLinear2, 3);

        SetupSaveThrow(binding.savethrowsLinear1, 0);
        SetupSaveThrow(binding.savethrowsLinear2, 2);
        SetupSaveThrow(binding.savethrowsLinear3, 4);

        SetupSkillBlock(binding.skillLinearContainer);



        /*
        SaveThows = new ArrayList<>();
        setupStatElements(SaveThows, view.findViewById(R.id.savethrows_linear_1));
        setupStatElements(SaveThows, view.findViewById(R.id.savethrows_linear_2));
        setupStatElements(SaveThows, view.findViewById(R.id.savethrows_linear_3));

        int statIndex = 0;
        for (View element: SaveThows) {
            String name = statArr.get(statIndex);
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
                db.update(character.id, "CHARACTERS", character.toJson());
            });
            statIndex++;
        }
         */

        //setupSkillSelects(view.findViewById(R.id.skill_linear_container));

        return view;
    }

    private void UpdateSaveThrow(View element, String throwName, Boolean hasProf) {
        int pBonus = character.pBonus;
        int stat = character.stats.get(throwName);

        int incr = hasProf ? pBonus : 0;
        int value = ((stat - 10) / 2) + incr;
        //character.savethrow.put(throwName, value);

        TextView valueText = element.findViewById(R.id.savethrow_text_value);
        //valueText.setText(String.valueOf(value));
    }

    private void promptValueChange(StatBlockBinding blockBinding, String valueName) {
        Dialog dialog = CreateDialog();
        EditText valueInput = dialog.findViewById(R.id.valuedialog_edittext_value);
        AppCompatButton saveButton = dialog.findViewById(R.id.valuedialog_button_save);

        saveButton.setOnClickListener(v -> {
            int value = Integer.valueOf(valueInput.getText().toString());
            character.stats.put(valueName, value);
            blockBinding.setValue(value);
            blockBinding.setBonus(character.calcStatBonus(valueName));

            character.stats.put(valueName, value);
            Character.save(blockBinding.getRoot().getContext(), character);

            List<ViewDataBinding> dependentViews = StatDependentViews.get(valueName);
            for (ViewDataBinding vBind : dependentViews) {
                vBind.setVariable(BR.character, character);
            }
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

    private void SetupStatBlock(LinearLayout container, int offset) {
        final int elementCount = container.getChildCount();
        for (int i = 0; i < elementCount; i++) {
            View element = container.getChildAt(i);
            if (!(element instanceof CardView)) { continue; }
            StatBlockBinding blockBinding = DataBindingUtil.getBinding(element);

            String name = statArr.get(i + offset);
            String properName = statMap.get(name);
            blockBinding.setName(properName);
            blockBinding.setValue(character.stats.get(name));
            blockBinding.setBonus(character.calcStatBonus(name));

            element.setOnClickListener(v -> promptValueChange(blockBinding, name));
        }
    }
    private void SetupSaveThrow(LinearLayout container, int offset) {
        final int elementCount = container.getChildCount();
        for (int i = 0; i < elementCount; i++) {
            View element = container.getChildAt(i);
            if (!(element instanceof CardView)) { continue; }
            SavingThrowBlockBinding blockBinding = DataBindingUtil.getBinding(element);
            String name = statArr.get(i + offset);

            StatDependentViews.get(name).add(blockBinding);
            blockBinding.setStat(name);
            blockBinding.setCharacter(character);

            int buttonState = character.proficiency.get(name);
            SetRadioSelect(blockBinding, blockBinding.savethrowRadioButton, name, buttonState == 1);
        }
    }
    private void SetupSkillBlock(LinearLayout container) {
        final int elementCount = container.getChildCount();
        int index = 0;
        for (int i = 0; i < elementCount; i++) {
            View element = container.getChildAt(i);
            if (!(element instanceof CardView)) { continue; }
            SkillBlockBinding blockBinding = DataBindingUtil.getBinding(element);
            String skillName = skillArr.get(index);
            String name = Character.profMap.get(skillName);

            StatDependentViews.get(name).add(blockBinding);

            blockBinding.setSkill(skillName);
            blockBinding.setMod(name);
            blockBinding.setCharacter(character);

            boolean select1Init = false;
            boolean select2Init = false;
            int profValue = character.proficiency.get(skillName);
            if (profValue > 0) { select1Init = true; profValue--; }
            if (profValue > 0) { select2Init = true; }

            SetRadioSelect(blockBinding, blockBinding.skillblockRadioSelect1, skillName, select1Init);
            SetRadioSelect(blockBinding, blockBinding.skillblockRadioSelect2, skillName, select2Init);

            index++;
        }
    }
    private void SetRadioSelect(ViewDataBinding vbinding, RadioButton radio, String profName, Boolean initValue) {
        AtomicBoolean isEnabled = new AtomicBoolean(initValue);
        radio.setChecked(initValue);

        radio.setOnClickListener(v -> {
            Context ctx = vbinding.getRoot().getContext();

            isEnabled.set(!isEnabled.get());
            radio.setChecked(isEnabled.get());
            character.setProf(ctx, profName, character.proficiency.get(profName) + (isEnabled.get() ? 1 : -1));
            vbinding.setVariable(BR.character, character);
        });
    }
}
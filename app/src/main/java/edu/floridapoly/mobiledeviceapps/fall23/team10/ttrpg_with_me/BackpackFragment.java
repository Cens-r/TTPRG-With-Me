package edu.floridapoly.mobiledeviceapps.fall23.team10.ttrpg_with_me;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.ObservableField;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import edu.floridapoly.mobiledeviceapps.fall23.team10.ttrpg_with_me.databinding.DisplayContainerBinding;
import edu.floridapoly.mobiledeviceapps.fall23.team10.ttrpg_with_me.databinding.FragmentBackpackBinding;

public class BackpackFragment extends Fragment {
    static Executor executor = Executors.newSingleThreadExecutor();
    static Handler handler = new Handler(Looper.getMainLooper());

    String[] nameList = {"Weapons", "Spells", "Armor", "Items", "Extras"};
    List<ItemContainer> itemContainers;

    FragmentBackpackBinding binding;
    static Character character;
    static DatabaseManager db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBackpackBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        Intent intent = getActivity().getIntent();
        int characterId = intent.getIntExtra("CharacterId", -1);
        if (characterId < 0) {
            getActivity().finish();
        }
        character = (Character) Character.getObject(Character.class, characterId - 1);

        db = new DatabaseManager(container.getContext());

        itemContainers = new ArrayList<>();
        ViewGroup linearLayout = rootView.findViewById(R.id.backpack_linear_container);
        for (int index = 0; index < linearLayout.getChildCount(); index++) {
            ItemContainer item = new ItemContainer(linearLayout.getChildAt(index), nameList[index]);
            itemContainers.add(item);
        }

        ImageButton createButton = rootView.findViewById(R.id.item_button_create);
        createButton.setOnClickListener(v -> {
            List<ClassManager> items = Item.getObjects(Item.class);
            if (items != null) {
                for (ClassManager managerItem : items) {
                    Item item = (Item) managerItem;
                    item.favorited(Boolean.FALSE.equals(item.favorited.get()));
                    Log.d("Backpack", item.toJson());
                }
            }
        });

        return rootView;
    }

    private static class ItemViewModel extends ViewModel {
        ObservableField<String> header;
        ObservableField<String> body;
        ObservableField<Boolean> favorite;
    }

    public static class ItemContainer {
        boolean isExpanded;
        List<View> itemList;

        View container;
        TextView headerText;
        ImageButton generateButton;
        ImageButton arrowButton;

        public ItemContainer(View container, String name) {
            isExpanded = true;
            itemList = new ArrayList<>();

            for (Item item : Objects.requireNonNull(character.Backpack.get(name))) {
                View itemView = createItem(item);
                itemList.add(itemView);
            }

            this.container = container;
            headerText = container.findViewById(R.id.item_text_header);
            generateButton = container.findViewById(R.id.item_button_generate);
            arrowButton = container.findViewById(R.id.item_button_arrow);

            headerText.setText(name);
            generateButton.setOnClickListener(view -> {
                executor.execute(() -> {
                    Item item = Item.Generate(name, null);
                    handler.post(() -> {
                        if (item != null) {
                            View itemView = this.createItem(item);
                            itemList.add(itemView);
                            db.update(character.id, "CHARACTERS", character.toJson());
                        } else {
                            Toast.makeText(this.container.getContext(), "Couldn't create item!", Toast.LENGTH_SHORT).show();
                        }
                    });
                });
            });
            arrowButton.setOnClickListener(view -> {
                ViewGroup layout = container.findViewById(R.id.item_linear_body);
                for (int index = 0; index < layout.getChildCount(); index++) {
                    View element = layout.getChildAt(index);
                    element.setVisibility((isExpanded) ? View.GONE : View.VISIBLE);
                }
                arrowButton.setImageResource((isExpanded) ? R.drawable.ic_collapse_arrow : R.drawable.ic_expand_arrow);
                isExpanded = !isExpanded;
            });

            ImageButton createButton = container.findViewById(R.id.item_button_create);
            createButton.setOnClickListener(v -> {
                Dialog dialog = new Dialog(container.getContext());
                dialog.setContentView(R.layout.dialog_create_item);
                Objects.requireNonNull(dialog.getWindow()).addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

                AppCompatButton saveButton = dialog.findViewById(R.id.createdialog_button_save);
                saveButton.setOnClickListener(view -> {
                    EditText header = dialog.findViewById(R.id.createdialog_editText_header);
                    EditText body = dialog.findViewById(R.id.createdialog_edittext_body);

                    Item item = new Item(header.getText().toString(), body.getText().toString());
                    character.Backpack.get(name).add(item);
                    db.update(character.id, "CHARACTERS", character.toJson());
                });
            });
        }

        View createItem(Item itemObject) {
            ViewGroup layout = container.findViewById(R.id.item_linear_body);
            LayoutInflater inflater = LayoutInflater.from(layout.getContext());
            View view = inflater.inflate(R.layout.display_container, layout, false);

            DisplayContainerBinding itemBinding = DisplayContainerBinding.bind(view);
            itemBinding.setHeader(itemObject.name);
            itemBinding.setBody(itemObject.description);
            itemBinding.setFavorited(itemObject.favorited);

            itemBinding.displayButtonFavorite.setOnClickListener(v -> {
                itemObject.favorited(Boolean.FALSE.equals(itemObject.favorited.get()));
            });

            if (!isExpanded) {
                view.setVisibility(View.GONE);
            }
            layout.addView(view);
            return view;
        }
    }
}
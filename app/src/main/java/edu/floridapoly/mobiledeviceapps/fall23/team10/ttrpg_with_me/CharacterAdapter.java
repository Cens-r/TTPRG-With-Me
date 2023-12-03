package edu.floridapoly.mobiledeviceapps.fall23.team10.ttrpg_with_me;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.MyViewHolder> {
    List<Character> characterList;
    Context context;
    DatabaseManager db;
    FileReaderWriterHelper frw;

    public CharacterAdapter(List<Character> characterList, Context context) {
        this.characterList = characterList;
        this.context = context;
    }

    @NonNull
    @Override
    public CharacterAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.character_list_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        db = new DatabaseManager(context);
        frw = new FileReaderWriterHelper(context);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CharacterAdapter.MyViewHolder holder, int position) {
        Character character = characterList.get(position);
        String url = character.getImageURL();

        holder.char_name.setText(character.getName());
        holder.char_desc.setText(character.getDescription());

        Glide.with(this.context)
                .load(url)
                .placeholder(R.drawable.ic_loading_image)
                .into(holder.char_icon);

        // When card is clicked on send relevant information to the next activity
        holder.card.setOnClickListener(view -> {
            Intent intent = new Intent(context, NavigationActivity.class);
            intent.putExtra("CharacterId", character.id);
            context.startActivity(intent);
        });

        holder.export_button.setOnClickListener(view -> {
            frw.CreateFile(character);
        });

        holder.delete_button.setOnClickListener(view -> {
            Cursor cursor = db.getAllItems(character.pk);
            if (cursor.moveToFirst()) {
                do {
                    int pkIndex = cursor.getColumnIndex("pk");
                    long pk = cursor.getLong(pkIndex);
                    db.delete(pk, "ITEMS");
                } while (cursor.moveToNext());
            }

            db.delete(character.pk, "CHARACTERS");
            Character.untrackObject(character);
            characterList.remove(character);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return characterList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView char_icon;
        TextView char_name;
        TextView char_desc;

        CardView card;
        ImageButton delete_button;
        ImageButton export_button;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            char_icon = itemView.findViewById(R.id.charselect_image_item);
            char_name = itemView.findViewById(R.id.charselect_text_itemname);
            char_desc = itemView.findViewById(R.id.charSelect_text_itemdesc);

            card = itemView.findViewById(R.id.charselect_card_item);
            delete_button = itemView.findViewById(R.id.charselect_button_delete);
            export_button = itemView.findViewById(R.id.charselect_button_export);
        }
    }
}

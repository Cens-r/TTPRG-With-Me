package edu.floridapoly.mobiledeviceapps.fall23.team10.ttrpg_with_me;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class ItemAdapter extends RecyclerView.Adapter <ItemAdapter.MyViewHolder> {
    List<Item> itemList;
    Context context;
    DatabaseManager db;

    public ItemAdapter(List<Item> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
        db = new DatabaseManager(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.display_container, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Item item = itemList.get(position);
        String header = item.name;
        String body = item.description;
        boolean favorited = item.favorited;

        holder.headerView.setText(header);
        holder.bodyView.setText(body);
        holder.favoriteButton.setImageResource(favorited ? R.drawable.ic_star_fill : R.drawable.ic_star_outline);

        holder.favoriteButton.setOnClickListener(view -> {
            int imageId = (Boolean.TRUE.equals(item.favorited)) ? R.drawable.ic_star_outline : R.drawable.ic_star_fill;
            holder.favoriteButton.setImageResource(imageId);
            item.favorited(Boolean.FALSE.equals(item.favorited));
            Collections.sort(itemList, new Comparator<Item>() {
                @Override
                public int compare(Item o1, Item o2) {
                    return (o1.favorited && !o2.favorited) ? -1 : (!o1.favorited && o2.favorited) ? 1 : 0;
                }
            });
            db.update(item.pk, "ITEMS", item.toJson());
            notifyDataSetChanged();
        });
        holder.deleteButton.setOnClickListener(view -> {
            itemList.remove(item);
            db.delete(item.pk, "ITEMS");
            notifyDataSetChanged();
        });
        holder.editButton.setOnClickListener(view -> {

            DatabaseManager db = new DatabaseManager(context);
            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_create_item);
            Objects.requireNonNull(dialog.getWindow()).addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

            Window window = dialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            AppCompatButton saveButton = dialog.findViewById(R.id.createdialog_button_save);

            EditText headerEdit = dialog.findViewById(R.id.createdialog_editText_header);
            EditText bodyEdit = dialog.findViewById(R.id.createdialog_edittext_body);
            headerEdit.setText(item.name);
            bodyEdit.setText(item.description);

            dialog.show();

            saveButton.setOnClickListener(v ->{

                item.description = bodyEdit.getText().toString();
                item.name = headerEdit.getText().toString();
                holder.headerView.setText(item.name);
                holder.bodyView.setText(item.description);
                db.update(item.pk, "ITEMS", item.toJson());
                dialog.dismiss();
            });


        });
    }

    @Override
    public int getItemCount() { return itemList.size(); }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView headerView;
        TextView bodyView;
        ImageButton favoriteButton;
        ImageButton deleteButton;
        ImageButton editButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            headerView = itemView.findViewById(R.id.display_text_header);
            bodyView = itemView.findViewById(R.id.display_text_body);
            favoriteButton = itemView.findViewById(R.id.display_button_favorite);
            deleteButton = itemView.findViewById(R.id.display_button_delete);
            editButton = itemView.findViewById(R.id.display_button_edit);

        }
    }
}

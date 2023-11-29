package edu.floridapoly.mobiledeviceapps.fall23.team10.ttrpg_with_me;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DisplayAdapter extends RecyclerView.Adapter <DisplayAdapter.MyViewHolder> {
    List<DisplayObject> displayList;
    Context context;

    public DisplayAdapter(List<DisplayObject> displayList, Context context) {
        this.displayList = displayList;
        this.context = context;
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
        DisplayObject display = displayList.get(position);
        String header = display.getHeader();
        String body = display.getBody();

        holder.headerView.setText(header);
        holder.bodyView.setText(body);

        holder.favoriteButton.setOnClickListener(view -> {
            int imageId = (display.favorited) ? R.drawable.ic_star_outline : R.drawable.ic_star_fill;
            holder.favoriteButton.setImageResource(imageId);
            display.setFavorited(!display.favorited);
        });
    }

    @Override
    public int getItemCount() { return displayList.size(); }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView headerView;
        TextView bodyView;
        ImageButton favoriteButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            headerView = itemView.findViewById(R.id.display_text_header);
            bodyView = itemView.findViewById(R.id.display_text_body);
            favoriteButton = itemView.findViewById(R.id.display_image_favorite);
        }
    }
}

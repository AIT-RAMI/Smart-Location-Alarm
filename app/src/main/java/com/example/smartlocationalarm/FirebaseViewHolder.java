package com.example.smartlocationalarm;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

public class FirebaseViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
    public TextView _name, _date;

    public FirebaseViewHolder(@NonNull View itemView) {
        super(itemView);

        _name = itemView.findViewById(R.id.name);
        _date = itemView.findViewById(R.id.date);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select menu");
        menu.add(0, 0, getAdapterPosition(),"Delete");
    }
}

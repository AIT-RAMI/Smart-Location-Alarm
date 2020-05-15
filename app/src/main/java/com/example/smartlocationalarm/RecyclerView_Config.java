package com.example.smartlocationalarm;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerView_Config {
    private Context mContext;
    private alarmAdapter malarmAdapter;

    public void setConfig(RecyclerView recyclerView, Context context, List<alarm> alarms, List<String> keys) {
        mContext = context;
        malarmAdapter = new alarmAdapter(alarms, keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(malarmAdapter);


    }

    class alarmItemView extends RecyclerView.ViewHolder {
        private TextView name_, note_, radius_;
        private String longitude, latitude;

        private String key;

        public alarmItemView(ViewGroup parent) {
            super(LayoutInflater.from(mContext).inflate(R.layout.alarm_list_item, parent, false));

            name_ = itemView.findViewById(R.id.name_txt);
            note_ = itemView.findViewById(R.id.note_txt);
            radius_ = itemView.findViewById(R.id.radius_txt);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, alarmDetailsActivity.class);
                    intent.putExtra("key", key);
                    intent.putExtra("name", name_.getText().toString());
                    intent.putExtra("notes", note_.getText().toString());
                    intent.putExtra("radius", radius_.getText().toString());
                    intent.putExtra("longitude", longitude);
                    intent.putExtra("latitude", latitude);
                    mContext.startActivity(intent);

                }
            });
        }

        public void bind(alarm alarm, String key) {
            name_.setText(alarm.getName());
            note_.setText(alarm.getNotes());
            radius_.setText(alarm.getRadius());
            longitude = alarm.getLongitude();
            latitude = alarm.getLatitude();
            this.key = key;
        }
    }

    class alarmAdapter extends RecyclerView.Adapter<alarmItemView> {
        private List<alarm> mAlarmList;
        private List<String> mKeyList;

        public alarmAdapter(List<alarm> mAlarmList, List<String> mKeyList) {
            this.mAlarmList = mAlarmList;
            this.mKeyList = mKeyList;
        }

        @NonNull
        @Override
        public alarmItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new alarmItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull alarmItemView holder, int position) {
            holder.bind(mAlarmList.get(position), mKeyList.get(position));
        }

        @Override
        public int getItemCount() {
            return mAlarmList.size();
        }
    }
}

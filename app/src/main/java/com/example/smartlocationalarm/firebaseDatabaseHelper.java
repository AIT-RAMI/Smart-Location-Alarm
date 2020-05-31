package com.example.smartlocationalarm;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class firebaseDatabaseHelper {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDref;
    private List<alarm> alarms = new ArrayList<>();

    //    SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
//    final String uniqueId = prefs.getString("UUID","alarm");
    public firebaseDatabaseHelper(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("prefs", context.MODE_PRIVATE);
        final String uniqueId = prefs.getString("UUID", "alarm");
        mDatabase = FirebaseDatabase.getInstance();
        mDref = mDatabase.getReference().child(uniqueId);
    }

    public void readAlarms(final DataStatus dataStatus) {
        mDref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                alarms.clear();

                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    alarm mAlarm = keyNode.getValue(alarm.class);
                    alarms.add(mAlarm);
                }
                dataStatus.DataIsLoaded(alarms, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }


    public void updateAlarm(String key, alarm alarm, final DataStatus dataStatus) {
        mDref.child(key).setValue(alarm).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsUpdated();
            }
        });
    }

    public void deleteAlarm(String key, final DataStatus dataStatus) {
        mDref.child(key).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsDeleted();
            }
        });
    }

    public interface DataStatus {
        void DataIsLoaded(List<alarm> alarms, List<String> keys);

        void DataIsInserted();

        void DataIsUpdated();

        void DataIsDeleted();
    }
}


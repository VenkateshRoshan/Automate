package com.example.automate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.automate.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutMa;

    public HomeFragment homeFragment = new HomeFragment();
    public ResultFragment resultFragment = new ResultFragment();

    public BottomNavigationView bottomNavigationView;

    ArrayList<ListItem> arrayList;

    ActivityMainBinding mBinding;

    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();

    DatabaseReference Data = FirebaseDatabase.getInstance().getReference("/Data");

    private final String TAG = "Automate";

    StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        //bottomNavigationView = mBinding.bottomNav;

        //bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        arrayList = new ArrayList<>();

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutMa = new LinearLayoutManager(getApplicationContext());
        mAdapter = new AdapterList(arrayList);
        mRecyclerView.setLayoutManager(mLayoutMa);
        mRecyclerView.setAdapter(mAdapter);

        mRef.child("count").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final Long count = snapshot.getValue(Long.class);
                Log.v("Counter : ", "" + count);

                mRef.child("Data").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String text = ds.getValue(String.class);
                            Log.d(TAG, "onDataChange: " + text);
                            arrayList.add(new ListItem(text));
                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mBinding.floating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View view = getLayoutInflater().inflate(R.layout.dialog_layout, null);

                final TextInputLayout textInputLayout = view.findViewById(R.id.text_layout);

                final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("AlertDialog")
                        .setView(view)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mRef.child("count")
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                final int count = snapshot.getValue(Integer.class);
                                                mRef.child("Data")
                                                        .child(String.valueOf(count))
                                                        .setValue(textInputLayout.getEditText().getText().toString())
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                increamentCounter(count);
                                                            }
                                                        });
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                alertDialog.show();
            }
        });


    }

    private void increamentCounter(int count) {
        mRef.child("count")
                .setValue(count + 1);
    }
}
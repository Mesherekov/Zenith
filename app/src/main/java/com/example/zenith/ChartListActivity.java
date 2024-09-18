package com.example.zenith;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChartListActivity extends AppCompatActivity {
    private TextView email;
    private EditText name;
    private ImageButton logout;
    private FirebaseAuth mfirebaseAuth;
    private DatabaseReference mdatabase;
    private final String USER_KEY = "User";
    private FirebaseUser currentuser;
    RecyclerView recyclerView;
    private SearchView search;
    List<Item> items;
    CustomAdapter adapter;
    Drawable namedraw;
    String checkS;
    private BottomNavigationView bview;
    private ConstraintLayout chatlayout, profilelayout;
    private String userID;
    private DataSnapshot dataSnapshot;
    private boolean isreadytoupdate = false;
    ImageButton pencil;

    @Override
    protected void onStart() {
        super.onStart();
        getData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_list);
        init();
        currentuser = mfirebaseAuth.getCurrentUser();
        assert currentuser != null;
        email.setText(currentuser.getEmail());
        logout.setOnClickListener(view -> {
            mfirebaseAuth.signOut();
            Intent intent = new Intent(ChartListActivity.this, MainActivity.class);
            startActivity(intent);
        });
        bview.setOnItemSelectedListener(item -> {
           if(item.getItemId()==R.id.chat){
             item.setChecked(true);
             chatlayout.setVisibility(View.VISIBLE);
             profilelayout.setVisibility(View.INVISIBLE);
           }
            if(item.getItemId()== R.id.profile){
                item.setChecked(true);
                chatlayout.setVisibility(View.INVISIBLE);
                profilelayout.setVisibility(View.VISIBLE);
            }

            return false;
        });
        pencil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isreadytoupdate){
                    updateData(name.getText().toString());
                    name.setEnabled(false);
                    name.setBackground(null);
                    isreadytoupdate = false;
                } else {
                    isreadytoupdate = true;
                    name.setEnabled(true);
                    name.setBackground(namedraw);
                }
            }
        });

    }
    private void init(){
        email = findViewById(R.id.ema);
        logout = findViewById(R.id.logout);
        name = findViewById(R.id.namechar);
        search = findViewById(R.id.search);
        search.clearFocus();
        mfirebaseAuth = FirebaseAuth.getInstance();
        mdatabase = FirebaseDatabase.getInstance().getReference(USER_KEY);
        recyclerView = findViewById(R.id.recyclerview);
        items = new ArrayList<Item>();
        adapter = new CustomAdapter(getApplicationContext(), items);
        bview = findViewById(R.id.bottomNavigationView);
        chatlayout = findViewById(R.id.chatlayout);
        profilelayout = findViewById(R.id.profilelayout);
        pencil = findViewById(R.id.pencil);
        namedraw = name.getBackground();
        name.setBackground(null);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(items.size() > 0){
                    items.clear();
                }
                for (DataSnapshot ds : snapshot.getChildren()) {
                    User user = ds.getValue(User.class);
                    assert user != null;
                    items.add(new Item(user.name, R.drawable.profileicon));
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mdatabase.addValueEventListener(valueEventListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                checkS = s;
                if(TextUtils.isEmpty(s)){
                    recyclerView.setVisibility(View.INVISIBLE);
                }
                else {
                    recyclerView.setVisibility(View.VISIBLE);
                    filterList(s);
                }
                return true;
            }
        });

    }

    private void filterList(String s) {
        List<Item> filterlist = new ArrayList<>();
        for(Item item : items){
            if(item.name.toLowerCase().contains(s.toLowerCase())){
                filterlist.add(item);
            }
        }
        if(filterlist.isEmpty()){
            if(!TextUtils.isEmpty(checkS)) {

            }
        } else {
            adapter.setFilterList(filterlist);
        }
    }

    private void getData(){
        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    User user = ds.getValue(User.class);
                    assert user != null;
                    if(user.email.equals(currentuser.getEmail())){
                        name.setText(user.name);
                        userID = user.id;
                        dataSnapshot = ds;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mdatabase.addValueEventListener(vListener);
    }
    private void updateData(String upname){
        assert dataSnapshot!=null;
        dataSnapshot.getRef().child("name").setValue(upname);
    }

}
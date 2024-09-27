package com.example.zenith;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ChartListActivity extends AppCompatActivity implements SelectListener{
    private TextView email;
    private EditText name;
    private ImageButton logout;
    private FirebaseAuth mfirebaseAuth;
    private DatabaseReference mdatabase;
    private StorageReference mstorage;
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
    private ImageButton pencil, changeavatar;
    private ImageView useravatar;
    private Uri uploaduri;
    private boolean finduser = false;
    private Drawable reserveAvatar;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_list);
        init();
        getData();
        currentuser = mfirebaseAuth.getCurrentUser();
        assert currentuser != null;
        email.setText(currentuser.getEmail());
        logout.setOnClickListener(view -> {
            try {
                mfirebaseAuth.signOut();
            } catch (Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(ChartListActivity.this, MainActivity.class);
            startActivity(intent);
        });
        changeavatar.setOnClickListener(view -> {
            ImagePicker.with(this)
                    .crop()	    			//Crop image(Optional), Check Customization for more option
                    .compress(1024)			//Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                    .start();
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
        pencil.setOnClickListener(view -> {
            if(isreadytoupdate){
                updateData(name.getText().toString(), "name");
                name.setEnabled(false);
                name.setBackground(null);
                isreadytoupdate = false;
            } else {
                isreadytoupdate = true;
                name.setEnabled(true);
                name.setBackground(namedraw);
            }
        });
    }
    private void uploadImage(){
        Bitmap bitmap =((BitmapDrawable) useravatar.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();
        final StorageReference mref = mstorage.child(System.currentTimeMillis()+ "user_image");
        UploadTask uptask = mref.putBytes(bytes);
        Task<Uri> task = uptask.continueWithTask(task1 -> mref.getDownloadUrl()).addOnCompleteListener(task12 -> uploaduri = task12.getResult());
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        assert data != null;
        Uri uri = data.getData();
        useravatar.setImageURI(uri);
        try {
            uploadImage();
            updateData(uri.toString(), "imageUri");
        }catch (Exception ex){
            assert reserveAvatar!=null;
            useravatar.setImageDrawable(reserveAvatar);
        }
    }

    private void init(){
        email = findViewById(R.id.ema);
        logout = findViewById(R.id.logout);
        name = findViewById(R.id.namechar);
        search = findViewById(R.id.search);
        search.clearFocus();
        mfirebaseAuth = FirebaseAuth.getInstance();
        mdatabase = FirebaseDatabase.getInstance().getReference(USER_KEY);
        mstorage = FirebaseStorage.getInstance().getReference("ImageDB");
        recyclerView = findViewById(R.id.recyclerview);

        items = new ArrayList<>();
        adapter = new CustomAdapter(getApplicationContext(), items, this);
        bview = findViewById(R.id.bottomNavigationView);
        chatlayout = findViewById(R.id.chatlayout);
        profilelayout = findViewById(R.id.profilelayout);
        pencil = findViewById(R.id.pencil);
        changeavatar = findViewById(R.id.changeavatar);
        useravatar = findViewById(R.id.userimage);
        namedraw = name.getBackground();
        name.setBackground(null);


        Runnable run = () -> {
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
                        if (!user.email.equals(currentuser.getEmail())) {

                            Target target = new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                    Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                                    items.add(new Item(user.name, drawable, user.UID));
                                }

                                @Override
                                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {

                                }
                            };
                        /*Glide.with(ChartListActivity.this).load(user.imageUri).placeholder(R.drawable.profileicon).listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        }).into(exam);*/
                            Picasso.get().load(user.imageUri).resize(400, 400).centerCrop().placeholder(R.drawable.profileicon).into(target);
                        }

                    }
                    adapter.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            mdatabase.addValueEventListener(valueEventListener);
        };
        run.run();


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
            TextUtils.isEmpty(checkS);
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
                    if(user.UID.equals(currentuser.getUid())){
                        name.setText(user.name);
                        userID = user.id;
                        Picasso.get().load(user.imageUri).resize(400,400).centerCrop().placeholder(R.drawable.profileicon).into(useravatar, new Callback() {
                            @Override
                            public void onSuccess() {
                                reserveAvatar = useravatar.getDrawable();
                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });
                        dataSnapshot = ds;
                        finduser = true;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        mdatabase.addValueEventListener(vListener);
    }
    private void updateData(String upobject, String pathupdate){
        assert dataSnapshot!=null;
        dataSnapshot.getRef().child(pathupdate).setValue(upobject);
    }

    @Override
    public void onItemClick(Item item) {
        Intent intent = new Intent(ChartListActivity.this, UserChatActivity.class);
        intent.putExtra("NameUser", item.getName());
        intent.putExtra("UID", item.getUID());
        Bitmap bitmap = ((BitmapDrawable) item.getImage()).getBitmap();
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bs);
        intent.putExtra("byteArray", bs.toByteArray());
        intent.putExtra("currentuser", currentuser.getUid());
        startActivity(intent);
    }
}
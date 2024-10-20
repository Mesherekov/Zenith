package com.example.zenith;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import java.util.Objects;

public class ChartListActivity extends AppCompatActivity implements SelectListener, SelectFriendsListener, SelectListenerDelFriend{
    private TextView email, numfriends, solid;
    private EditText name;
    private ImageButton logout, close, delete, settings, closesett;
    private FirebaseAuth mfirebaseAuth;
    private DatabaseReference mdatabase;
    private StorageReference mstorage;
    private final String USER_KEY = "User";
    private FirebaseUser currentuser;
    private RecyclerView recyclerView, recyclerViewfriends;
    private SearchView search;
    private CustomFriendsAdapter customFriendsAdapter;
    private List<ItemFriends> itemFriends;
    private List<Item> items;
    private CustomAdapter adapter;
    private Drawable namedraw;
    private String checkS;
    private List<String> FriendsKey;
    private BottomNavigationView bview;
    private ConstraintLayout chatlayout, profilelayout, settingslayout;
    private String userID;
    private DataSnapshot dataSnapshot;
    private DatabaseReference friendsdatasnapshot;
    private boolean isreadytoupdate = false;
    private ImageButton pencil, changeavatar;
    private ImageView useravatar;
    private Uri uploaduri, newuploaduri;
    ValueEventListener vListener, valueEventListener;
    private boolean finduser = false;
    private Drawable reserveAvatar;
    private List<String> imageUriFriends;
    private List<String> FriendUID;
    private String FriendKey;

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

        assert currentuser != null;
        email.setText(currentuser.getEmail());
        logout.setOnClickListener(view -> {
            try {
                mdatabase.removeEventListener(vListener);
                mdatabase.removeEventListener(valueEventListener);
                mfirebaseAuth.signOut();
            } catch (Exception e){
                Log.d("ERROR", e.getMessage());
            }

           Intent intent = new Intent(ChartListActivity.this, MainActivity.class);
            startActivity(intent);
        });
        changeavatar.setOnClickListener(view -> ImagePicker.with(this)
                .crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start());
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
        settings.setOnClickListener(view -> {
            settingslayout.setVisibility(View.VISIBLE);
            chatlayout.setVisibility(View.GONE);
            profilelayout.setVisibility(View.GONE);
            bview.setClickable(false);
        });
        closesett.setOnClickListener(view -> {
            settingslayout.setVisibility(View.GONE);
            profilelayout.setVisibility(View.VISIBLE);
            bview.setClickable(true);
        });
        close.setOnClickListener(view -> {
            solid.setVisibility(View.GONE);
            close.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
            search.setVisibility(View.VISIBLE);
        });
        delete.setOnClickListener(view -> {
            friendsdatasnapshot.child(FriendKey).removeValue();
            solid.setVisibility(View.GONE);
            close.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
            search.setVisibility(View.VISIBLE);
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
    private void newuploadImage(){
        useravatar.setImageResource(R.drawable.genava);
        Bitmap bitmap =((BitmapDrawable) useravatar.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();
        final StorageReference mref = mstorage.child(System.currentTimeMillis()+ "user_image");
        UploadTask uptask = mref.putBytes(bytes);
        Task<Uri> task = uptask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return mref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                newuploaduri = task.getResult();
            }
        });
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

    @SuppressLint({"NotifyDataSetChanged", "UseCompatLoadingForDrawables"})
    private void init(){
        email = findViewById(R.id.ema);
        logout = findViewById(R.id.logout);
        name = findViewById(R.id.namechar);
        search = findViewById(R.id.search);
        delete = findViewById(R.id.delfriend);
        close = findViewById(R.id.closefrie);
        closesett = findViewById(R.id.closesettings);
        solid = findViewById(R.id.solidfriend);
        settings = findViewById(R.id.settings);
        settingslayout = findViewById(R.id.settingslayout);
        search.clearFocus();
        mfirebaseAuth = FirebaseAuth.getInstance();
        currentuser = mfirebaseAuth.getCurrentUser();
        mdatabase = FirebaseDatabase.getInstance().getReference(USER_KEY);
        friendsdatasnapshot = FirebaseDatabase.getInstance().getReference("Friends").child(currentuser.getUid());
        mstorage = FirebaseStorage.getInstance().getReference("ImageDB");
        recyclerView = findViewById(R.id.recyclerview);
        recyclerViewfriends = findViewById(R.id.friendrecycler);

        items = new ArrayList<>();
        imageUriFriends = new ArrayList<>();
        itemFriends = new ArrayList<>();
        FriendUID = new ArrayList<>();
        FriendsKey = new ArrayList<>();
        customFriendsAdapter = new CustomFriendsAdapter(ChartListActivity.this, itemFriends, this, this);
        adapter = new CustomAdapter(getApplicationContext(), items, this);
        bview = findViewById(R.id.bottomNavigationView);
        chatlayout = findViewById(R.id.chatlayout);
        profilelayout = findViewById(R.id.profilelayout);
        pencil = findViewById(R.id.pencil);
        changeavatar = findViewById(R.id.changeavatar);
        useravatar = findViewById(R.id.userimage);
        numfriends = findViewById(R.id.numfriends);
        namedraw = name.getBackground();
        name.setBackground(null);

        Runnable run = () -> {
            valueEventListener = new ValueEventListener() {
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
                            imageUriFriends.add(user.imageUri);
                        }

                    }
                    adapter.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            mdatabase.addValueEventListener(valueEventListener);
            ValueEventListener eventListener = new ValueEventListener() {
                @SuppressLint({"NotifyDataSetChanged", "UseCompatLoadingForDrawables", "SetTextI18n"})
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(itemFriends.size()>0) itemFriends.clear();
                    if(FriendsKey.size()>0) FriendsKey.clear();
                    final int[] numoffriends = {0};
                    for (DataSnapshot ds: snapshot.getChildren()) {
                        Friends friends = ds.getValue(Friends.class);
                        FriendsKey.add(ds.getKey());
                        assert friends!=null;
                        Target target1 = new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                                itemFriends.add(new ItemFriends(friends.Name, drawable, friends.UID));
                                FriendUID.add(friends.UID);
                                numoffriends[0]++;
                            }

                            @Override
                            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        };
                        Picasso.get().load(friends.PNG).resize(400, 400).centerCrop().placeholder(R.drawable.profileicon).into(target1);

                    }
                   customFriendsAdapter.notifyDataSetChanged();
                    numfriends.setText("Number of friends: "+numoffriends[0]);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            try {
                friendsdatasnapshot.addValueEventListener(eventListener);
            }catch (Exception ex){
                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        run.run();


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerViewfriends.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewfriends.setAdapter(customFriendsAdapter);
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
                    recyclerViewfriends.setVisibility(View.VISIBLE);
                }
                else {
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerViewfriends.setVisibility(View.INVISIBLE);
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
            recyclerViewfriends.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            adapter.setFilterList(filterlist);
            recyclerViewfriends.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void getData(){
        vListener = new ValueEventListener() {
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
                if(!finduser){
                    String id = mdatabase.getKey();
                    String name = Objects.requireNonNull(currentuser.getEmail()).substring(0, 5);
                    String email = currentuser.getEmail();
                    String password = String.valueOf(currentuser.getUid().hashCode());
                    newuploadImage();

                    User user = new User(id, name, email, password, "newuploaduri.toString()", currentuser.getUid());
                    mdatabase.push().setValue(user);

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
    public void onItemClick(Item item, int position) {
        Intent intent = new Intent(ChartListActivity.this, UserChatActivity.class);
        intent.putExtra("NameUser", item.getName());
        intent.putExtra("UID", item.getUID());
        try {
            if(!FriendUID.contains(item.UID)) {
                String id = friendsdatasnapshot.getKey();
                Friends friends = new Friends(id, item.UID, item.getName(), imageUriFriends.get(position));
                friendsdatasnapshot.push().setValue(friends);
            }
        }catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        Bitmap bitmap = ((BitmapDrawable) item.getImage()).getBitmap();
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bs);
        intent.putExtra("byteArray", bs.toByteArray());
        intent.putExtra("currentuser", currentuser.getUid());
        startActivity(intent);
    }

    @Override
    public void onItemFriendClick(ItemFriends item, int position) {
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

    @Override
    public void onLongItemClick(ItemFriends item, int position) {
        solid.setVisibility(View.VISIBLE);
        close.setVisibility(View.VISIBLE);
        delete.setVisibility(View.VISIBLE);
        search.setVisibility(View.INVISIBLE);
        FriendKey = FriendsKey.get(position);
    }
}
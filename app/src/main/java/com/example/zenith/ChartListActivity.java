package com.example.zenith;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zenith.OnClickListeners.AddFriendListener;
import com.example.zenith.OnClickListeners.SelectFriendsListener;
import com.example.zenith.OnClickListeners.SelectListener;
import com.example.zenith.OnClickListeners.SelectListenerDelFriend;
import com.example.zenith.RelLayouts.CustomAdapter;
import com.example.zenith.RelLayouts.CustomFriendsAdapter;
import com.example.zenith.RelLayouts.CustomNotificationAdapter;
import com.github.dhaval2404.imagepicker.ImagePicker;
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
//@SuppressWarnings("all")
public class ChartListActivity extends AppCompatActivity implements SelectListener, SelectFriendsListener, SelectListenerDelFriend, AddFriendListener {
    private TextView email, numfriends, solid;
    private EditText name;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch sw_theme, sw_private;
    private ImageButton logout, close, delete, settings, closesett, bellnoti;
    private FirebaseAuth mfirebaseAuth;
    private SoundPool mSoundPool;
    private DatabaseReference mdatabase, notificationdatabase, mynotidatabase;
    private StorageReference mstorage;

    private User userown;
    private FirebaseUser currentuser;
    private RecyclerView recyclerView, recyclerViewfriends, notirecyclerView;
    private SearchView search;
    private CustomFriendsAdapter customFriendsAdapter;
    private CustomNotificationAdapter notiadapter;
    private List<ItemFriends> itemFriends;
    private List<ItemNotification> itemNotifications;
    private List<Item> items;
    private CustomAdapter adapter;
    private Drawable namedraw;
    private String checkS;
    private List<String> FriendsKey;
    private BottomNavigationView bview;
    private ConstraintLayout chatlayout, profilelayout, settingslayout, notificationlayout;
    private String userID;
    private SettingsDatabase settingsDatabase;
    private SQLiteDatabase sqLiteDatabase;
    private SharedPreferences spref;
    private boolean isfirst;
    private final String SAVED_TEXT = "savedtext";
    private DataSnapshot dataSnapshot;
    private DatabaseReference friendsdatasnapshot;
    private boolean isreadytoupdate = false;
    private ImageButton pencil, changeavatar;
    private ImageView useravatar, solidsett;
    private Uri uploaduri, newuploaduri;
    ValueEventListener vListener, valueEventListener, vnotilistener;
    private boolean finduser = false;
    private Drawable reserveAvatar;
    private List<String> imageUriFriends;
    private List<String> FriendUID;
    private String FriendKey;
    private ImageButton redtheme, bluetheme, blacktheme, yellowtheme, purpletheme, greentheme;
    private int savecol = Color.rgb(72, 61, 139);
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
                mynotidatabase.removeEventListener(vnotilistener);
                playSoundBool(3);
                mfirebaseAuth.signOut();
            } catch (Exception e){
                Log.d("ERROR", e.getMessage());
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
            playSoundBool(4);
        });
        bview.setOnItemSelectedListener(item -> {
            playSoundBool(2);
            settingslayout.setVisibility(View.GONE);
            notificationlayout.setVisibility(View.GONE);
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
        redtheme.setOnClickListener(view -> {
            playSoundBool(4);
            @SuppressLint("ResourceType") int colorFrom = savecol;
            int colorTo = Color.rgb(255, 0, 0);
            ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
            colorAnimation.setDuration(400); // milliseconds
            colorAnimation.addUpdateListener(animator -> {
                getWindow().setStatusBarColor((int) animator.getAnimatedValue());
                getWindow().setNavigationBarColor((int) animator.getAnimatedValue());
                savecol = colorTo;
                ContentValues contentValues = new ContentValues();
                contentValues.put(SettingsDatabase.COLOROFTHEME, colorTo);
                sqLiteDatabase.update(SettingsDatabase.TABLE_SETTINGS, contentValues, SettingsDatabase.ID + "= ?", new String[]{"1"});
            });
            colorAnimation.start();
        });
        greentheme.setOnClickListener(view -> {
            playSoundBool(4);
            @SuppressLint("ResourceType") int colorFrom = savecol;
            int colorTo = Color.rgb(0, 186, 127);
            ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
            colorAnimation.setDuration(400); // milliseconds
            colorAnimation.addUpdateListener(animator -> {
                getWindow().setStatusBarColor((int) animator.getAnimatedValue());
                getWindow().setNavigationBarColor((int) animator.getAnimatedValue());
                savecol = colorTo;
                ContentValues contentValues = new ContentValues();
                contentValues.put(SettingsDatabase.COLOROFTHEME, colorTo);
                sqLiteDatabase.update(SettingsDatabase.TABLE_SETTINGS, contentValues, SettingsDatabase.ID + "= ?", new String[]{"1"});
            });
            colorAnimation.start();
        });
        bluetheme.setOnClickListener(view -> {
            playSoundBool(4);
            @SuppressLint("ResourceType") int colorFrom = savecol;
            int colorTo = Color.rgb(72, 61, 139);
            ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
            colorAnimation.setDuration(400); // milliseconds
            colorAnimation.addUpdateListener(animator -> {
                getWindow().setStatusBarColor((int) animator.getAnimatedValue());
                getWindow().setNavigationBarColor((int) animator.getAnimatedValue());
                savecol = colorTo;
                ContentValues contentValues = new ContentValues();
                contentValues.put(SettingsDatabase.COLOROFTHEME, colorTo);
                sqLiteDatabase.update(SettingsDatabase.TABLE_SETTINGS, contentValues, SettingsDatabase.ID + "= ?", new String[]{"1"});
            });
            colorAnimation.start();
        });
        blacktheme.setOnClickListener(view -> {
            playSoundBool(4);
            @SuppressLint("ResourceType") int colorFrom = savecol;
            int colorTo = Color.rgb(0, 0, 0);
            ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
            colorAnimation.setDuration(400); // milliseconds
            colorAnimation.addUpdateListener(animator -> {
                getWindow().setStatusBarColor((int) animator.getAnimatedValue());
                getWindow().setNavigationBarColor((int) animator.getAnimatedValue());
                savecol = colorTo;
                ContentValues contentValues = new ContentValues();
                contentValues.put(SettingsDatabase.COLOROFTHEME, colorTo);
                sqLiteDatabase.update(SettingsDatabase.TABLE_SETTINGS, contentValues, SettingsDatabase.ID + "= ?", new String[]{"1"});
            });
            colorAnimation.start();
        });
        purpletheme.setOnClickListener(view -> {
            playSoundBool(4);
            @SuppressLint("ResourceType") int colorFrom = savecol;
            int colorTo = Color.rgb(194, 51, 147);
            ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
            colorAnimation.setDuration(400); // milliseconds
            colorAnimation.addUpdateListener(animator -> {
                getWindow().setStatusBarColor((int) animator.getAnimatedValue());
                getWindow().setNavigationBarColor((int) animator.getAnimatedValue());
                savecol = colorTo;
                ContentValues contentValues = new ContentValues();
                contentValues.put(SettingsDatabase.COLOROFTHEME, colorTo);
                sqLiteDatabase.update(SettingsDatabase.TABLE_SETTINGS, contentValues, SettingsDatabase.ID + "= ?", new String[]{"1"});
            });
            colorAnimation.start();
        });
        yellowtheme.setOnClickListener(view -> {
            playSoundBool(4);
            @SuppressLint("ResourceType") int colorFrom = savecol;
            int colorTo = Color.rgb(255, 235, 59);
            ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
            colorAnimation.setDuration(400); // milliseconds
            colorAnimation.addUpdateListener(animator -> {
                getWindow().setStatusBarColor((int) animator.getAnimatedValue());
                getWindow().setNavigationBarColor((int) animator.getAnimatedValue());
                savecol = colorTo;
                ContentValues contentValues = new ContentValues();
                contentValues.put(SettingsDatabase.COLOROFTHEME, colorTo);
                sqLiteDatabase.update(SettingsDatabase.TABLE_SETTINGS, contentValues, SettingsDatabase.ID + "= ?", new String[]{"1"});
            });
            colorAnimation.start();
        });
        bellnoti.setOnClickListener(view -> {
            chatlayout.setVisibility(View.GONE);
            profilelayout.setVisibility(View.GONE);
            notificationlayout.setVisibility(View.VISIBLE);
        });
        settings.setOnClickListener(view -> {
            playSoundBool(3);
            settingslayout.setVisibility(View.VISIBLE);
            chatlayout.setVisibility(View.GONE);
            profilelayout.setVisibility(View.GONE);
            bview.setClickable(false);
        });
        closesett.setOnClickListener(view -> {
            playSoundBool(3);
            settingslayout.setVisibility(View.GONE);
            profilelayout.setVisibility(View.VISIBLE);
            bview.setClickable(true);
        });
        close.setOnClickListener(view -> {
            solid.setVisibility(View.GONE);
            close.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
            search.setVisibility(View.VISIBLE);
            playSoundBool(3);
        });
        delete.setOnClickListener(view -> {
            friendsdatasnapshot.child(FriendKey).removeValue();
            solid.setVisibility(View.GONE);
            close.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
            search.setVisibility(View.VISIBLE);
            playSoundBool(3);
        });
        sw_theme.setOnCheckedChangeListener((compoundButton, b) -> {
            playSoundBool(1);
            ContentValues contentValues = new ContentValues();
            if (sw_theme.isChecked()) {
                contentValues.put(SettingsDatabase.THEME, "dark");
                sqLiteDatabase.update(SettingsDatabase.TABLE_SETTINGS, contentValues, SettingsDatabase.ID + "= ?", new String[]{"1"});
                setDarkTheme();
            }
            else {
                contentValues.put(SettingsDatabase.THEME, "light");
                sqLiteDatabase.update(SettingsDatabase.TABLE_SETTINGS, contentValues, SettingsDatabase.ID + "= ?", new String[]{"1"});
                setLightTheme();
            }

        });
        sw_private.setOnCheckedChangeListener((compoundButton, b) -> playSoundBool(1));

        pencil.setOnClickListener(view -> {
            playSoundBool(5);
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

    private void setLightTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    private void setDarkTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    }

    @SuppressLint({"ResourceAsColor", "ResourceType"})
    @Override
    protected void onResume() {
        super.onResume();
        int textcolor;
        if(sw_theme.isChecked()){
            textcolor = R.color.white;
            logout.setColorFilter(ContextCompat.getColor(this, R.color.White));
            closesett.setColorFilter(ContextCompat.getColor(this, R.color.White));
            pencil.setColorFilter(ContextCompat.getColor(this, R.color.White));
            changeavatar.setColorFilter(ContextCompat.getColor(this, R.color.White));
            settings.setColorFilter(ContextCompat.getColor(this, R.color.White), android.graphics.PorterDuff.Mode.MULTIPLY);
            solidsett.setImageResource(R.drawable.rectanglesolidblack);
            settings.setColorFilter(Color.argb(255, 255, 255, 255));
            ViewCompat.setBackgroundTintList(search, ContextCompat.getColorStateList(this, R.color.Blue));
        }
        else {
            textcolor = R.color.black;
            logout.setColorFilter(ContextCompat.getColor(this, R.color.Black));
            closesett.setColorFilter(ContextCompat.getColor(this, R.color.Black));
            pencil.setColorFilter(ContextCompat.getColor(this, R.color.Black));
            changeavatar.setColorFilter(ContextCompat.getColor(this, R.color.Black));
            settings.setColorFilter(ContextCompat.getColor(this, R.color.Black), android.graphics.PorterDuff.Mode.MULTIPLY);
            settings.setColorFilter(Color.argb(255, 0, 0, 0));
            solidsett.setImageResource(R.drawable.rectanglesolid);
        }
        email.setTextColor(ContextCompat.getColor(this, textcolor));
        name.setTextColor(ContextCompat.getColor(this, textcolor));
        sw_theme.setTextColor(ContextCompat.getColor(this, textcolor));
        sw_private.setTextColor(ContextCompat.getColor(this, textcolor));
        numfriends.setTextColor(ContextCompat.getColor(this, textcolor));
        bview.setSelectedItemId(R.id.chat);
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
        Task<Uri> task = uptask.continueWithTask(task1 -> mref.getDownloadUrl()).addOnCompleteListener(task12 -> newuploaduri = task12.getResult());
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

    @SuppressLint({"NotifyDataSetChanged", "UseCompatLoadingForDrawables", "WrongViewCast"})
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
        sw_private = findViewById(R.id.switchprivate);
        sw_theme = findViewById(R.id.switchtheme);
        redtheme = findViewById(R.id.redtheme);
        greentheme = findViewById(R.id.greentheme);
        blacktheme = findViewById(R.id.blacktheme);
        yellowtheme = findViewById(R.id.yellowtheme);
        purpletheme = findViewById(R.id.purpletheme);
        bellnoti = findViewById(R.id.bell);
        bluetheme = findViewById(R.id.bluetheme);
        settingslayout = findViewById(R.id.settingslayout);
        solidsett = findViewById(R.id.solidsett);
        mSoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
        mSoundPool.load(this, R.raw.switcher, 1);
        mSoundPool.load(this, R.raw.modern, 2);
        mSoundPool.load(this, R.raw.mouse, 3);
        mSoundPool.load(this, R.raw.selectcl, 4);
        mSoundPool.load(this, R.raw.pencils, 5);
        search.clearFocus();
        mfirebaseAuth = FirebaseAuth.getInstance();
        currentuser = mfirebaseAuth.getCurrentUser();
        mdatabase = FirebaseDatabase.getInstance().getReference(FirebaseHelper.USER_KEY);
        notificationdatabase = FirebaseDatabase.getInstance().getReference(FirebaseHelper.NOTIFICATION_KEY);
        mynotidatabase = notificationdatabase.child(String.valueOf(currentuser.getUid().hashCode()));
        friendsdatasnapshot = FirebaseDatabase.getInstance().getReference("Friends").child(currentuser.getUid());
        mstorage = FirebaseStorage.getInstance().getReference("ImageDB");
        recyclerView = findViewById(R.id.recyclerview);
        notirecyclerView = findViewById(R.id.relativeLayoutnoti);
        recyclerViewfriends = findViewById(R.id.friendrecycler);

        items = new ArrayList<>();
        imageUriFriends = new ArrayList<>();
        itemFriends = new ArrayList<>();
        FriendUID = new ArrayList<>();
        itemNotifications = new ArrayList<>();
        FriendsKey = new ArrayList<>();
        customFriendsAdapter = new CustomFriendsAdapter(ChartListActivity.this, itemFriends, this, this);
        adapter = new CustomAdapter(getApplicationContext(), items, this, this);
        notiadapter = new CustomNotificationAdapter(getApplicationContext(), itemNotifications);
        bview = findViewById(R.id.bottomNavigationView);
        chatlayout = findViewById(R.id.chatlayout);
        profilelayout = findViewById(R.id.profilelayout);
        notificationlayout = findViewById(R.id.Notification);
        pencil = findViewById(R.id.pencil);
        changeavatar = findViewById(R.id.changeavatar);
        useravatar = findViewById(R.id.userimage);
        numfriends = findViewById(R.id.numfriends);
        namedraw = name.getBackground();
        name.setBackground(null);
        settingsDatabase = new SettingsDatabase(this);
        sqLiteDatabase = settingsDatabase.getWritableDatabase();
        loadbool();
        if(isfirst){
            ContentValues contentValues = new ContentValues();
            contentValues.put(SettingsDatabase.LANGUAGE, "English");
            contentValues.put(SettingsDatabase.THEME, "light");
            contentValues.put(SettingsDatabase.COLOROFTHEME, Color.rgb(72, 61, 139));
            sqLiteDatabase.insert(SettingsDatabase.TABLE_SETTINGS, null, contentValues);
            isfirst = false;
            savebool();
        }else {
            Cursor cursor = sqLiteDatabase.query(SettingsDatabase.TABLE_SETTINGS, null, null, null, null, null, null);
            int theme = cursor.getColumnIndex(SettingsDatabase.THEME);
            int coloroftheme = cursor.getColumnIndex(SettingsDatabase.COLOROFTHEME);
            int language = cursor.getColumnIndex(SettingsDatabase.LANGUAGE);
            if(cursor.moveToFirst()){
                savecol = cursor.getInt(coloroftheme);
                getWindow().setStatusBarColor(savecol);
                getWindow().setNavigationBarColor(savecol);
                if(cursor.getString(theme).equals("dark")){
                    sw_theme.setChecked(true);
                    setDarkTheme();
                }
                cursor.close();
            }
        }
        Runnable runfriends = () -> {
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
                        DatabaseReference frdb = mdatabase.child(friends.KEY);
                        ValueEventListener friendlistener = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                User user = snapshot.getValue(User.class);
                                assert user != null;
                                ds.getRef().child("Name").setValue(user.name);
                                ds.getRef().child("PNG").setValue(user.imageUri);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        };
                        frdb.addValueEventListener(friendlistener);

                        FriendUID.add(friends.UID);
                        numoffriends[0]++;
                       if(sw_theme.isChecked()) {
                           itemFriends.add(new ItemFriends(friends.Name, null, friends.UID, Color.rgb(255,255,255), friends.PNG, friends.KEY));
                       }else {
                           itemFriends.add(new ItemFriends(friends.Name, null, friends.UID, Color.rgb(0,0,0), friends.PNG, friends.KEY));
                       }

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
                                    if(sw_theme.isChecked()) items.add(new Item(user.name, drawable, user.UID, Color.rgb(255,255,255), ds.getKey()));
                                    else items.add(new Item(user.name, drawable, user.UID, Color.rgb(0,0,0), ds.getKey()));
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
            vnotilistener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (itemNotifications.size() > 0) itemNotifications.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            Notification notification = ds.getValue(Notification.class);
                            assert notification != null;
                            if(sw_theme.isChecked()) itemNotifications.add(new ItemNotification(notification.Name, null, notification.UID, Color.rgb(255,255,255), notification.PNG, ds.getKey()));
                            else itemNotifications.add(new ItemNotification(notification.Name, null, notification.UID, Color.rgb(0,0,0), notification.PNG, ds.getKey()));
                        }
                        notiadapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            mynotidatabase.addValueEventListener(vnotilistener);

        };
        run.run();
        runfriends.run();
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
            notirecyclerView.setLayoutManager(new LinearLayoutManager(this));
            notirecyclerView.setAdapter(notiadapter);
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

    private void loadbool() {
        spref = getPreferences(MODE_PRIVATE);
        isfirst = spref.getBoolean(SAVED_TEXT, true);
    }

    private void savebool() {
        spref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = spref.edit();
        editor.putBoolean(SAVED_TEXT, isfirst);
        editor.apply();
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
                        userown = user;
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
                Friends friends = new Friends(id, item.UID, item.getName(), imageUriFriends.get(position), item.key);
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
        playSoundBool(2);
    }
    private void playSoundBool(int mSoundId){
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        float curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float leftVolume = curVolume / maxVolume;
        float rightVolume = curVolume / maxVolume;
        int priority = 1;
        int no_loop = 0;
        float normal_playback_rate = 1f;
        mSoundPool.play(mSoundId, leftVolume, rightVolume, priority, no_loop, normal_playback_rate);
    }

    @Override
    public void onLongItemClick(ItemFriends item, int position) {
        playSoundBool(2);
        solid.setVisibility(View.VISIBLE);
        close.setVisibility(View.VISIBLE);
        delete.setVisibility(View.VISIBLE);
        search.setVisibility(View.INVISIBLE);
        FriendKey = FriendsKey.get(position);
    }

    @Override
    public void addFriendOnClick(Item item, int position) {
        String id = notificationdatabase.getKey();
        Notification noti = new Notification(userown.id, currentuser.getUid(), userown.name, userown.imageUri, FirebaseHelper.TYPE_ADD_TO_FRIENDS);
        notificationdatabase.child(String.valueOf(item.UID.hashCode())).push().setValue(noti);
    }
}
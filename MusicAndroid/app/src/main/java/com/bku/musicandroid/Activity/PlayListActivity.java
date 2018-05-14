package com.bku.musicandroid.Activity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bku.musicandroid.Adapter.PlayListAdapter;
import com.bku.musicandroid.Adapter.SearchAdapter;
import com.bku.musicandroid.Model.PlayListOnlineInfo;
import com.bku.musicandroid.Model.SongPlayerOnlineInfo;
import com.bku.musicandroid.R;
import com.bku.musicandroid.Utility.UtilitySongOfflineClass;
import com.bku.musicandroid.Utility.UtilitySongOnlineClass;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Welcome on 5/14/2018.
 */

public class PlayListActivity extends AppCompatActivity {

    public static final String Song_Of_PlayList="All_Song_Of_PlayList_Database";
    public static final String Liked_Path="All_Liked_PlayList_Database";
    public static final String PlayList_Database="All_PlayList_Info_Database";
    public static final String View_Database="All_View_PlayList_Database";
    RecyclerView recyclerView;
    private TextView namePlayList;
    private ImageView liked,backGround;

    ArrayList<SongPlayerOnlineInfo> lstSong;

    int  nPosition=0;
    String playListId="";

    PlayListOnlineInfo playListOnlineInfo;
    private boolean isLiked=false;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewplaylistonline);

        namePlayList=findViewById(R.id.namePlayList);
        liked=(ImageView)findViewById(R.id.Liked);
        backGround=(ImageView)findViewById(R.id.blurFrame);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAuth=FirebaseAuth.getInstance();
        final String userId=mAuth.getCurrentUser().getUid();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            playListId=extras.getString("PlayListId");
            getPlayListInfo(playListId);
        }

        lstSong=new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Liked_Path);
        databaseReference.child(playListId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    if (singleSnapshot.getKey().equals(userId)) {
                        isLiked = true;
                        liked.setImageResource(R.drawable.ic_faved_video);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference databaseReference1= FirebaseDatabase.getInstance().getReference(Song_Of_PlayList);
        databaseReference1.child(playListId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    lstSong.add(singleSnapshot.getValue(SongPlayerOnlineInfo.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        PlayListAdapter searchAdapter=new PlayListAdapter(this,lstSong);
        searchAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(searchAdapter);

        liked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLiked) {
                    isLiked = false;
                    liked.setImageResource(R.drawable.ic_fav_song);
                    UnLikedSong(userId,playListId);
                } else {
                    isLiked = true;
                    liked.setImageResource(R.drawable.ic_faved_video);
                    LikedSong(userId,playListId);
                }
            }
        });

        //increase 1 view when click to this acitivity
        upView(playListId);



    }

    //So like se dua tren so child tren firebase 
    private void LikedSong(String userId,String playListId) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Liked_Path).child(playListId);
        final DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference(PlayList_Database).child(playListId);
        databaseReference.child(userId).setValue("Liked This PlayList");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                playListOnlineInfo.setLiked(String.valueOf(dataSnapshot.getChildrenCount()));
                databaseReference1.setValue(playListOnlineInfo);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void UnLikedSong(String userId,String playListId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Liked_Path).child(playListId);
        final DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference(PlayList_Database).child(playListId);
        databaseReference.child(userId).removeValue(); //remove
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                playListOnlineInfo.setLiked(String.valueOf(dataSnapshot.getChildrenCount()));
                databaseReference1.setValue(playListOnlineInfo);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void getPlayListInfo(String playListId){
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference(PlayList_Database).child(playListId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                playListOnlineInfo=dataSnapshot.getValue(PlayListOnlineInfo.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    protected void upView(String playListId){

        final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference(PlayList_Database).child(playListId);
        DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReference(View_Database).child(playListId);
        databaseReference1.push().setValue("Id of View");
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //   songInfo=dataSnapshot.getValue(SongPlayerOnlineInfo.class);
                playListOnlineInfo.setView(String.valueOf(dataSnapshot.getChildrenCount()));
                databaseReference.setValue(playListOnlineInfo);
                //ReLoadActivity();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
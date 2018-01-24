package com.nodomain.shouryaj98.pratikriyarec;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class MainActivity extends Activity {

    FirebaseDatabase database;
    DatabaseReference myRef;
    private List<task> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    RecyclerView.Adapter recyclerViewAdapter;
    RecyclerView.LayoutManager recylerViewLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recylerViewLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(recylerViewLayoutManager);
        recyclerViewAdapter = new RecyclerViewAdapter(movieList);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());



        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                task mess = dataSnapshot.getValue(task.class);
                movieList.add(mess);
                recyclerView.setAdapter(recyclerViewAdapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                task mess = dataSnapshot.getValue(task.class);
                movieList.add(mess);
                recyclerView.setAdapter(recyclerViewAdapter);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                recreate();
            }


            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void del(View v)
    {

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();


        Query applesQuery = myRef.orderByChild("active").equalTo(true);


        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });

    }

}

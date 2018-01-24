package com.nodomain.shouryaj98.pratikriyarec;


import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


import static android.content.ContentValues.TAG;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>  {

    private List<task> moviesList;


    public RecyclerViewAdapter(List<task> moviesList) {
        this.moviesList = moviesList;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener
             {
        private TextView add;
        private TextView lat;
        private TextView lon;
        private TextView accu;
        private task itemm;

        public MyViewHolder(View view) {
            super(view);

            view.setOnClickListener(this);

            add =  view.findViewById(R.id.add);
            lat =  view.findViewById(R.id.lat);
            lon =  view.findViewById(R.id.lon);
            accu =  view.findViewById(R.id.accu);

        }

        public void setItem(task item) {
            // s=item.title;
            itemm = item;
            if(itemm.active==true) {
                add.setTextColor(Color.BLUE);
                lat.setTextColor(Color.BLUE);
                lon.setTextColor(Color.BLUE);
                accu.setTextColor(Color.BLUE);
            }
            else
                {
                add.setTextColor(Color.BLACK);
                lat.setTextColor(Color.BLACK);
                lon.setTextColor(Color.BLACK);
                accu.setTextColor(Color.BLACK);
                }


                add.setText(item.add);
                lat.setText(("" + item.lat));
                lon.setText("" + item.lon);
                accu.setText("" + item.acc);
        }


            @Override
            public void onClick(View view) {

                itemm.active = itemm.active==true?false:true;
                setItem(itemm);

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                Query applesQuery = ref.orderByChild("add").equalTo(itemm.add);


                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                            appleSnapshot.getRef().removeValue();
                            appleSnapshot.getRef().setValue(itemm);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "onCancelled", databaseError.toException());
                    }
                });
           }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        task movie = moviesList.get(position);
        holder.setItem(movie);

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }




}
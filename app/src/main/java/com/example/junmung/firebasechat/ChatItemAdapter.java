package com.example.junmung.firebasechat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatItemAdapter extends RecyclerView.Adapter<ChatItemAdapter.ChatViewHolder>{
    private List<UserData> items;
    private Context context;
    private String myName;

    public ChatItemAdapter(String name, ArrayList<UserData> items) {
        myName = name;
        this.items = items;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false);
        ChatViewHolder viewHolder = new ChatViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        UserData data = items.get(position);

        // 자신이 보낸 메세지
        if(data.getName().equals(myName)){
            holder.userName.setVisibility(View.GONE);
            holder.layout_container.setGravity(Gravity.RIGHT);
            holder.message.setBackgroundResource(R.drawable.rightchatbubble);
        }
        // 다른 유저가 보낸 메세지
        else{
            holder.userName.setText(data.getName());
            holder.userName.setVisibility(View.VISIBLE);
            holder.layout_container.setGravity(Gravity.LEFT);
            holder.message.setBackgroundResource(R.drawable.leftchatbubble);
        }

        holder.message.setText(data.getMessage());
    }


    // FireBase 에서 메세지들을 받아온다.
    public void getMessages(){
        items.clear();
        notifyDataSetChanged();
        FirebaseDatabase.getInstance().getReference().child("message").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot item : dataSnapshot.getChildren()) {
                    items.add(item.getValue(UserData.class));
                }

                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    class ChatViewHolder extends RecyclerView.ViewHolder{
        public TextView userName;
        public TextView message;
        public LinearLayout layout_container;

        public ChatViewHolder(View view) {
            super(view);
            userName = view.findViewById(R.id.item_textView_name);
            message = view.findViewById(R.id.item_textView_message);
            layout_container = view.findViewById(R.id.item_linearLayout_container);
        }
    }


    @Override
    public int getItemCount() {
        return items.size();
    }
}

package com.example.devanshusapra.StudentRepublic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    private List <Notification> mDataset;

    public MyAdapter(Context context, List<Notification> Templist){
        this.mDataset = Templist;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_list_view, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder Holder, int position) {

        Notification notification = mDataset.get(position);
        Holder.msgTitle.setText(notification.getTitle());
        Holder.msgDesc.setText(notification.getMessage());
        Holder.msgTime.setText(notification.getTimestamp());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView msgTitle;
        public TextView msgDesc;
        public TextView msgTime;

        public MyViewHolder(View view) {
            super(view);

            msgTitle = view.findViewById(R.id.title);
            msgDesc = view.findViewById(R.id.description);
            msgTime = view.findViewById(R.id.timeStamp);
        }
    }
}
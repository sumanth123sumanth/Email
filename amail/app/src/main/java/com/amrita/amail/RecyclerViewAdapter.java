package com.amrita.amail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amrita.model.Message;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private ArrayList<Message> msgs=new ArrayList<>();
    private Context context;

    public RecyclerViewAdapter(ArrayList<Message> msgs, Context context) {
        this.msgs = msgs;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listmail,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.ld.setText(msgs.get(i).getDate());
        viewHolder.lf.setText(msgs.get(i).getFrom());
        viewHolder.lt.setText(msgs.get(i).getTimestamp());
        viewHolder.ls.setText(msgs.get(i).getSubject());
        viewHolder.lm.setText(msgs.get(i).getMessage());
    }

    @Override
    public int getItemCount() {
        return msgs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView lf,ld,lt,ls,lm;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lf=(TextView)itemView.findViewById(R.id.list_from);
            ld=(TextView)itemView.findViewById(R.id.list_date);
            lt=(TextView)itemView.findViewById(R.id.list_time);
            ls=(TextView)itemView.findViewById(R.id.list_sub);
            lm=(TextView)itemView.findViewById(R.id.list_msg);
        }

    }
}

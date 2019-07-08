package com.amrita.amail;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class contactList extends ArrayAdapter<String>  {
    private Activity context;
    private List<String> contactl;
        public contactList(Activity context,List<String> contactl){
            super(context,R.layout.list_layout,contactl);
           this.context=context;
           this.contactl=contactl;
            }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        final View listview=inflater.inflate(R.layout.list_layout,null,true);
        TextView name=(TextView)listview.findViewById(R.id.et_conname_list);
        String c=contactl.get(position);
        name.setText(c);
        return listview;
    }



}

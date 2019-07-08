package com.amrita.amail;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.amrita.amail.afterLogin.cuid;
import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class cont_tab extends Fragment {
    SwipeMenuListView listView;
    DatabaseReference datacon,test;
    String ct;
    ArrayList<String> cl;
    private String id;
    private Handler handler;
    private  String name_clk,phno_clk,uname_clk,uk,nk,pk;
    Activity a;
    private Intent d;
    String delcon,delid,delkey,aa;
    static boolean fromconttab=false;
    int delin;Intent w,k,h;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.cont_tab,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        listView=(SwipeMenuListView) view.findViewById(R.id.listViewId);
        datacon=FirebaseDatabase.getInstance().getReference("users").child(cuid).child("contacts");
        test=FirebaseDatabase.getInstance().getReference("users");
        ct="";
        name_clk="";phno_clk="";uname_clk="";
        d=new Intent(getActivity(),dialog.class);
        cl=new ArrayList<>();
        a=getActivity();
        h=new Intent(getActivity(),editcon.class);

    }

    @Override
    public void onStart() {
        super.onStart();

        datacon.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cl.clear();
                // cl=new ArrayList<>();
                for(DataSnapshot c:dataSnapshot.getChildren())
                {    String user=c.child("name").getValue().toString();
                    cl.add(user);
                    //Toast.makeText(getContext(),"hi"+id,Toast.LENGTH_SHORT).show();

                    //Toast.makeText(getContext(),"hi"+ct.getName(),Toast.LENGTH_SHORT).show();

                }

                contactList adapter = new contactList(getActivity(),cl);
                listView.setAdapter(adapter);
                SwipeMenuCreator creator = new SwipeMenuCreator() {

                    @Override
                    public void create(SwipeMenu menu) {
                        // create "open" item
                        SwipeMenuItem openItem = new SwipeMenuItem(
                                getContext());
                        // set item background
                        openItem.setBackground(R.color.pink);
                        // set item width
                        openItem.setWidth(100);
                        // set item title
                        openItem.setIcon(R.drawable.ic_edit_black_24dp);
                        // add to menu


                        // create "delete" item
                        SwipeMenuItem deleteItem = new SwipeMenuItem(
                                getContext());
                        // set item background
                        deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                                0x3F, 0x25)));
                        // set item width
                        deleteItem.setWidth(100);
                        // set a icon
                        deleteItem.setIcon(R.drawable.ic_delete_black_24dp);
                        // add to menu


                        SwipeMenuItem infoItem=new SwipeMenuItem(getContext());
                        infoItem.setBackground(R.color.bg_action_mode);
                        infoItem.setWidth(100);
                        infoItem.setIcon(R.drawable.ic_info_outline_black_24dp);
                        SwipeMenuItem sendItem=new SwipeMenuItem(getContext());
                        sendItem.setBackground(R.color.bluefab);
                        sendItem.setWidth(100);
                        sendItem.setIcon(R.drawable.ic_send_black_24dp);
                        menu.addMenuItem(infoItem);
                        menu.addMenuItem(sendItem);
                        menu.addMenuItem(openItem);
                        menu.addMenuItem(deleteItem);
                    }
                };

// set creator
                listView.setMenuCreator(creator);
                listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                        switch (index) {
                            case 0:
                                uname_clk=(String)cl.get(position);
                                d.putExtra("name",uname_clk);
                                datacon.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for(DataSnapshot s:dataSnapshot.getChildren())
                                        {
                                            if(s.child("name").getValue().toString().equals(uname_clk))
                                            {
                                                name_clk=s.child("username").getValue().toString();
                                                phno_clk=s.child("phno").getValue().toString();
                                                d.putExtra("uname",name_clk);
                                                //Toast.makeText(getContext(),name_clk,Toast.LENGTH_SHORT).show();
                                                d.putExtra("phno",phno_clk);
                                                startActivity(d);
                                                break;
                                            }
                                        }

                                        //Toast.makeText(getContext(),name+phno,Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                                break;
                            case 2:

                                 aa=cl.get(position);

                                datacon.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for(DataSnapshot dd:dataSnapshot.getChildren())
                                        {
                                            if(dd.child("name").getValue().toString().equals(aa))
                                            {
                                                uk=dd.child("username").getValue().toString();
                                                pk=dd.child("phno").getValue().toString();
                                              //  Toast.makeText(getContext(),uk+pk,Toast.LENGTH_SHORT).show();
                                                h.putExtra("username",uk);
                                                h.putExtra("name",aa);
                                                h.putExtra("phno",pk);
                                                startActivity(h);
                                               break;
                                            }
                                        }


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                break;
                            case 3:
                                 delcon=cl.get(position);
                                cl.remove(position);
                                datacon.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for(DataSnapshot d:dataSnapshot.getChildren())
                                        if(d.child("name").getValue().toString().equals(delcon))
                                        {
                                            delkey=d.getKey();break;
                                            //Toast.makeText(getContext(),delkey,Toast.LENGTH_SHORT).show();
                                        }
                                        datacon.child(delkey).child("uid").removeValue();
                                        datacon.child(delkey).child("username").removeValue();
                                        datacon.child(delkey).child("name").removeValue();
                                        datacon.child(delkey).child("phno").removeValue();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                contactList adapter = new contactList(getActivity(),cl);
                                listView.setAdapter(adapter);
                                 delin=position;
                                /*Snackbar s=Snackbar.make(getView().findViewById(R.id.cont_tab),"Contact deleted",Snackbar.LENGTH_INDEFINITE).setAction("UNDO", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                             test.addListenerForSingleValueEvent(new ValueEventListener() {
                                                 @Override
                                                 public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                     for(DataSnapshot a:dataSnapshot.getChildren())
                                                    if(a.child("username").getValue().toString().equals(delcon))
                                                    {   delid=a.child("uid").getValue().toString();
                                                        datacon.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                String k = datacon.push().getKey();
                                                                HashMap<String, Object> s = new HashMap<>();
                                                                s.put("username", delcon);
                                                                s.put("uid", delid);
                                                                datacon.child(k).setValue(s);
                                                                cl.add(delcon+"@amail.com");
                                                                contactList adapter = new contactList(getActivity(),cl);
                                                                listView.setAdapter(adapter);
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                            }
                                                        });
                                                    }
                                                 }

                                                 @Override
                                                 public void onCancelled(@NonNull DatabaseError databaseError) {

                                                 }
                                             });

                                    }
                                });
                                s.setActionTextColor(Color.rgb(255,147,0));
                                s.setDuration(4000);
                                s.show();*/
                                break;
                            case 1:
                                fromconttab=true;
                                 w=new Intent(getActivity(),Main2Activity.class);
                                uname_clk=(String)cl.get(position);

                                datacon.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for(DataSnapshot d:dataSnapshot.getChildren())
                                        {
                                            if(d.child("name").getValue().toString().equals(uname_clk))
                                            {
                                                name_clk=d.child("username").getValue().toString();

                                                w.putExtra("user",name_clk);
                                                startActivity(w);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                                break;
                        }
                        // false : close the menu; true : not close the menu
                        return false;
                    }
                });


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



}

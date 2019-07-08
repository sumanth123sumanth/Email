package com.amrita.amail;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amrita.adapter.MessagesAdapter;
import com.amrita.helper.DividerItemDecoration;
import com.amrita.model.Message;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.amrita.amail.afterLogin.cuid;

public class send_fragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, MessagesAdapter.MessageAdapterListener , SearchView.OnQueryTextListener {
    private List<Message> messages = new ArrayList<>();
    private RecyclerView recyclerView;
    private MessagesAdapter mAdapter;
    private SwipeRefreshLayout swipeRefreshLayout1;
    private android.support.v7.view.ActionMode actionMode;
    private ActionModeCallback actionModeCallback;
    private String f, m, s, d, t, del;
    private DatabaseReference df;
    private Intent i;
    private SearchView searchView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_send, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        df = FirebaseDatabase.getInstance().getReference("users").child(cuid).child("inbox");
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        swipeRefreshLayout1 = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout1.setOnRefreshListener(this);
        i=new Intent(this.getActivity(),Unead.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        mAdapter = new MessagesAdapter(this.getContext(), messages, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        actionModeCallback = new ActionModeCallback();
        swipeRefreshLayout1.post(
                new Runnable() {
                    @Override
                    public void run() {
                        getInbox();
                    }
                }
        );
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    if (!searchView.isIconified()) {
                        searchView.setIconified(true);
                        return true;
                    }

                    else{
                        getActivity().onBackPressed();
                    }

                }
                return false;
            }
        } );
    }

    private void getInbox()
    {
        messages.clear();
        swipeRefreshLayout1.setRefreshing(true);
        df.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot s : dataSnapshot.getChildren())
                {
                    if(s.child("isread").getValue().toString().equals("false")) {
                        Message m = new Message();
                        m.setDate(s.child("date").getValue().toString());
                        m.setFrom(s.child("from").getValue().toString());
                        m.setMessage(s.child("msg").getValue().toString());
                        m.setRead(Boolean.parseBoolean(s.child("isread").getValue().toString()));
                        m.setSubject(s.child("sub").getValue().toString());
                        m.setImportant(false);
                        m.setTimestamp(s.child("time").getValue().toString());
                        m.setColor(getRandomMaterialColor("400"));
                        messages.add(m);
                    }
                }
                Collections.sort(messages);
                mAdapter.notifyDataSetChanged();
                swipeRefreshLayout1.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Unable to fetch json: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                swipeRefreshLayout1.setRefreshing(false);
            }
        });
    }
    private int getRandomMaterialColor(String typeColor) {
        int returnColor = Color.GRAY;
        int arrayId = getResources().getIdentifier("mdcolor_" + typeColor, "array", this.getActivity().getPackageName());

        if (arrayId != 0) {
            TypedArray colors = getResources().obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.GRAY);
            colors.recycle();
        }
        return returnColor;
    }


    @Override
    public void onRefresh() {
        getInbox();
    }

    @Override
    public void onIconClicked(int position) {
        if (actionMode == null) {
            actionMode = (ActionMode) ((AppCompatActivity)getActivity()).startSupportActionMode(actionModeCallback);
        }
        toggleSelection(position);
    }

    @Override
    public void onIconImportantClicked(int position) {
        Message message = messages.get(position);
        message.setImportant(!message.isImportant());
        messages.set(position, message);
        Collections.sort(messages);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMessageRowClicked(int position) {
        if (mAdapter.getSelectedItemCount() > 0) {
            enableActionMode(position);
        } else {
            // read the message which removes bold from the row
            Message message = messages.get(position);
            message.setRead(true);
            f=message.getFrom();
            m=message.getMessage();
            s=message.getSubject();
            d=message.getDate();
            t=message.getTimestamp();
            df.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot dd:dataSnapshot.getChildren())
                    {
                        if(dd.child("from").getValue().toString().equals(f) && dd.child("time").getValue().toString().equals(t)&&dd.child("msg").getValue().toString().equals(m)&&dd.child("date").getValue().toString().equals(d)&&dd.child("sub").getValue().toString().equals(s))
                        {
                            del=dd.getKey();
                            break;
                        }
                    }
                    df.child(del).child("isread").setValue("true");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            messages.remove(message);
            Collections.sort(messages);
            mAdapter.notifyDataSetChanged();
            i.putExtra("from",f);
            i.putExtra("sub",s);
            i.putExtra("msg",m);
            i.putExtra("date",d);
            i.putExtra("time",t);
            startActivity(i);
           // getInbox();

            Toast.makeText(getContext(), "Read: " + message.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRowLongClicked(int position) {
        enableActionMode(position);
    }
    private void enableActionMode(int position) {
        if (actionMode == null) {
            actionMode = ((AppCompatActivity)getActivity()).startSupportActionMode(actionModeCallback);
        }
        toggleSelection(position);
    }
    private void toggleSelection(int position) {
        mAdapter.toggleSelection(position);
        int count = mAdapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        String in=s.toLowerCase();
        List<Message> newlist=new ArrayList<>();
        for(Message a:messages)
        {
            if(a.getFrom().contains(in))
            {
                newlist.add(a);
            }
        }
        mAdapter.updateList(newlist);
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.search,menu);
        MenuItem menuItem=menu.findItem(R.id.search);
         searchView=(SearchView)menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_action_mode, menu);

            // disable swipe refresh if action mode is enabled
            swipeRefreshLayout1.setEnabled(false);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    // delete all the selected messages
                    deleteMessages();
                    mode.finish();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mAdapter.clearSelections();
            swipeRefreshLayout1.setEnabled(true);
            actionMode = null;
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    mAdapter.resetAnimationIndex();
                    // mAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    // deleting the messages from recycler view
    private void deleteMessages() {
        mAdapter.resetAnimationIndex();
        final List<Integer> selectedItemPositions =
                mAdapter.getSelectedItems();
        for (int i = selectedItemPositions.size() - 1; i >= 0; i--) {
            f=messages.get(i).getFrom();
            m=messages.get(i).getMessage();
            s=messages.get(i).getSubject();
            d=messages.get(i).getDate();
            t=messages.get(i).getTimestamp();
            //Toast.makeText(getContext(),f+m+s+d+t,Toast.LENGTH_SHORT).show();
            df.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot dd:dataSnapshot.getChildren())
                    {
                        if(dd.child("from").getValue().toString().equals(f) && dd.child("time").getValue().toString().equals(t)&&dd.child("msg").getValue().toString().equals(m)&&dd.child("date").getValue().toString().equals(d)&&dd.child("sub").getValue().toString().equals(s))
                        {
                            del=dd.getKey();
                            // Toast.makeText(getContext(),del,Toast.LENGTH_SHORT).show();
                            break;
                        }

                    }
                    df.child(del).child("from").removeValue();
                    df.child(del).child("isread").removeValue();
                    df.child(del).child("time").removeValue();
                    df.child(del).child("date").removeValue();
                    df.child(del).child("sub").removeValue();
                    df.child(del).child("msg").removeValue();
                    df.child(del).child("to").removeValue();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            mAdapter.removeData(selectedItemPositions.get(i));
        }
        Collections.sort(messages);
        mAdapter.notifyDataSetChanged();
    }

}

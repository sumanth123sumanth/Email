package com.amrita.amail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
public class Contacts_fragment extends Fragment {
private BottomNavigationView bottomNavigationView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contacts,container,false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        bottomNavigationView=(BottomNavigationView)view.findViewById(R.id.bottom_navigation);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_contact_container,new cont_tab()).commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment f=null;
                switch(item.getItemId())
                {
                    case R.id.con_list:
                        f=new cont_tab();break;
                    case R.id.add_contacts:
                        f=new addcont_tab();break;
                }
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_contact_container,f).commit();

                return true;
            }
        });
    }

}

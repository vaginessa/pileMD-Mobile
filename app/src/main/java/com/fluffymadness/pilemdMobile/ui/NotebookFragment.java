package com.fluffymadness.pilemdMobile.ui;

import android.app.Activity;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by fluffymadness on 9/28/2016.
 */

public class NotebookFragment extends Fragment {
    private DataModel dataModel;
    private FragmentActivity myContext;
    private String rackName;

    private ListView notebookList;

    public static NotebookFragment newInstance(String rackName) {
        NotebookFragment f = new NotebookFragment();
        Bundle args = new Bundle();
        args.putString("rackName", rackName);
        f.setArguments(args);
        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notebook, container, false);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        rackName = getArguments().getString("rackName");
        String path= PreferenceManager.getDefaultSharedPreferences(myContext).getString("pref_root_directory", "");
        dataModel = new DataModel(path);
    }
    @Override
    public void onResume(){
        super.onResume();
        refreshNotebooks();
    }
    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    private void refreshNotebooks(){
        //TODO handle Exception if folder is null
        ArrayList<File> notebooks = dataModel.getRackFolders(this.rackName);
        NotebookAdapter adapter = new NotebookAdapter(myContext, notebooks);
        notebookList = (ListView) getView().findViewById(R.id.folderview);
        notebookList.setAdapter(adapter);
        notebookList.setOnItemClickListener(new NotebookItemClickListener());

    }
    private class NotebookItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

    }

    private void selectItem(int position) {

        String selectedNotebookName = notebookList.getAdapter().getItem(position).toString();

        FragmentManager fm = myContext.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        NotesFragment fragment = new NotesFragment().newInstance(rackName,selectedNotebookName);

        fragmentTransaction.addToBackStack("NotebookFragment");
        fragmentTransaction.hide(NotebookFragment.this);
        fragmentTransaction.add(android.R.id.content, fragment);
        fragmentTransaction.commit();
    }
}

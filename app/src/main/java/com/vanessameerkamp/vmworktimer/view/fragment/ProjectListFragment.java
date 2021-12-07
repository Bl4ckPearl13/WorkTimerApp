package com.vanessameerkamp.vmworktimer.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vanessameerkamp.vmworktimer.R;
import com.vanessameerkamp.vmworktimer.controller.listener_fragment.ProjectListListener;


public class ProjectListFragment extends Fragment {

    public Button btnAllProjects, btnOpenProjects, btnClosedProjects;
    public FloatingActionButton abtnAddProject;
    public ListView listViewProject;

    ProjectListListener projectListListener;

    public ProjectListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_project_list, container, false);

        btnAllProjects = view.findViewById(R.id.btnAllProjects);
        btnOpenProjects = view.findViewById(R.id.btnOpenProjects);
        btnClosedProjects = view.findViewById(R.id.btnClosedProjects);

        abtnAddProject = view.findViewById(R.id.abtnAddProject);

        listViewProject = view.findViewById(R.id.listViewProject);

        projectListListener = new ProjectListListener(this);

        btnAllProjects.setOnClickListener(projectListListener);
        btnOpenProjects.setOnClickListener(projectListListener);
        btnClosedProjects.setOnClickListener(projectListListener);

        abtnAddProject.setOnClickListener(projectListListener);

        listViewProject.setOnItemClickListener(projectListListener);

        //registerForContextMenu(listViewProject);

        return view;
    }

    /*
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        getActivity().getMenuInflater().inflate(R.menu.context_menu_project, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        return projectListListener.onContextItemSelected(item);
    }
     */

    @Override
    public void onResume() {
        super.onResume();
        projectListListener.onResume();
    }
}
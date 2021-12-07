package com.vanessameerkamp.vmworktimer.controller.listener_fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;

import com.vanessameerkamp.vmworktimer.R;
import com.vanessameerkamp.vmworktimer.controller.adapter.ProjectListAdapter;
import com.vanessameerkamp.vmworktimer.controller.database.ProjectDataSource;
import com.vanessameerkamp.vmworktimer.controller.database.WorkTimeDataSource;
import com.vanessameerkamp.vmworktimer.model.Mode;
import com.vanessameerkamp.vmworktimer.model.Project;
import com.vanessameerkamp.vmworktimer.view.activity.MainActivity;
import com.vanessameerkamp.vmworktimer.view.activity.ProjectDetailActivity;
import com.vanessameerkamp.vmworktimer.view.fragment.ProjectListFragment;

import java.util.ArrayList;
import java.util.List;

public class ProjectListListener implements View.OnClickListener, AdapterView.OnItemClickListener {

    ProjectListFragment projectListFragment;

    public List<Project> projectList = new ArrayList<>();
    public ProjectListAdapter projectListAdapter;

    ProjectDataSource projectDataSource;
    WorkTimeDataSource workTimeDataSource;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public ProjectListListener(ProjectListFragment projectListFragment) {

        this.projectListFragment = projectListFragment;

        projectDataSource = new ProjectDataSource(projectListFragment.getContext());
        workTimeDataSource = new WorkTimeDataSource(projectListFragment.getContext());

        sharedPreferences = projectListFragment.getActivity().getSharedPreferences(MainActivity.PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        setListViewAdapter();

        projectListFragment.btnAllProjects.setEnabled(false);
    }

    public void onResume() {
        refreshListView();
        projectListFragment.btnAllProjects.setEnabled(false);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.abtnAddProject:
                Intent openCreateActivity = new Intent(projectListFragment.getActivity(), ProjectDetailActivity.class);

                editor.putString("projectMode", Mode.CREATE.toString());
                editor.apply();

                projectListFragment.startActivity(openCreateActivity);
                break;
            case R.id.btnAllProjects:
                refreshListView(projectDataSource.getAllProjects());
                projectListFragment.btnAllProjects.setEnabled(false);
                projectListFragment.btnOpenProjects.setEnabled(true);
                projectListFragment.btnClosedProjects.setEnabled(true);
                break;
            case R.id.btnOpenProjects:
                refreshListView(projectDataSource.getOpenProjects());
                projectListFragment.btnAllProjects.setEnabled(true);
                projectListFragment.btnOpenProjects.setEnabled(false);
                projectListFragment.btnClosedProjects.setEnabled(true);
                break;
            case R.id.btnClosedProjects:
                refreshListView(projectDataSource.getClosedProjects());
                projectListFragment.btnAllProjects.setEnabled(true);
                projectListFragment.btnOpenProjects.setEnabled(true);
                projectListFragment.btnClosedProjects.setEnabled(false);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long l) {

        Project selectedProject = (Project) parent.getItemAtPosition(position);

        Intent openDetailActivity = new Intent(projectListFragment.getActivity(), ProjectDetailActivity.class);

        editor.putString("projectMode", Mode.SHOW.toString());
        editor.apply();

        openDetailActivity.putExtra("project", selectedProject);

        projectListFragment.startActivity(openDetailActivity);
    }

    /*
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        Project selectedProject = projectList.get(info.position);

        switch (item.getItemId()) {
            case R.id.cmEdit:
                Intent openEditActivity = new Intent(projectListFragment.getActivity(), CustomerDetailActivity.class);

                editor.putString("projectMode", Mode.EDIT.toString());
                editor.apply();

                openEditActivity.putExtra("project", selectedProject);

                projectListFragment.startActivity(openEditActivity);
                return true;
            case R.id.cmDelete:

                android.app.AlertDialog.Builder alert = new AlertDialog.Builder(projectListFragment.getContext());

                alert.setTitle(R.string.delete_dialog_header)
                        .setMessage(R.string.confirm_project_deletion)
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                            projectDataSource.deleteProject(selectedProject);
                            refreshListView();

                            List<WorkTime> workTimeList = workTimeDataSource.getProjectWorkTimes(selectedProject.getId());
                            workTimeDataSource.deleteWorkTimes(workTimeList);
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();

                return true;
        }

        return false;
    }
     */

    private void setListViewAdapter() {

        projectList = projectDataSource.getAllProjects();

        projectListAdapter = new ProjectListAdapter(projectListFragment.getContext(), projectList);

        projectListFragment.listViewProject.setAdapter(projectListAdapter);
    }

    private void refreshListView() {

        projectList.clear();

        projectList.addAll(projectDataSource.getAllProjects());

        projectListAdapter.notifyDataSetChanged();
    }

    private void refreshListView(List<Project> projects) {

        projectList.clear();

        projectList.addAll(projects);

        projectListAdapter.notifyDataSetChanged();
    }
}

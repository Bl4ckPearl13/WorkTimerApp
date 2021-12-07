package com.vanessameerkamp.vmworktimer.controller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.vanessameerkamp.vmworktimer.R;
import com.vanessameerkamp.vmworktimer.controller.database.CustomerDataSource;
import com.vanessameerkamp.vmworktimer.model.Duration;
import com.vanessameerkamp.vmworktimer.model.Project;
import com.vanessameerkamp.vmworktimer.model.State;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ProjectListAdapter extends ArrayAdapter<Project> {

    private List<Project> projectList;
    private LayoutInflater layoutInflater;
    CustomerDataSource customerDataSource;

    public ProjectListAdapter(@NonNull Context context, List<Project> projectList) {

        super(context, R.layout.row_layout_project, projectList);

        this.projectList = projectList;
        layoutInflater = LayoutInflater.from(context);
        customerDataSource = new CustomerDataSource(this.getContext());
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View rowView;
        if (convertView == null) {
            rowView = layoutInflater.inflate(R.layout.row_layout_project, parent, false);
        } else {
            rowView = convertView;
        }

        Project currentProject = projectList.get(position);

        ImageView imgvProjectState = (ImageView) rowView.findViewById(R.id.imgvProjectState);

        TextView txtvProjectName = (TextView) rowView.findViewById(R.id.txtvProjectName);
        TextView txtvProjectStartDate = (TextView) rowView.findViewById(R.id.txtvProjectStartDate);
        TextView txtvProjectCustomer = (TextView) rowView.findViewById(R.id.txtvProjectCustomer);
        TextView txtvProjectDuration = (TextView) rowView.findViewById(R.id.txtvProjectDuration);

        if (currentProject.getState() == State.open.ordinal()) {
            imgvProjectState.setImageResource(R.drawable.state_open);
        } else if (currentProject.getState() == State.inProgress.ordinal()) {
            imgvProjectState.setImageResource(R.drawable.state_in_progress);
        } else if (currentProject.getState() == State.closed.ordinal()){
            imgvProjectState.setImageResource(R.drawable.state_closed);
        }

        txtvProjectName.setText(currentProject.getName());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        if (currentProject.getStart() != null) {
            txtvProjectStartDate.setText(currentProject.getStart().format(formatter));
        } else {
            txtvProjectStartDate.setText(R.string.msg_not_started_yet);
        }
        txtvProjectCustomer.setText((customerDataSource.getCustomerById(currentProject.getCustomerId())).getName());
        txtvProjectDuration.setText(new Duration(currentProject.getDuration()).toString());

        return rowView;
    }
}

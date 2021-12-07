package com.vanessameerkamp.vmworktimer.controller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.vanessameerkamp.vmworktimer.R;
import com.vanessameerkamp.vmworktimer.controller.database.ProjectDataSource;
import com.vanessameerkamp.vmworktimer.model.Duration;
import com.vanessameerkamp.vmworktimer.model.WorkTime;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class WorkTimeListAdapter extends ArrayAdapter {

    private List<WorkTime> workTimeList;
    private LayoutInflater layoutInflater;

    ProjectDataSource projectDataSource;

    public WorkTimeListAdapter(@NonNull Context context, List<WorkTime> workTimeList) {
        super(context, R.layout.row_layout_worktime, workTimeList);

        projectDataSource = new ProjectDataSource(this.getContext());

        this.workTimeList = workTimeList;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View rowView;
        if (convertView == null) {
            rowView = layoutInflater.inflate(R.layout.row_layout_worktime, parent, false);
        } else {
            rowView = convertView;
        }

        WorkTime currentWorkTime = workTimeList.get(position);

        TextView txtvWorkTimeProject = (TextView) rowView.findViewById(R.id.txtvWorkTimeProject);
        TextView txtvWorkTimeStartDate = (TextView) rowView.findViewById(R.id.txtvWorkTimeStartDate);
        TextView txtvWorkTimeDescription = (TextView) rowView.findViewById(R.id.txtvWorkTimeDescription);
        TextView txtvWorkTimeDuration = (TextView) rowView.findViewById(R.id.txtvWorkTimeDuration);

        int projectId = currentWorkTime.getProjectId();
        txtvWorkTimeProject.setText(projectDataSource.getProjectById(projectId).getName());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        txtvWorkTimeStartDate.setText(currentWorkTime.getStart().format(formatter));

        if (currentWorkTime.getDescription().equals("") || currentWorkTime.getDescription().isEmpty()) {

            txtvWorkTimeDescription.setText(R.string.msg_no_work_time_description);
        } else {
            txtvWorkTimeDescription.setText(String.valueOf(currentWorkTime.getDescription()));
        }

        txtvWorkTimeDuration.setText(new Duration(currentWorkTime.getDuration()).toString());

        return rowView;
    }
}

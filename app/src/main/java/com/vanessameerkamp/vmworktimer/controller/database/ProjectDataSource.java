package com.vanessameerkamp.vmworktimer.controller.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.vanessameerkamp.vmworktimer.controller.helper.SQL;
import com.vanessameerkamp.vmworktimer.model.Project;
import com.vanessameerkamp.vmworktimer.model.State;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProjectDataSource extends DataSource {

    //region Konstanten
    private String[] columns = {
            SQL.COLUMN_PROJECT_ID,
            SQL.COLUMN_PROJECT_CUSTOMER_ID,
            SQL.COLUMN_PROJECT_NAME,
            SQL.COLUMN_PROJECT_DESCRIPTION,
            SQL.COLUMN_PROJECT_START,
            SQL.COLUMN_PROJECT_END,
            SQL.COLUMN_PROJECT_DURATION,
            SQL.COLUMN_PROJECT_STATE
    };
    //endregion

    //region Konstruktor
    public ProjectDataSource(Context context) {
        super(context);
    }
    //endregion

    //region CREATE
    public Project insertProject(Project project) {

        ContentValues values = new ContentValues();
        values.put(SQL.COLUMN_PROJECT_CUSTOMER_ID, project.getCustomerId());
        values.put(SQL.COLUMN_PROJECT_NAME, project.getName());
        values.put(SQL.COLUMN_PROJECT_DESCRIPTION, project.getDescription());
        values.put(SQL.COLUMN_PROJECT_DURATION, project.getDuration());
        values.put(SQL.COLUMN_PROJECT_STATE, project.getState());

        openWritable();

        long id = database.insert(SQL.TABLE_PROJECT, null, values);
        Cursor cursor = database.query(SQL.TABLE_PROJECT, columns, SQL.COLUMN_PROJECT_ID + " = " + id,
                null, null, null, null);

        cursor.moveToFirst();
        Project currentProject = cursorToProject(cursor);

        cursor.close();
        closeDb();

        return currentProject;
    }
    //endregion

    //region READ all
    public List<Project> getAllProjects() {

        List<Project> projectList = new ArrayList<>();

        openReadable();

        Cursor cursor = database.rawQuery(SQL.GET_ALL_PROJECTS, null);

        projectList = cursorToProjectList(cursor);

        cursor.close();
        closeDb();

        return projectList;
    }
    //endregion

    //region READ by ID
    public Project getProjectById(int id) {

        Project project = new Project();

        openReadable();

        Cursor cursor = database.rawQuery(SQL.GET_PROJECT_BY_ID, new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {
            project = cursorToProject(cursor);
        }

        cursor.close();
        closeDb();

        return project;
    }
    //endregion

    //region READ by status
    public List<Project> getOpenProjects() {

        List<Project> projectList = new ArrayList<>();

        openReadable();

        Cursor cursor = database.rawQuery(SQL.GET_PROJECT_WHERE_STATE_IN, new String[]{
                String.valueOf(State.open.ordinal()),
                String.valueOf(State.inProgress.ordinal())});

        projectList = cursorToProjectList(cursor);

        cursor.close();
        closeDb();

        return projectList;
    }

    public List<Project> getInProgressProjects() {

        List<Project> projectList = new ArrayList<>();

        openReadable();

        Cursor cursor = database.rawQuery(SQL.GET_PROJECT_BY_STATE, new String[]{String.valueOf(State.inProgress.ordinal())});

        projectList = cursorToProjectList(cursor);

        cursor.close();
        closeDb();

        return projectList;
    }

    public List<Project> getClosedProjects() {

        List<Project> projectList = new ArrayList<>();

        openReadable();

        Cursor cursor = database.rawQuery(SQL.GET_PROJECT_BY_STATE, new String[]{String.valueOf(State.closed.ordinal())});

        projectList = cursorToProjectList(cursor);

        cursor.close();
        closeDb();

        return projectList;
    }
    //endregion

    //region READ for Customer
    public List<Project> getAllCustomerProjects(int customerId) {

        List<Project> projectList = new ArrayList<>();

        openReadable();

        Cursor cursor = database.rawQuery(SQL.GET_CUSTOMER_PROJECTS, new String[]{String.valueOf(customerId)});

        projectList = cursorToProjectList(cursor);

        cursor.close();
        closeDb();

        return projectList;
    }

    public List<Project> getOpenCustomerProjects(int customerId) {

        List<Project> projectList = new ArrayList<>();

        openReadable();

        Cursor cursor = database.rawQuery(SQL.GET_CUSTOMER_PROJECT_WHERE_STATE_IN, new String[]{
                String.valueOf(customerId),
                String.valueOf(State.open.ordinal()),
                String.valueOf(State.inProgress.ordinal())});

        projectList = cursorToProjectList(cursor);

        cursor.close();
        closeDb();

        return projectList;
    }

    public List<Project> getClosedCustomerProjects(int customerId) {

        List<Project> projectList = new ArrayList<>();

        openReadable();

        Cursor cursor = database.rawQuery(SQL.GET_CUSTOMER_PROJECT_BY_STATE, new String[]{
                String.valueOf(customerId),
                String.valueOf(State.closed.ordinal())});

        projectList = cursorToProjectList(cursor);

        cursor.close();
        closeDb();

        return projectList;
    }
    //endregion

    //region UPDATE
    public void updateProject(Project project) {

        ContentValues values = new ContentValues();
        values.put(SQL.COLUMN_PROJECT_CUSTOMER_ID, project.getCustomerId());
        values.put(SQL.COLUMN_PROJECT_NAME, project.getName());
        values.put(SQL.COLUMN_PROJECT_DESCRIPTION, project.getDescription());
        values.put(SQL.COLUMN_PROJECT_DURATION, project.getDuration());
        values.put(SQL.COLUMN_PROJECT_STATE, project.getState());

        if (project.getStart() != null) {
            values.put(SQL.COLUMN_PROJECT_START, project.getStart().toString());
        }

        if (project.getEnd() != null) {
            values.put(SQL.COLUMN_PROJECT_END, project.getEnd().toString());
        }

        openWritable();

        database.update(SQL.TABLE_PROJECT, values, SQL.COLUMN_PROJECT_ID + " = " + project.getId(), null);

        closeDb();
    }
    //endregion

    //region DELETE by ID
    public void deleteProject(Project project) {

        openWritable();

        database.delete(SQL.TABLE_PROJECT, SQL.COLUMN_PROJECT_ID + " = " + project.getId(), null);

        closeDb();
    }
    //endregion

    //region DELETE List of Projects
    public void deleteProjects(List<Project> projectList) {

        openWritable();

        for (Project project : projectList) {
            database.delete(SQL.TABLE_PROJECT, SQL.COLUMN_PROJECT_ID + " = " + project.getId(), null);
        }

        closeDb();
    }
    //endregion

    //region Hilfsmethoden
    private Project cursorToProject(Cursor cursor) {

        int idIndex = cursor.getColumnIndex(SQL.COLUMN_PROJECT_ID);
        int idCustomerId = cursor.getColumnIndex(SQL.COLUMN_PROJECT_CUSTOMER_ID);
        int idName = cursor.getColumnIndex(SQL.COLUMN_PROJECT_NAME);
        int idDescription = cursor.getColumnIndex(SQL.COLUMN_PROJECT_DESCRIPTION);
        int idStart = cursor.getColumnIndex(SQL.COLUMN_PROJECT_START);
        int idEnd = cursor.getColumnIndex(SQL.COLUMN_PROJECT_END);
        int idDuration = cursor.getColumnIndex(SQL.COLUMN_PROJECT_DURATION);
        int idState = cursor.getColumnIndex(SQL.COLUMN_PROJECT_STATE);

        int id = cursor.getInt(idIndex);
        int customerId = cursor.getInt(idCustomerId);
        String name = cursor.getString(idName);
        String description = cursor.getString(idDescription);
        String startAsString = cursor.getString(idStart);
        String endAsString = cursor.getString(idEnd);
        int duration = cursor.getInt(idDuration);
        int state = cursor.getInt(idState);

        LocalDateTime start = null;
        LocalDateTime end = null;

        if (startAsString != null) {
            start = LocalDateTime.parse(startAsString);
        }
        if (endAsString != null) {
            end = LocalDateTime.parse(endAsString);
        }

        return new Project(id, customerId, name, description, start, end, duration, state);
    }

    private List<Project> cursorToProjectList(Cursor cursor) {

        List<Project> projectList = new ArrayList<>();

        if (cursor.moveToFirst()) {

            do {
                projectList.add(cursorToProject(cursor));
            } while (cursor.moveToNext());
        }

        Collections.reverse(projectList);

        return projectList;
    }
    //endregion
}


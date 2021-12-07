package com.vanessameerkamp.vmworktimer.controller.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.vanessameerkamp.vmworktimer.controller.helper.SQL;
import com.vanessameerkamp.vmworktimer.model.WorkTime;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WorkTimeDataSource extends DataSource {

    //region Konstanten
    private String[] columns = {
            SQL.COLUMN_WORK_TIME_ID,
            SQL.COLUMN_WORK_TIME_PROJECT_ID,
            SQL.COLUMN_WORK_TIME_DESCRIPTION,
            SQL.COLUMN_WORK_TIME_START,
            SQL.COLUMN_WORK_TIME_END,
            SQL.COLUMN_WORK_TIME_DURATION
    };
    //endregion

    //region Konstruktor
    public WorkTimeDataSource(Context context) {
        super(context);
    }
    //endregion

    //region CREATE
    public WorkTime insertWorkTime(WorkTime workTime) {

        ContentValues values = new ContentValues();
        values.put(SQL.COLUMN_WORK_TIME_PROJECT_ID, workTime.getProjectId());
        values.put(SQL.COLUMN_WORK_TIME_DESCRIPTION, workTime.getDescription());
        values.put(SQL.COLUMN_WORK_TIME_START, workTime.getStart().toString());
        if (workTime.getEnd() != null) {
            values.put(SQL.COLUMN_WORK_TIME_END, workTime.getEnd().toString());
        }
        values.put(SQL.COLUMN_WORK_TIME_DURATION, workTime.getDuration());

        openWritable();

        long id = database.insert(SQL.TABLE_WORK_TIME, null, values);
        Cursor cursor = database.query(SQL.TABLE_WORK_TIME, columns, SQL.COLUMN_WORK_TIME_ID + " = " + id,
                null, null, null, null);

        cursor.moveToFirst();
        WorkTime currentWorkTime = cursorToWorkTime(cursor);

        cursor.close();
        closeDb();

        return currentWorkTime;
    }
    //endregion

    //region READ all
    public List<WorkTime> getAllWorkTimes() {

        List<WorkTime> workTimeList = new ArrayList<>();

        openReadable();

        Cursor cursor = database.rawQuery(SQL.GET_ALL_WORK_TIMES, null);

        workTimeList = cursorToWorkTimeList(cursor);

        cursor.close();
        closeDb();

        return workTimeList;
    }
    //endregion

    //region READ by ID
    public WorkTime getWorkTimeById(int workTimeId) {

        WorkTime workTime = new WorkTime();

        openReadable();

        Cursor cursor = database.rawQuery(SQL.GET_WORK_TIME_BY_ID, new String[]{String.valueOf(workTimeId)});

        if (cursor.moveToFirst()) {
            workTime = cursorToWorkTime(cursor);
        }

        cursor.close();
        closeDb();

        return workTime;
    }
    //endregion

    //region READ for Project
    public List<WorkTime> getProjectWorkTimes(int projectId) {

        List<WorkTime> workTimeList = new ArrayList<>();

        openReadable();

        Cursor cursor = database.rawQuery(SQL.GET_WORK_TIMES_BY_PROJECT_ID, new String[]{String.valueOf(projectId)});

        workTimeList = cursorToWorkTimeList(cursor);

        cursor.close();
        closeDb();

        return workTimeList;
    }
    //endregion

    //region UPDATE
    public void updateWorkTime(WorkTime workTime) {

        ContentValues values = new ContentValues();
        values.put(SQL.COLUMN_WORK_TIME_PROJECT_ID, workTime.getProjectId());
        values.put(SQL.COLUMN_WORK_TIME_DESCRIPTION, workTime.getDescription());
        values.put(SQL.COLUMN_WORK_TIME_START, workTime.getStart().toString());
        if (workTime.getEnd() != null) {
            values.put(SQL.COLUMN_WORK_TIME_END, workTime.getEnd().toString());
        }
        values.put(SQL.COLUMN_WORK_TIME_DURATION, workTime.getDuration());

        openWritable();

        database.update(SQL.TABLE_WORK_TIME, values, SQL.COLUMN_WORK_TIME_ID + " = " + workTime.getId(), null);

        closeDb();
    }
    //endregion

    //region DELETE by ID
    public void deleteWorkTime(WorkTime workTime) {

        openWritable();

        database.delete(SQL.TABLE_WORK_TIME, SQL.COLUMN_WORK_TIME_ID + " = " + workTime.getId(), null);

        closeDb();
    }
    //endregion

    //region DELETE List of WorkTimes
    public void deleteWorkTimes(List<WorkTime> workTimes) {

        openWritable();

        for (WorkTime workTime : workTimes) {
            database.delete(SQL.TABLE_WORK_TIME, SQL.COLUMN_WORK_TIME_ID + " = " + workTime.getId(), null);
        }

        closeDb();
    }
    //endregion

    //region Hilfsmethoden
    private WorkTime cursorToWorkTime(Cursor cursor) {

        int idIndex = cursor.getColumnIndex(SQL.COLUMN_WORK_TIME_ID);
        int idWorkTimeId = cursor.getColumnIndex(SQL.COLUMN_WORK_TIME_PROJECT_ID);
        int idDescription = cursor.getColumnIndex(SQL.COLUMN_WORK_TIME_DESCRIPTION);
        int idStart = cursor.getColumnIndex(SQL.COLUMN_WORK_TIME_START);
        int idEnd = cursor.getColumnIndex(SQL.COLUMN_WORK_TIME_END);
        int idDuration = cursor.getColumnIndex(SQL.COLUMN_WORK_TIME_DURATION);

        int id = cursor.getInt(idIndex);
        int workTimeId = cursor.getInt(idWorkTimeId);
        String description = cursor.getString(idDescription);
        String startAsString = cursor.getString(idStart);
        String endAsString = cursor.getString(idEnd);
        int duration = cursor.getInt(idDuration);

        LocalDateTime start = LocalDateTime.parse(startAsString);
        LocalDateTime end = null;
        if (endAsString != null) {
            end = LocalDateTime.parse(endAsString);
        }

        return new WorkTime(id, workTimeId, description, start, end, duration);
    }

    private List<WorkTime> cursorToWorkTimeList(Cursor cursor) {

        List<WorkTime> workTimeList = new ArrayList<>();

        if (cursor.moveToFirst()) {

            do {
                workTimeList.add(cursorToWorkTime(cursor));
            } while (cursor.moveToNext());
        }

        Collections.reverse(workTimeList);

        return workTimeList;
    }
    //endregion
}

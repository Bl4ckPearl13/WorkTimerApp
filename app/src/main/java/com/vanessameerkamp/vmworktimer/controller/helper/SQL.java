package com.vanessameerkamp.vmworktimer.controller.helper;

import com.vanessameerkamp.vmworktimer.controller.database.DbHelper;

public class SQL{

    public static final String TABLE_CUSTOMER = "customer";
    public static final String TABLE_PROJECT = "project";
    public static final String TABLE_WORK_TIME = "work_time";

    public static final String COLUMN_CUSTOMER_ID = "_id";
    public static final String COLUMN_CUSTOMER_NAME = "name";
    public static final String COLUMN_CUSTOMER_ADDRESS = "address";
    public static final String COLUMN_CUSTOMER_PHONE = "phone";
    public static final String COLUMN_CUSTOMER_EMAIL = "email";
    public static final String COLUMN_CUSTOMER_CONTACT = "contact";

    public static final String COLUMN_PROJECT_ID = "_id";
    public static final String COLUMN_PROJECT_CUSTOMER_ID = "customer_id";
    public static final String COLUMN_PROJECT_NAME = "name";
    public static final String COLUMN_PROJECT_DESCRIPTION = "description";
    public static final String COLUMN_PROJECT_START = "start";
    public static final String COLUMN_PROJECT_END = "end";
    public static final String COLUMN_PROJECT_DURATION = "duration";
    public static final String COLUMN_PROJECT_STATE = "state";

    public static final String COLUMN_WORK_TIME_ID = "_id";
    public static final String COLUMN_WORK_TIME_PROJECT_ID = "project_id";
    public static final String COLUMN_WORK_TIME_START = "start";
    public static final String COLUMN_WORK_TIME_END = "end";
    public static final String COLUMN_WORK_TIME_DURATION = "duration";
    public static final String COLUMN_WORK_TIME_DESCRIPTION = "description";

    public static final String CREATE_TABLE_CUSTOMER = "CREATE TABLE " + TABLE_CUSTOMER +
            " (" + COLUMN_CUSTOMER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_CUSTOMER_NAME + " TEXT NOT NULL, " +
            COLUMN_CUSTOMER_ADDRESS + " TEXT, " +
            COLUMN_CUSTOMER_PHONE + " TEXT, " +
            COLUMN_CUSTOMER_EMAIL + " TEXT, " +
            COLUMN_CUSTOMER_CONTACT + " TEXT);";

    public static final String CREATE_TABLE_PROJECT = "CREATE TABLE " + TABLE_PROJECT +
            " (" + COLUMN_PROJECT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_PROJECT_CUSTOMER_ID + " INTEGER NOT NULL, " +
            COLUMN_PROJECT_NAME + " TEXT NOT NULL, " +
            COLUMN_PROJECT_DESCRIPTION + " TEXT, " +
            COLUMN_PROJECT_START + " TEXT, " +
            COLUMN_PROJECT_END + " TEXT, " +
            COLUMN_PROJECT_DURATION + " INTEGER, " +
            COLUMN_PROJECT_STATE + " INTEGER);";

    public static final String CREATE_TABLE_WORK_TIME = "CREATE TABLE " + TABLE_WORK_TIME +
            " (" + COLUMN_WORK_TIME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_WORK_TIME_PROJECT_ID + " INTEGER NOT NULL, " +
            COLUMN_WORK_TIME_DESCRIPTION + " TEXT, " +
            COLUMN_WORK_TIME_START + " TEXT NOT NULL, " +
            COLUMN_WORK_TIME_END + " TEXT, " +
            COLUMN_WORK_TIME_DURATION + " INTEGER);";

    public static final String DROP_DB = "DROP DATABASE IF EXISTS " + DbHelper.DB_NAME;

    public static final String GET_ALL_CUSTOMERS = "SELECT * FROM " + TABLE_CUSTOMER;
    public static final String GET_ALL_PROJECTS = "SELECT * FROM " + TABLE_PROJECT;
    public static final String GET_ALL_WORK_TIMES = "SELECT * FROM " + TABLE_WORK_TIME;

    public static final String GET_CUSTOMER_BY_ID = GET_ALL_CUSTOMERS + " WHERE " + COLUMN_CUSTOMER_ID + " = ?";
    public static final String GET_PROJECT_BY_ID = GET_ALL_PROJECTS + " WHERE " + COLUMN_PROJECT_ID + " = ?";
    public static final String GET_WORK_TIME_BY_ID = GET_ALL_WORK_TIMES + " WHERE " + COLUMN_WORK_TIME_ID + " = ?";

    public static final String GET_PROJECT_BY_STATE = GET_ALL_PROJECTS + " WHERE " + COLUMN_PROJECT_STATE + " = ?";
    public static final String GET_PROJECT_WHERE_STATE_IN = GET_ALL_PROJECTS + " WHERE " + COLUMN_PROJECT_STATE + " IN (?, ?)";

    public static final String GET_CUSTOMER_PROJECTS = GET_ALL_PROJECTS + " WHERE " + COLUMN_PROJECT_CUSTOMER_ID + " = ?";
    public static final String GET_CUSTOMER_PROJECT_BY_STATE = GET_ALL_PROJECTS + " WHERE " + COLUMN_PROJECT_CUSTOMER_ID + " = ? AND " + COLUMN_PROJECT_STATE + " = ?";
    public static final String GET_CUSTOMER_PROJECT_WHERE_STATE_IN = GET_ALL_PROJECTS + " WHERE " + COLUMN_PROJECT_CUSTOMER_ID + " = ? AND " + COLUMN_PROJECT_STATE + " IN (?, ?)";

    public static final String GET_WORK_TIMES_BY_PROJECT_ID = GET_ALL_WORK_TIMES + " WHERE " + COLUMN_WORK_TIME_PROJECT_ID + " = ?";

    private SQL() {

    }
}

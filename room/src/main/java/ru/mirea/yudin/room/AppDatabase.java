package ru.mirea.yudin.room;

import androidx.room.*;

@Database(entities = {Employee.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract EmployeeDao employeeDao();
}


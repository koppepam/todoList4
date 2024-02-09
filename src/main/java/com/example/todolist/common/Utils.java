package com.example.todolist.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;

public class Utils {
    public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public static Date str2date(String s) {
        long ms = 0;
        try {
            ms = sdf.parse(s).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date(ms);
    }
}

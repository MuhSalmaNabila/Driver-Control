package com.ditrol.tugasakhir.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ADIK on 10/06/2016.
 */
public class FormatDate {

    //fungsi untuk mengkonversi tanggal menjadi hari
    public static String getDay(Date date){
        String result;
        SimpleDateFormat targetFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        result = targetFormat.format(date);
        return result;
    }

    // fungsi untuk mengkonvesi tanggal menjadi hari dan tanggalnya
    public static String getDayDate(Date date){
        String result;
        SimpleDateFormat targetFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault());
        result = targetFormat.format(date);
        return result;
    }

    // fungsi untuk mengkonvesi tanggal menjadi hari dan tanggalnya
    public static String getDate(Date date){
        String result;
        SimpleDateFormat targetFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        result = targetFormat.format(date);
        return result;
    }

    //fungsi menggubah string ke tanggal
    public static Date convertStringToDate(String date){
        SimpleDateFormat originFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date convertedDate = new Date();
        try {
            convertedDate = originFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedDate;
    }

    //fungsi untuk membandingkan dua tanggal
    public static boolean CompareTwoDates(Date date1, Date date2){
        boolean result=false;

        if (date1.compareTo(date2)== 0)
        {
            result=true;
        }
        return result;
    }

    // fungsi untuk mendapatkan tanggal 7 hari terakhir
    public static Date getDayWeek(int day){
        if(day==0){
            day=6;
        }else
        if(day==1){
            day=5;
        }else
        if(day==2){
            day=4;
        }else
        if(day==3){
            day=3;
        }else
        if(day==4){
            day=2;
        }else
        if(day==5){
            day=1;
        }else
        if(day==6){
            day=0;
        }
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, - day);
        date = calendar.getTime();

        return date;

    }
}


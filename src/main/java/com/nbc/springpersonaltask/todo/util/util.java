package com.nbc.springpersonaltask.todo.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class util {

  public static String currentTime() {
    Date today = new Date();
    Locale currentLocale = new Locale("KOREAN", "KOREA");
    String pattern = "yyyy-MM-dd HH:mm:ss";
    SimpleDateFormat formatter = new SimpleDateFormat(pattern, currentLocale);
    return formatter.format(today);
  }
}

package com.nbc.springpersonaltask.todo.util;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import lombok.SneakyThrows;

public class util {
  private static String salt = "O5vHR8wGx1D1GvWpKnG3Vw==";

  public static String currentTime() {
    Date today = new Date();
    Locale currentLocale = new Locale("KOREAN", "KOREA");
    String pattern = "yyyy-MM-dd HH:mm:ss";
    SimpleDateFormat formatter = new SimpleDateFormat(pattern, currentLocale);
    return formatter.format(today);
  }
  @SneakyThrows
  public static String encrypt(String pwd){
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    md.update((pwd+salt).getBytes());
    byte[] hashPwd = md.digest();

    StringBuffer sb = new StringBuffer();
    for(byte b : hashPwd) {
      String hexString = java.lang.String.format("%02x", b);
      sb.append(hexString);
    }
    return sb.toString();
  }
}

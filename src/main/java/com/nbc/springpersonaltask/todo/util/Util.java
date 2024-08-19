package com.nbc.springpersonaltask.todo.util;

import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.SneakyThrows;

public class Util {

  private static String salt = "O5vHR8wGx1D1GvWpKnG3Vw==";

  public static String currentTime() {
    LocalDateTime timeNow = LocalDateTime.now();
    String regTime = timeNow.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    return regTime;
  }

  @SneakyThrows
  public static String encrypt(String pwd) {
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    md.update((pwd + salt).getBytes());
    byte[] hashPwd = md.digest();

    StringBuffer sb = new StringBuffer();
    for (byte b : hashPwd) {
      String hexString = java.lang.String.format("%02x", b);
      sb.append(hexString);
    }
    return sb.toString();
  }
}

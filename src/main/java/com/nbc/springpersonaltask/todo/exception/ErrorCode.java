package com.nbc.springpersonaltask.todo.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
  String name();
  HttpStatus getHttpStatus();
  int getCode();
  String getMessage();

}

package com.nbc.springpersonaltask.todo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

  INCORRECT_PASSWORD(HttpStatus.UNAUTHORIZED,"비밀번호가 틀렸습니다.");

  private HttpStatus httpStatus;
  private String message;

}

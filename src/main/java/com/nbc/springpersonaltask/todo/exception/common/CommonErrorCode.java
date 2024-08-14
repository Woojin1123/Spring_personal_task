package com.nbc.springpersonaltask.todo.exception.common;

import com.nbc.springpersonaltask.todo.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {
  INVALID_ARGUMENT_ERROR(HttpStatus.BAD_REQUEST,400,"올바르지 않은 파라미터입니다.");
  private final HttpStatus httpStatus;
  private final int code;
  private final String message;
}

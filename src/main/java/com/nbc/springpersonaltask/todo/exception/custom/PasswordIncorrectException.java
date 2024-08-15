package com.nbc.springpersonaltask.todo.exception.custom;

import com.nbc.springpersonaltask.todo.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PasswordIncorrectException extends RuntimeException {

  private final ErrorCode errorCode;
}

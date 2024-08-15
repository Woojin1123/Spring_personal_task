package com.nbc.springpersonaltask.todo.exception;

import com.nbc.springpersonaltask.todo.exception.custom.PasswordIncorrectException;
import java.util.InputMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;


@RestControllerAdvice
public class ExceptionHandlerAdvice {
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity handleException(IllegalArgumentException e){
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 정보로 조회를 시도하셨습니다.");
  }

  @ExceptionHandler(PasswordIncorrectException.class)
  public ResponseEntity handlePasswordIncorrectException(PasswordIncorrectException e) {
    return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(e.getErrorCode().getMessage());
  }
  @ExceptionHandler(NullPointerException.class)
  public ResponseEntity handleNullPointerException(NullPointerException e){
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
  }
  @ExceptionHandler(InputMismatchException.class)
  public ResponseEntity handleInputMismatchException(InputMismatchException e){
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
  }
  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity handleNoHandlerFoundException(NoHandlerFoundException e){
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("잘못된 조회 경로입니다.");
  }
}

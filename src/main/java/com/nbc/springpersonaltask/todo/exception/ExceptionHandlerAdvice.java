package com.nbc.springpersonaltask.todo.exception;

import com.nbc.springpersonaltask.todo.exception.custom.ManagerAlreadyExistException;
import com.nbc.springpersonaltask.todo.exception.custom.PasswordIncorrectException;
import java.security.NoSuchAlgorithmException;
import java.util.InputMismatchException;
import java.util.stream.Collectors;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;


@RestControllerAdvice
public class ExceptionHandlerAdvice {

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity handleException(IllegalArgumentException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body("잘못된 정보로 조회를 시도하셨습니다.");
  }

  @ExceptionHandler(PasswordIncorrectException.class)
  public ResponseEntity handlePasswordIncorrectException(PasswordIncorrectException e) {
    return ResponseEntity.status(e.getErrorCode()
            .getHttpStatus())
        .body(e.getErrorCode()
            .getMessage());
  }

  @ExceptionHandler(NullPointerException.class)
  public ResponseEntity handleNullPointerException(NullPointerException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(e.getMessage());
  }

  @ExceptionHandler(InputMismatchException.class)
  public ResponseEntity handleInputMismatchException(InputMismatchException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(e.getMessage());
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity handleNoHandlerFoundException(NoHandlerFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body("잘못된 조회 경로입니다.");
  }

  @ExceptionHandler(EmptyResultDataAccessException.class)
  public ResponseEntity handleEmptyResultDataAccessException(EmptyResultDataAccessException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body("해당 id를 가진 매니저가 없습니다.");
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    String message = e.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(x -> x.getDefaultMessage())
        .collect(Collectors.joining(""));
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(message);
  }

  @ExceptionHandler(ManagerAlreadyExistException.class)
  public ResponseEntity handleManagerAlreadyExistException(ManagerAlreadyExistException e) {
    return ResponseEntity.status(e.getErrorCode()
            .getHttpStatus())
        .body(e.getErrorCode()
            .getMessage());
  }

  @ExceptionHandler(NoSuchAlgorithmException.class)
  public ResponseEntity handleNoSuchAlgorithmException(NoSuchAlgorithmException e){
    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("암호화 사용 불가능");
  }
}

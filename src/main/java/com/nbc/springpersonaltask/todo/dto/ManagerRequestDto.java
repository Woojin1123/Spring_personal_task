package com.nbc.springpersonaltask.todo.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class ManagerRequestDto {

  private String name;
  @Pattern(regexp = "^[a-zA-Z0-9+-_.]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+$", message = "이메일 형식이 다릅니다.")
  private String email;
  private String registerDate;
  private String updateDate;
}

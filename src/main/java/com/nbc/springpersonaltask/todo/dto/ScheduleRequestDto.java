package com.nbc.springpersonaltask.todo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ScheduleRequestDto {

  @NotBlank(message = "todo 입력이 없습니다.")
  @Size(max = 200, message = "200자 이내로 입력해주세요.")
  private String todo;
  private int managerId;
  @Size(min = 8, max = 64, message = "비밀번호는 8자이상 64자 이하로 입력해주세요")
  private String pwd;
  private String registerDate;
  private String updateDate;
}

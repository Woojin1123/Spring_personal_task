package com.nbc.springpersonaltask.todo.service;

import com.nbc.springpersonaltask.todo.dto.ScheduleRequestDto;
import com.nbc.springpersonaltask.todo.dto.ScheduleResponseDto;
import com.nbc.springpersonaltask.todo.entity.Schedule;
import com.nbc.springpersonaltask.todo.exception.ErrorCode;
import com.nbc.springpersonaltask.todo.exception.custom.PasswordIncorrectException;
import com.nbc.springpersonaltask.todo.repository.ScheduleRepository;
import com.nbc.springpersonaltask.todo.util.util;
import java.util.InputMismatchException;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;

public class ScheduleService { // 서비스에서 repository를 이용해 기능 수행

  private final JdbcTemplate jdbcTemplate;
  ScheduleRepository scheduleRepository;

  public ScheduleService(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
    this.scheduleRepository = new ScheduleRepository(jdbcTemplate);
  }

  public ScheduleResponseDto createSchedule(ScheduleRequestDto requestDto) {
    if (!scheduleRepository.managerExists(requestDto.getManagerId())) {
      throw new NullPointerException("해당 매니저가 존재하지 않습니다.");
    }
    if (requestDto.getPwd()
        .length() > 64) {
      throw new InputMismatchException("비밀번호가 너무 깁니다.");
    } else if (requestDto.getPwd()
        .length() < 8) {
      throw new InputMismatchException("비밀번호가 너무 짧습니다.");
    } else {
      String time = util.currentTime();
      Schedule schedule = new Schedule(requestDto);
      schedule.setPwd(util.encrypt(requestDto.getPwd()));
      schedule.setUpdateDate(time);
      schedule.setRegisterDate(time);
      scheduleRepository.save(schedule);
      ScheduleResponseDto responseDto = new ScheduleResponseDto(schedule);
      scheduleRepository.setManagerName(responseDto, responseDto.getManagerId());
      return responseDto;
    }
  }

  public List<ScheduleResponseDto> getSchedules(String managerName, String updateDate, int page,
      int pagesize) {
    return scheduleRepository.findAll(managerName, updateDate, page, pagesize);
  }

  public ScheduleResponseDto choiceSchedule(int id) {
    Schedule schedule = scheduleRepository.findById(id);
    if (schedule == null) {
      throw new NullPointerException("해당 일정이 존재하지 않습니다.");
    }
    ScheduleResponseDto responseDto = new ScheduleResponseDto(schedule);
    scheduleRepository.setManagerName(responseDto, responseDto.getManagerId());
    return responseDto;
  }

  public ScheduleResponseDto updateSchedule(int id, ScheduleRequestDto requestDto) {
    Schedule schedule = scheduleRepository.findById(id);
    if (schedule == null) {
      throw new NullPointerException("해당 일정이 존재하지 않습니다.");
    }
    if (!scheduleRepository.managerExists(requestDto.getManagerId())) {
      throw new NullPointerException("해당 매니저가 존재하지 않습니다.");
    }

    if (!util.encrypt(requestDto.getPwd())
        .equals(schedule.getPwd())) {
      throw new PasswordIncorrectException(ErrorCode.INCORRECT_PASSWORD);
    }
    String time = util.currentTime();
    schedule.setUpdateDate(time);
    scheduleRepository.update(schedule, requestDto);
    schedule.setManagerId(requestDto.getManagerId());
    ScheduleResponseDto responseDto = new ScheduleResponseDto(schedule);
    scheduleRepository.setManagerName(responseDto, responseDto.getManagerId());
    return responseDto;
  }

  public int deleteSchedule(int id, ScheduleRequestDto requestDto) {
    Schedule schedule = scheduleRepository.findById(id);
     if(schedule == null) {
      throw new NullPointerException("해당 일정이 존재하지 않습니다.");
    }
    if (util.encrypt(requestDto.getPwd())
        .equals(schedule.getPwd())) {
      return scheduleRepository.delete(requestDto, schedule);
    }else{
      throw new PasswordIncorrectException(ErrorCode.INCORRECT_PASSWORD);
    }
  }
}

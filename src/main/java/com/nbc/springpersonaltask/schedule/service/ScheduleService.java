package com.nbc.springpersonaltask.schedule.service;

import com.nbc.springpersonaltask.schedule.dto.ScheduleRequestDto;
import com.nbc.springpersonaltask.schedule.dto.ScheduleResponseDto;
import com.nbc.springpersonaltask.schedule.entity.Schedule;
import com.nbc.springpersonaltask.schedule.repository.ScheduleRepository;
import com.nbc.springpersonaltask.schedule.util.util;
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
      throw new IllegalArgumentException("해당 manager가 존재하지 않습니다.");
    }
    if (requestDto.getPwd()
        .length() > 64) {
      throw new IllegalArgumentException("비밀번호가 너무 깁니다.");
    } else if (requestDto.getPwd()
        .length() < 8) {
      throw new IllegalArgumentException("비밀번호가 너무 짧습니다.");
    } else {
      String time = util.currentTime();
      Schedule schedule = new Schedule(requestDto);
      Schedule saveSchedule = scheduleRepository.save(schedule);
      saveSchedule.setRegisterDate(time);
      saveSchedule.setUpdateDate(time);
      ScheduleResponseDto responseDto = new ScheduleResponseDto(saveSchedule);
      scheduleRepository.setManagerName(responseDto, responseDto.getManagerId());
      return responseDto;
    }
  }

  public List<ScheduleResponseDto> getSchedules(Integer managerId, String updateDate, int page,
      int pagesize) {
    return scheduleRepository.findAll(managerId, updateDate, page, pagesize);
  }

  public ScheduleResponseDto choiceSchedule(int id) {
    Schedule schedule = scheduleRepository.findById(id);
    ScheduleResponseDto responseDto = new ScheduleResponseDto(schedule);
    scheduleRepository.setManagerName(responseDto, responseDto.getManagerId());
    return responseDto;
  }

  public ScheduleResponseDto updateSchedule(int id, ScheduleRequestDto requestDto) {
    Schedule schedule = scheduleRepository.findById(id);
    if (schedule == null) {
      throw new IllegalArgumentException("해당 ID의 일정이 존재하지 않습니다.");
    }else if(requestDto.getPwd()
        .equals(schedule.getPwd())){
      schedule = scheduleRepository.update(schedule, requestDto);
      ScheduleResponseDto responseDto = new ScheduleResponseDto(schedule);
      scheduleRepository.setManagerName(responseDto, responseDto.getManagerId());
      return responseDto;
    }  else {
      throw new IllegalArgumentException("비밀번호가 틀립니다");
    }
  }

  public int deleteSchedule(int id, ScheduleRequestDto requestDto) {
    Schedule schedule = scheduleRepository.findById(id);
    if (schedule != null & requestDto.getPwd()
        .equals(schedule.getPwd())) {
      return scheduleRepository.delete(requestDto, schedule);
    }else {
      throw new IllegalArgumentException("해당 ID의 일정이 존재하지 않습니다.");
    }
  }

  private void setManagerName(ScheduleResponseDto responseDto, int id) {
    responseDto.setManagerName(
        jdbcTemplate.queryForObject(
            "SELECT m.name FROM schedule s JOIN manager m on s.manager_id = m.id WHERE m.id = ? GROUP BY m.name",
            String.class, id));
  }

  private Schedule findById(int id) {
    // DB 조회
    String sql = "SELECT * FROM schedule WHERE id = ?";

    return jdbcTemplate.query(sql, resultSet -> {
      if (resultSet.next()) {
        Schedule schedule = new Schedule();
        schedule.setId(resultSet.getInt("id"));
        schedule.setTodo(resultSet.getString("todo"));
        schedule.setManagerId(resultSet.getInt("manager_id"));
        schedule.setPwd(resultSet.getString("pwd"));
        schedule.setRegisterDate(resultSet.getString("registerDate"));
        schedule.setUpdateDate(resultSet.getString("updateDate"));
        return schedule;
      } else {
        return null;
      }
    }, id);
  }
}

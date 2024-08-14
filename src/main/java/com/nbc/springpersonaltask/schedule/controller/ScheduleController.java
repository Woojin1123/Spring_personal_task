package com.nbc.springpersonaltask.schedule.controller;

import com.nbc.springpersonaltask.schedule.dto.ScheduleRequestDto;
import com.nbc.springpersonaltask.schedule.dto.ScheduleResponseDto;
import com.nbc.springpersonaltask.schedule.service.ScheduleService;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/todolist")
public class ScheduleController { // Http 요청에 따라 service 호출

  private final JdbcTemplate jdbcTemplate;
  private final ScheduleService scheduleService;

  public ScheduleController(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
    this.scheduleService = new ScheduleService(jdbcTemplate);
  }

  @PostMapping("/schedules") //create니까 Post로 전송
  public ScheduleResponseDto createSchedule(@RequestBody ScheduleRequestDto requestDto) {
    return scheduleService.createSchedule(requestDto);
  }

  @GetMapping("/schedules")
  public List<ScheduleResponseDto> getSchedules(
      @RequestParam(name = "managerId", required = false) Integer managerId,
      @RequestParam(name = "updateDate", required = false) String updateDate,
      @RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "pagesize", defaultValue = "10") int pagesize) {
    return scheduleService.getSchedules(managerId, updateDate, page, pagesize);
  }

  @GetMapping("/schedules/{id}") //선택한 일정 조회
  public ScheduleResponseDto choiceSchedule(@PathVariable int id) {
    return scheduleService.choiceSchedule(id);
  }

  @PutMapping("/schedules/{id}")
  public ScheduleResponseDto updateSchedule(@PathVariable int id,
      @RequestBody ScheduleRequestDto requestDto) {
    return scheduleService.updateSchedule(id, requestDto);
  }

  @DeleteMapping("/schedules/{id}")
  public int deleteSchedule(@PathVariable int id, @RequestBody ScheduleRequestDto requestDto) {
    return scheduleService.deleteSchedule(id, requestDto);
  }

}

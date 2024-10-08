  package com.nbc.springpersonaltask.todo.controller;

  import com.nbc.springpersonaltask.todo.dto.ScheduleRequestDto;
  import com.nbc.springpersonaltask.todo.dto.ScheduleResponseDto;
  import com.nbc.springpersonaltask.todo.service.ScheduleService;
  import jakarta.validation.Valid;
  import java.util.List;
  import org.springframework.http.HttpStatus;
  import org.springframework.http.ResponseEntity;
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
  @RequestMapping("/schedules")
  public class ScheduleController { // Http 요청에 따라 service 호출

    private final JdbcTemplate jdbcTemplate;
    private final ScheduleService scheduleService;

    public ScheduleController(JdbcTemplate jdbcTemplate) {
      this.jdbcTemplate = jdbcTemplate;
      this.scheduleService = new ScheduleService(jdbcTemplate);
    }

    @PostMapping//create니까 Post로 전송
    public ResponseEntity<ScheduleResponseDto> createSchedule(@Valid @RequestBody ScheduleRequestDto requestDto) {
      ScheduleResponseDto responseDto = scheduleService.createSchedule(requestDto);
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<ScheduleResponseDto>> getSchedules(
        @RequestParam(name = "name", required = false) String managerName,
        @RequestParam(name = "updateDate", required = false) String updateDate,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "pagesize", defaultValue = "10") int pagesize) {
      List<ScheduleResponseDto> responseDto = scheduleService.getSchedules(managerName, updateDate,
          page, pagesize);
      return ResponseEntity.status(HttpStatus.OK)
          .body(responseDto);
    }

    @GetMapping("/{id}") //선택한 일정 조회
    public ResponseEntity<ScheduleResponseDto> choiceSchedule(@PathVariable int id) {
      return ResponseEntity.status(HttpStatus.OK)
          .body(scheduleService.choiceSchedule(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(@PathVariable int id,
       @Valid @RequestBody ScheduleRequestDto requestDto) {
      return ResponseEntity.status(HttpStatus.OK)
          .body(scheduleService.updateSchedule(id, requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> deleteSchedule(@PathVariable int id,
       @Valid @RequestBody ScheduleRequestDto requestDto) {
      return ResponseEntity.status(HttpStatus.OK)
          .body(scheduleService.deleteSchedule(id, requestDto));
    }

  }

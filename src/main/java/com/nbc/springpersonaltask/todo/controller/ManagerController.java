package com.nbc.springpersonaltask.todo.controller;


import com.nbc.springpersonaltask.todo.dto.ManagerRequestDto;
import com.nbc.springpersonaltask.todo.dto.ManagerResponseDto;
import com.nbc.springpersonaltask.todo.service.ManagerService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/managers")
public class ManagerController {

  private final JdbcTemplate jdbcTemplate;//spring의 bean에 등록되어있어 직접 객체를 생성하지 않아도 스프링 컨테이너에 의해 관리됨
  private ManagerService managerService;

  public ManagerController(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
    managerService = new ManagerService(jdbcTemplate);
  }

  @PostMapping
  public ResponseEntity createManager(@RequestBody ManagerRequestDto requestDto) {
    ManagerResponseDto responseDto = managerService.createManager(requestDto);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(responseDto);
  }

  @GetMapping
  public ResponseEntity getManagers() {
    List<ManagerResponseDto> responseDtos = managerService.getManager();
    return ResponseEntity.status(HttpStatus.OK).body(responseDtos);
  }

  @GetMapping("/{id}")
  public ResponseEntity getManagerById(@PathVariable int id) {
    ManagerResponseDto responseDto = managerService.getManagerById(id);
    return ResponseEntity.status(HttpStatus.OK).body(responseDto);
  }
}

package com.nbc.springpersonaltask.todo.service;

import com.nbc.springpersonaltask.todo.dto.ManagerRequestDto;
import com.nbc.springpersonaltask.todo.dto.ManagerResponseDto;
import com.nbc.springpersonaltask.todo.entity.Manager;
import com.nbc.springpersonaltask.todo.repository.ManagerRepository;
import com.nbc.springpersonaltask.todo.util.Util;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;

public class ManagerService {

  private final JdbcTemplate jdbcTemplate;
  private ManagerRepository managerRepository;

  public ManagerService(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
    this.managerRepository = new ManagerRepository(jdbcTemplate);
  }


  public ManagerResponseDto createManager(ManagerRequestDto requestDto) {
    ManagerResponseDto responseDto;
    Manager manager = new Manager(requestDto);
    String time = Util.currentTime();
    manager.setUpdateDate(time);
    manager.setRegisterDate(time);
    managerRepository.save(manager);
    responseDto = new ManagerResponseDto(manager);
    return responseDto;
  }

  public List<ManagerResponseDto> getManager() {
    return managerRepository.findAll();
  }

  public ManagerResponseDto getManagerById(int id) {
    return managerRepository.findById(id);
  }
}

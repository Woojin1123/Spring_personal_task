package com.nbc.springpersonaltask.schedule.repository;

import org.springframework.jdbc.core.JdbcTemplate;

public class ScheduleRepository { // DB접근 관련 기능 수행하는 클래스

  private final JdbcTemplate jdbcTemplate;

  public ScheduleRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }
  public void save(){

  }
}

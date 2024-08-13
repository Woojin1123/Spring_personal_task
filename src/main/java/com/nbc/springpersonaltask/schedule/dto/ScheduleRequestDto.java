package com.nbc.springpersonaltask.schedule.dto;

import lombok.Getter;

@Getter

public class ScheduleRequestDto {
    private String todo;
    private int managerId;
    private String pwd;
    private String registerDate;
    private String updateDate;
}

package com.nbc.springpersonaltask.schedule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@Getter

public class ScheduleRequestDto {
    private String todo;
    private String manager;
    private String pwd;
    private String registerDate;
    private String updateDate;
}

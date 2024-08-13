package com.nbc.springpersonaltask.schedule.dto;

import com.nbc.springpersonaltask.schedule.entity.Schedule;
import lombok.Getter;

@Getter
public class ScheduleResponseDto {
    private int id;
    private String todo;
    private int managerId;
    private String registerDate;
    private String updateDate;


    public ScheduleResponseDto(Schedule schedule) {
        this.id = schedule.getId();
        this.todo = schedule.getTodo();
        this.managerId = schedule.getManagerId();
        this.registerDate = schedule.getRegisterDate();
        this.updateDate = schedule.getUpdateDate();
    }

    public ScheduleResponseDto(int id, String todo, int managerId, String registerDate, String updateDate) {
        this.id = id;
        this.todo = todo;
        this.managerId = managerId;
        this.registerDate = registerDate;
        this.updateDate = updateDate;
    }
}

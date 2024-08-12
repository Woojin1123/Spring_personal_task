package com.nbc.springpersonaltask.schedule.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nbc.springpersonaltask.schedule.dto.ScheduleRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Schedule {
    String todo;
    String manager;
    String pwd;
    String registerDate;
    String updateDate;
    int id;

    public Schedule(ScheduleRequestDto requestDto) {
        this.todo = requestDto.getTodo();
        this.manager = requestDto.getManager();
        this.pwd = requestDto.getPwd();
        this.registerDate = requestDto.getRegisterDate();
        this.updateDate = requestDto.getUpdateDate();
    }
}

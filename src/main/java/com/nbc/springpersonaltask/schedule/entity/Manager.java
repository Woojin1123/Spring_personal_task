package com.nbc.springpersonaltask.schedule.entity;

import com.nbc.springpersonaltask.schedule.dto.ManagerRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Manager {
    private int id;
    private String name;
    private String email;
    private String registerDate;
    private String updateDate;
    public Manager(ManagerRequestDto requestDto) {
        this.name = requestDto.getName();
        this.email = requestDto.getEmail();
    }
}

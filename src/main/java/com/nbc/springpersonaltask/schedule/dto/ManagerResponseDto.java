package com.nbc.springpersonaltask.schedule.dto;

import com.nbc.springpersonaltask.schedule.entity.Manager;
import lombok.Getter;

@Getter
public class ManagerResponseDto {
    private int id;
    private String name;
    private String email;
    private String registerDate;
    private String updateDate;
    public ManagerResponseDto(Manager manager) {
        this.id = manager.getId();
        this.email = manager.getEmail();
        this.name = manager.getName();
        this.registerDate = manager.getRegisterDate();
        this.updateDate = manager.getUpdateDate();
    }
}

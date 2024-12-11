package org.example.lab4_technology.data;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Teachers {
    private int id;
    private String name;

    public Teachers(int id, String name) {
        this.id = id;
        this.name = name;
    }
}


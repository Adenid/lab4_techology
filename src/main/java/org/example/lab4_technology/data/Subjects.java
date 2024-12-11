package org.example.lab4_technology.data;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Subjects {
    int id;
    String name;

    public Subjects(int id, String name) {
        this.id = id;
        this.name = name;
    }
}

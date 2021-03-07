package ru.netology.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Issue implements Comparable<Issue> {
    private int id;
    private String title;
    private User author;
    private Project project;
    private Set <Assignee> assignees;
    private Set<Label> labels;
    private String status;

    public boolean matchesStatus(String status) {
        return this.status.equalsIgnoreCase(status);
    }

    @Override
    public int compareTo(Issue o) {
        return id - o.id;
    }
}

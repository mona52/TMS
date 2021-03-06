package ru.netology.Domain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Issue implements Comparable <Issue> {
    private int id;
    private String title;
    private User author;
    private Project project;
    private Set <Assignees> assignees;
    private Set <Labels> labels;
    private String status;

   public boolean matchesStatus(String status) {
        if (this.status.equalsIgnoreCase(status)) {
            return true;
        }
        return false;
    }

    @Override
    public int compareTo(Issue o) {
        return id - o.id;
    }
}

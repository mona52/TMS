package ru.netology.domain;


import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.List;

public class IssuePredicate
{

    public static Predicate<Issue> isAuthor(User author) {
        return p -> p.getAuthor().equals(author);
    }

    public static Predicate<Issue> isLabel(Set<Label> labelSet) {
        return p -> p.getLabels().containsAll(labelSet);
    }

    public static Predicate<Issue> isAssignee(Set<Assignee> assigneeSet) {
        return p -> p.getAssignees().containsAll(assigneeSet);
    }

    public static List<Issue> filterIssues(List<Issue> issueList, Predicate<Issue> predicate) {
        return issueList.stream()
                .filter(predicate)
                .collect(Collectors.<Issue>toList());
    }
}

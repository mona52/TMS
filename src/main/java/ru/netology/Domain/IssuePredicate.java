package ru.netology.Domain;


import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.List;

public class IssuePredicate implements Predicate<Issue> {


    @Override
    public boolean test(Issue issue) {
        return false;
    }

    @Override
    public Predicate<Issue> and(Predicate<? super Issue> other) {
        return null;
    }

    @Override
    public Predicate<Issue> negate() {
        return null;
    }

    @Override
    public Predicate<Issue> or(Predicate<? super Issue> other) {
        return null;
    }

    public static Predicate<Issue> isAuthor(User author) {
        return p -> p.getAuthor().equals(author);
    }

    public static Predicate<Issue> isLabel(Set<Labels> labelsSet) {
        return p -> p.getLabels().containsAll(labelsSet);
    }

    public static Predicate<Issue> isAssignee(Set<Assignees> assigneesSet) {
        return p -> p.getAssignees().containsAll(assigneesSet);
    }

    public static List<Issue> filterIssues(List<Issue> issueList, Predicate<Issue> predicate) {
        return issueList.stream()
                .filter(predicate)
                .collect(Collectors.<Issue>toList());
    }
}

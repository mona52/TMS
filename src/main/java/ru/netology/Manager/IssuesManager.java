package ru.netology.Manager;

import ru.netology.Domain.*;
import ru.netology.Repository.IssuesRepository;

import java.util.*;
import java.util.function.Predicate;

public class IssuesManager {

        private IssuesRepository repository;

        public IssuesManager(IssuesRepository repository) {
            this.repository = repository;
        }

        public IssuesManager() {
        }

        public void add(Issue item) {
            repository.add(item);
        }

        public List<Issue> getAll() {
            List<Issue> result = repository.getAll();
            return result;
        }

    public List<Issue> findAllThatMatchesStatus(String status) {
        List<Issue> issuesWithStatus = new ArrayList<>();
        for (Issue issue : getAll()) {
            if (issue.matchesStatus(status)) {
                issuesWithStatus.add(issue);
            }
        }
        return issuesWithStatus;
    }

    public List<Issue> filterByAuthor(User author) {
        List<Issue> allIssues = repository.getAll();
        List<Issue> issuesWithAuthor = new ArrayList<>();
        issuesWithAuthor = IssuePredicate.filterIssues(allIssues,IssuePredicate.isAuthor(author));
        return issuesWithAuthor;
    }

    public List<Issue> filterByLabel(Set<Labels> labels) {
        List<Issue> allIssues = repository.getAll();
        List<Issue> issuesWithLabel = new ArrayList<>();
        issuesWithLabel = IssuePredicate.filterIssues(allIssues,IssuePredicate.isLabel(labels));
        return issuesWithLabel;
    }

    public List<Issue> filterByAssignee(Set <Assignees> assignees) {
        List<Issue> allIssues = repository.getAll();
        List<Issue> issuesWithAssignee = new ArrayList<>();
        issuesWithAssignee = IssuePredicate.filterIssues(allIssues,IssuePredicate.isAssignee(assignees));
        return issuesWithAssignee;
    }

    public List<Issue> getAllSortBy(Comparator <Issue> comparator) {
        List<Issue> result = repository.getAll();
        result.sort(comparator);
        return result;
    }

    public Comparator getComparator(String p) {
            Comparator comparator;
            switch (p) {
                case "Author": comparator = new IssueComparator.SortByAuthor();
                    break;
                case "Project": comparator = new IssueComparator.SortByProject();
                    break;
                case "Id": comparator = new IssueComparator.SortById();
                    break;
                default: comparator = new IssueComparator.SortById();
                    break;
            }
        return comparator;
    }

    public void updateStatusToClosed(int id) {
            String newStatus = "closed";
        Issue issue = repository.findById(id);
        if (issue !=null) {
            String oldStatus = issue.getStatus();
            if (oldStatus != newStatus) {
                issue.setStatus(newStatus);
            }
        }
    }

    public void updateStatusToOpen(int id) {
        String newStatus = "open";
        Issue issue = repository.findById(id);
        if (issue !=null) {
            String oldStatus = issue.getStatus();
            if (oldStatus != newStatus) {
                issue.setStatus(newStatus);
            }
        }
    }




}





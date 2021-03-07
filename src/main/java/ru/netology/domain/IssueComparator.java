package ru.netology.domain;

import java.util.Comparator;

public class IssueComparator {

    public static class SortByProject implements Comparator <Issue> {
        public int compare(Issue o1, Issue o2) {
            Project p1 = o1.getProject();
            Project p2 = o2.getProject();
            return p1.getName().compareTo(p2.getName());
        }
    }

    public static class SortByAuthor implements Comparator <Issue> {
        public int compare(Issue o1, Issue o2) {
            User a1 = o1.getAuthor();
            User a2 = o2.getAuthor();
            return a1.getName().compareTo(a2.getName());
        }
    }

    public static class SortById implements Comparator <Issue> {
        public int compare(Issue o1, Issue o2) {
            return o1.getId() - o2.getId();
        }
    }
}

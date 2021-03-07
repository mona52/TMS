package ru.netology.repository;

import ru.netology.domain.Issue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class IssuesRepository {

private final List<Issue> items = new ArrayList<>();

    public boolean add(Issue item) {
        return items.add(item);
    }

    public List<Issue> getAll() {
        return items;
    }

    public boolean addAll(Collection<? extends Issue> items) {
        return this.items.addAll(items);
    }

    public boolean removeAll(Collection<? extends Issue> items) {
        return this.items.removeAll(items);
    }

    public Issue findById(int id) {
        for (Issue item : items) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

}

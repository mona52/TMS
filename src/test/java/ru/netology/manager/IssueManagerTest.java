package ru.netology.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import ru.netology.domain.*;
import ru.netology.repository.IssuesRepository;

import java.util.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IssueManagerTest {

    private final IssuesRepository repository = new IssuesRepository();
    private final IssuesManager manager = new IssuesManager(repository);

    private final User user1 = new User("User 1");
    private final User user2 = new User("User 2");
    private final User user3 = new User("User 3");
    private final Project project1 = new Project("Проект 1");
    private final Project project2 = new Project("Проект 2");
    private final Project project3 = new Project("Проект 3");

    private final Label label1 = new Label("t1");
    private final Label label2 = new Label("t2");
    private final Label label3 = new Label("t3");
    private final Label label4 = new Label("t4");
    private final Label label5 = new Label("t5");
    private final Label label6 = new Label("t6");

    private final Assignee assignee1 = new Assignee(user1.getName());
    private final Assignee assignee2 = new Assignee(user2.getName());
    private final Assignee assignee3 = new Assignee(user3.getName());

    private final List<Issue> issues = new ArrayList<>();
    private final Issue issue1 = new Issue(1, "задача 1", user2, project1, getElementsSet(assignee1, assignee2), getElementsSet(label1, label2, label5), "open");
    private final Issue issue2 = new Issue(2, "задача 2", user3, project2, getElementsSet(assignee2, assignee3), getElementsSet(label2, label4, label6), "closed");
    private final Issue issue3 = new Issue(3, "задача 3", user1, project3, getElementsSet(assignee1), getElementsSet(label1, label5, label3), "open");
    private final Issue issue4 = new Issue(4, "задача 4", user1, project2, getElementsSet(assignee1, assignee2, assignee3), getElementsSet(label6, label1), "closed");
    private final Issue issue5 = new Issue(5, "задача 5", user3, project1, getElementsSet(), getElementsSet(), "open");

    @SafeVarargs
    public static <E> Set<E> getElementsSet(E... elements) {
        Set<E> setElements = new HashSet<>();
        Collections.addAll(setElements, elements);
        return setElements;
    }

    @Nested
    public class MultipleItems {

        @BeforeEach
        public void setUp() {
            issues.add(issue1);
            issues.add(issue2);
            issues.add(issue3);
            issues.add(issue4);
            issues.add(issue5);
            repository.addAll(issues);
        }


        @Test
            //отображение всех задач
        void getAll() {
            List<Issue> actual = manager.getAll();
            List<Issue> expected = Arrays.asList(issue1, issue2, issue3, issue4, issue5);
            assertIterableEquals(expected, actual);
        }

        @Test
            //отображение закрытых задач
        void getAllClosed() {
            List<Issue> actual = manager.findAllThatMatchesStatus("closed");
            List<Issue> expected = Arrays.asList(issue2, issue4);
            assertIterableEquals(expected, actual);
        }

        @Test
            //отображение открытых задач
        void getAllOpen() {
            List<Issue> actual = manager.findAllThatMatchesStatus("open");
            List<Issue> expected = Arrays.asList(issue1, issue3, issue5);
            assertIterableEquals(expected, actual);
        }

        @Test
            //добавление
        void addIssue() {
            Issue issue6 = new Issue(6, "задача 6", user1, project1, getElementsSet(assignee3), getElementsSet(label6, label3), "open");
            manager.add(issue6);
            List<Issue> actual = manager.getAll();
            List<Issue> expected = Arrays.asList(issue1, issue2, issue3, issue4, issue5, issue6);
            assertIterableEquals(expected, actual);
        }

        @Test
            //фильтр по автору
        void filterByAuthor() {
            List<Issue> actual = manager.filterByAuthor(user1);
            List<Issue> expected = Arrays.asList(issue3,issue4);
            assertIterableEquals(expected, actual);
        }

        @Test
            //фильтр по тегам
        void filterByLabel() {
            List<Issue> actual = manager.filterByLabel(getElementsSet(label1, label5));
            List<Issue> expected = Arrays.asList(issue1, issue3);
            assertIterableEquals(expected, actual);
        }

        @Test
            //фильтр по исполнителям
        void filterByAssignee() {
            List<Issue> actual = manager.filterByAssignee(getElementsSet(assignee2, assignee3));
            List<Issue> expected = Arrays.asList(issue2, issue4);
            assertIterableEquals(expected, actual);
        }

        @Test
            //сортировка по названию Проекта
        void sortByProject() {
            List<Issue> actual = manager.getAllSortBy(manager.getComparator("Project"));
            List<Issue> expected = Arrays.asList(issue1, issue5, issue2, issue4, issue3);
            assertIterableEquals(expected, actual);
        }

        @Test
            //сортировка по Автору
        void sortByAuthor() {
            List<Issue> actual = manager.getAllSortBy(manager.getComparator("Author"));
            List<Issue> expected = Arrays.asList(issue3, issue4, issue1, issue2, issue5);
            assertIterableEquals(expected, actual);
        }

        @Test
            //сортировка по ID
        void sortById() {
            List<Issue> actual = manager.getAllSortBy(manager.getComparator("Id"));
            List<Issue> expected = Arrays.asList(issue1, issue2, issue3, issue4, issue5);
            assertIterableEquals(expected, actual);
        }

        @Test
            //закрытие существующей открытой задачи с заданным Id
        void closeByIdOpenIssueExist() {
            int id = 1;
            String newStatus = "closed";
            String actual = null;
            String expected = null;
            manager.updateStatusToClosed(id);
            Issue issue = repository.findById(id);
            if (issue != null) {
                actual = issue.getStatus();
                expected = newStatus;
            }
            assertEquals(expected, actual);
        }

        @Test
            //закрытие несуществующей открытой задачи с заданным Id
        void closeByIdOpenIssueNotExist() {
            int id = 10;
            String newStatus = "closed";
            String actual = null;
            String expected = null;
            manager.updateStatusToClosed(id);
            Issue issue = repository.findById(id);
            if (issue != null) {
                actual = issue.getStatus();
                expected = newStatus;
            }
            assertEquals(expected, actual);
        }

        @Test
            //повторное закрытие существующей закрытой задачи с заданным Id
        void closeByIdClosedIssueExist() {
            int id = 2;
            String newStatus = "closed";
            String actual = null;
            String expected = null;
            manager.updateStatusToClosed(id);
            Issue issue = repository.findById(id);
            if (issue != null) {
                actual = issue.getStatus();
                expected = newStatus;
            }
            assertEquals(expected, actual);
        }

        @Test
            //повторное закрытие несуществующей закрытой задачи с заданным Id
        void closeByIdClosedIssueNotExist() {
            int id = 10;
            String newStatus = "closed";
            String actual = null;
            String expected = null;
            manager.updateStatusToClosed(id);
            Issue issue = repository.findById(id);
            if (issue != null) {
                actual = issue.getStatus();
                expected = newStatus;
            }
            assertEquals(expected, actual);
        }

        @Test
            //открытие существующей закрытой задачи с заданным Id
        void openByIdCloseIssueExist() {
            int id = 2;
            String newStatus = "open";
            String actual = null;
            String expected = null;
            manager.updateStatusToOpen(id);
            Issue issue = repository.findById(id);
            if (issue != null) {
                actual = issue.getStatus();
                expected = newStatus;
            }
            assertEquals(expected, actual);
        }

        @Test
            //открытие несуществующей закрытой задачи с заданным Id
        void openByIdCloseIssue() {
            int id = 10;
            String newStatus = "open";
            String actual = null;
            String expected = null;
            manager.updateStatusToOpen(id);
            Issue issue = repository.findById(id);
            if (issue != null) {
                actual = issue.getStatus();
                expected = newStatus;
            }
            assertEquals(expected, actual);
        }

        @Test
            //повторное открытие существующей открытой задачи с заданным Id
        void openByIdOpenIssueExist() {
            int id = 1;
            String newStatus = "open";
            String actual = null;
            String expected = null;
            manager.updateStatusToOpen(id);
            Issue issue = repository.findById(id);
            if (issue != null) {
                actual = issue.getStatus();
                expected = newStatus;
            }
            assertEquals(expected, actual);
        }

        @Test
            //повторное открытие несуществующей открытой задачи с заданным Id
        void openByIdOpenIssueNotExist() {
            int id = 10;
            String newStatus = "open";
            String actual = null;
            String expected = null;
            manager.updateStatusToOpen(id);
            Issue issue = repository.findById(id);
            if (issue != null) {
                actual = issue.getStatus();
                expected = newStatus;
            }
            assertEquals(expected, actual);
        }
    }

    @Nested
    public class Empty {
        @Test
            //отображение всех задач
        void getAll() {
            List<Issue> actual = manager.getAll();
            List<Issue> expected = Collections.emptyList();
            assertIterableEquals(expected, actual);
        }

        @Test
            //отображение закрытых задач
        void getAllClosed() {
            List<Issue> actual = manager.findAllThatMatchesStatus("closed");
            List<Issue> expected = Collections.emptyList();
            assertIterableEquals(expected, actual);
        }

        @Test
            //отображение открытых задач
        void getAllOpen() {
            List<Issue> actual = manager.findAllThatMatchesStatus("open");
            List<Issue> expected = Collections.emptyList();
            assertIterableEquals(expected, actual);
        }

        @Test
            //добавление
        void addIssue() {
            Issue issue6 = new Issue(6, "задача 6", user1, project1, getElementsSet(assignee3), getElementsSet(label6, label3), "open");
            manager.add(issue6);
            List<Issue> actual = manager.getAll();
            List<Issue> expected = Collections.singletonList(issue6);
            assertIterableEquals(expected, actual);
        }

        @Test
            //фильтр по автору
        void filterByAuthor() {
            List<Issue> actual = manager.filterByAuthor(user1);
            List<Issue> expected = Collections.emptyList();
            assertIterableEquals(expected, actual);
        }

        @Test
            //фильтр по тегам
        void filterByLabel() {
            List<Issue> actual = manager.filterByLabel(getElementsSet(label1, label5));
            List<Issue> expected = Collections.emptyList();
            assertIterableEquals(expected, actual);
        }

        @Test
            //фильтр по исполнителям
        void filterByAssignee() {
            List<Issue> actual = manager.filterByAssignee(getElementsSet(assignee2, assignee3));
            List<Issue> expected = Collections.emptyList();
            assertIterableEquals(expected, actual);
        }

        @Test
            //сортировка по названию Проекта
        void sortByProject() {
            List<Issue> actual = manager.getAllSortBy(manager.getComparator("Project"));
            List<Issue> expected = Collections.emptyList();
            assertIterableEquals(expected, actual);
        }

        @Test
            //сортировка по Автору
        void sortByAuthor() {
            List<Issue> actual = manager.getAllSortBy(manager.getComparator("Author"));
            List<Issue> expected = Collections.emptyList();
            assertIterableEquals(expected, actual);
        }

        @Test
            //сортировка по ID
        void sortById() {
            List<Issue> actual = manager.getAllSortBy(manager.getComparator("Id"));
            List<Issue> expected = Collections.emptyList();
            assertIterableEquals(expected, actual);
        }

        @Test
            //закрытие существующей открытой задачи с заданным Id
        void closeByIdOpenIssueExist() {
            int id = 1;
            String newStatus = "closed";
            String actual = null;
            String expected = null;
            manager.updateStatusToClosed(id);
            Issue issue = repository.findById(id);
            if (issue != null) {
                actual = issue.getStatus();
                expected = newStatus;
            }
            assertEquals(expected, actual);

        }

        @Test
            //закрытие несуществующей открытой задачи с заданным Id
        void closeByIdOpenIssueNotExist() {
            int id = 10;
            String newStatus = "closed";
            String actual = null;
            String expected = null;
            manager.updateStatusToClosed(id);
            Issue issue = repository.findById(id);
            if (issue != null) {
                actual = issue.getStatus();
                expected = newStatus;
            }
            assertEquals(expected, actual);
        }

        @Test
            //повторное закрытие существующей закрытой задачи с заданным Id
        void closeByIdClosedIssueExist() {
            int id = 2;
            String newStatus = "closed";
            String actual = null;
            String expected = null;
            manager.updateStatusToClosed(id);
            Issue issue = repository.findById(id);
            if (issue != null) {
                actual = issue.getStatus();
                expected = newStatus;
            }
            assertEquals(expected, actual);
        }

        @Test
            //повторное закрытие несуществующей закрытой задачи с заданным Id
        void closeByIdClosedIssueNotExist() {
            int id = 10;
            String newStatus = "closed";
            String actual = null;
            String expected = null;
            manager.updateStatusToClosed(id);
            Issue issue = repository.findById(id);
            if (issue != null) {
                actual = issue.getStatus();
                expected = newStatus;
            }
            assertEquals(expected, actual);
        }


        @Test
            //открытие существующей закрытой задачи с заданным Id
        void openByIdCloseIssueExist() {
            int id = 2;
            String newStatus = "open";
            String actual = null;
            String expected = null;
            manager.updateStatusToOpen(id);
            Issue issue = repository.findById(id);
            if (issue != null) {
                actual = issue.getStatus();
                expected = newStatus;
            }
            assertEquals(expected, actual);
        }

        @Test
            //открытие несуществующей закрытой задачи с заданным Id
        void openByIdCloseIssue() {
            int id = 10;
            String newStatus = "open";
            String actual = null;
            String expected = null;
            manager.updateStatusToOpen(id);
            Issue issue = repository.findById(id);
            if (issue != null) {
                actual = issue.getStatus();
                expected = newStatus;
            }
            assertEquals(expected, actual);
        }

        @Test
            //повторное открытие существующей открытой задачи с заданным Id
        void openByIdOpenIssueExist() {
            int id = 1;
            String newStatus = "open";
            String actual = null;
            String expected = null;
            manager.updateStatusToOpen(id);
            Issue issue = repository.findById(id);
            if (issue != null) {
                actual = issue.getStatus();
                expected = newStatus;
            }
            assertEquals(expected, actual);
        }

        @Test
            //повторное открытие несуществующей открытой задачи с заданным Id
        void openByIdOpenIssueNotExist() {
            int id = 10;
            String newStatus = "open";
            String actual = null;
            String expected = null;
            manager.updateStatusToOpen(id);
            Issue issue = repository.findById(id);
            if (issue != null) {
                actual = issue.getStatus();
                expected = newStatus;
            }
            assertEquals(expected, actual);
        }

    }

    @Nested
    public class SingleItem {
        @BeforeEach
        public void setUp() {
            manager.add(issue3);
        }


        @Test
            //отображение всех задач
        void getAll() {
            List<Issue> actual = manager.getAll();
            List<Issue> expected = Collections.singletonList(issue3);
            assertIterableEquals(expected, actual);
        }

        @Test
            //отображение закрытых задач
        void getAllClosed() {
            List<Issue> actual = manager.findAllThatMatchesStatus("closed");
            List<Issue> expected = Collections.emptyList();
            assertIterableEquals(expected, actual);
        }

        @Test
            //отображение открытых задач
        void getAllOpen() {
            List<Issue> actual = manager.findAllThatMatchesStatus("open");
            List<Issue> expected = Collections.singletonList(issue3);
            assertIterableEquals(expected, actual);
        }

        @Test
            //добавление
        void addIssue() {
            Issue issue6 = new Issue(6, "задача 6", user1, project1, getElementsSet(assignee3), getElementsSet(label6, label3), "open");
            manager.add(issue6);
            List<Issue> actual = manager.getAll();
            List<Issue> expected = Arrays.asList(issue3, issue6);
            assertIterableEquals(expected, actual);
        }

        @Test
            //фильтр по автору
        void filterByAuthor() {
            List<Issue> actual = manager.filterByAuthor(user1);
            List<Issue> expected = Collections.singletonList(issue3);
            assertIterableEquals(expected, actual);
        }

        @Test
            //фильтр по тегам
        void filterByLabel() {
            List<Issue> actual = manager.filterByLabel(getElementsSet(label1, label5));
            List<Issue> expected = Collections.singletonList(issue3);
            assertIterableEquals(expected, actual);
        }

        @Test
            //фильтр по исполнителям
        void filterByAssignee() {
            List<Issue> actual = manager.filterByAssignee(getElementsSet(assignee2, assignee3));
            List<Issue> expected = Collections.emptyList();
            assertIterableEquals(expected, actual);
        }

        @Test
            //сортировка по названию Проекта
        void sortByProject() {
            List<Issue> actual = manager.getAllSortBy(manager.getComparator("Project"));
            List<Issue> expected = Collections.singletonList(issue3);
            assertIterableEquals(expected, actual);
        }

        @Test
            //сортировка по Автору
        void sortByAuthor() {
            List<Issue> actual = manager.getAllSortBy(manager.getComparator("Author"));
            List<Issue> expected = Collections.singletonList(issue3);
            assertIterableEquals(expected, actual);
        }

        @Test
            //сортировка по ID
        void sortById() {
            List<Issue> actual = manager.getAllSortBy(manager.getComparator("Id"));
            List<Issue> expected = Collections.singletonList(issue3);
            assertIterableEquals(expected, actual);
        }

        @Test
            //закрытие существующей открытой задачи с заданным Id
        void closeByIdOpenIssueExist() {
            int id = 3;
            String newStatus = "closed";
            String actual = null;
            String expected = null;
            manager.updateStatusToClosed(id);
            Issue issue = repository.findById(id);
            if (issue != null) {
                actual = issue.getStatus();
                expected = newStatus;
            }
            assertEquals(expected, actual);

        }

        @Test
            //закрытие несуществующей открытой задачи с заданным Id
        void closeByIdOpenIssueNotExist() {
            int id = 1;
            String newStatus = "closed";
            String actual = null;
            String expected = null;
            manager.updateStatusToClosed(id);
            Issue issue = repository.findById(id);
            if (issue != null) {
                actual = issue.getStatus();
                expected = newStatus;
            }
            assertEquals(expected, actual);

        }

        @Test
            //повторное закрытие существующей закрытой задачи с заданным Id
        void closeByIdClosedIssueExist() {
            int id = 3;
            String newStatus = "closed";
            String actual = null;
            String expected = null;
            manager.updateStatusToClosed(id);
            Issue issue = repository.findById(id);
            if (issue != null) {
                actual = issue.getStatus();
                expected = newStatus;
            }
            assertEquals(expected, actual);
        }

        @Test
            //повторное закрытие несуществующей закрытой задачи с заданным Id
        void closeByIdClosedIssueNotExist() {
            int id = 1;
            String newStatus = "closed";
            String actual = null;
            String expected = null;
            manager.updateStatusToClosed(id);
            Issue issue = repository.findById(id);
            if (issue != null) {
                actual = issue.getStatus();
                expected = newStatus;
            }
            assertEquals(expected, actual);
        }


        @Test
            //открытие существующей закрытой задачи с заданным Id
        void openByIdCloseIssueExist() {
            int id = 3;
            String newStatus = "open";
            String actual = null;
            String expected = null;
            manager.updateStatusToOpen(id);
            Issue issue = repository.findById(id);
            if (issue != null) {
                actual = issue.getStatus();
                expected = newStatus;
            }
            assertEquals(expected, actual);
        }

        @Test
            //открытие несуществующей закрытой задачи с заданным Id
        void openByIdCloseIssue() {
            int id = 1;
            String newStatus = "open";
            String actual = null;
            String expected = null;
            manager.updateStatusToOpen(id);
            Issue issue = repository.findById(id);
            if (issue != null) {
                actual = issue.getStatus();
                expected = newStatus;
            }
            assertEquals(expected, actual);
        }

        @Test
            //повторное открытие существующей открытой задачи с заданным Id
        void openByIdOpenIssueExist() {
            int id = 3;
            String newStatus = "open";
            String actual = null;
            String expected = null;
            manager.updateStatusToOpen(id);
            Issue issue = repository.findById(id);
            if (issue != null) {
                actual = issue.getStatus();
                expected = newStatus;
            }
            assertEquals(expected, actual);
        }

        @Test
            //повторное открытие несуществующей открытой задачи с заданным Id
        void openByIdOpenIssueNotExist() {
            int id = 1;
            String newStatus = "open";
            String actual = null;
            String expected = null;
            manager.updateStatusToOpen(id);
            Issue issue = repository.findById(id);
            if (issue != null) {
                actual = issue.getStatus();
                expected = newStatus;
            }
            assertEquals(expected, actual);
        }

    }

}

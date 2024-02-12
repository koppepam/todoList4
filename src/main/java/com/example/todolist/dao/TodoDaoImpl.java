package com.example.todolist.dao;

import java.util.ArrayList;
import java.util.List;
import com.example.todolist.common.Utils;
import com.example.todolist.entity.Todo;
import com.example.todolist.form.TodoQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TodoDaoImpl implements TodoDao {
    private final EntityManager entityManager;

    // JPQL
    @Override
    public List<Todo> findByJPQL(TodoQuery todoQuery) {
        StringBuilder sb = new StringBuilder("select t from Todo t where 1 = 1");
        List<Object> params = new ArrayList<>();
        int pos = 0;

        // 実行するJPQLのくみたて
        if (todoQuery.getTitle().length() > 0) {
            sb.append(" and t.title like ?" + (++pos));
            params.add("%" + todoQuery.getTitle() + "%");
        }

        if (todoQuery.getImportance() != -1) {
            sb.append(" and t.importance = ?" + (++pos));
            params.add(todoQuery.getImportance());
        }

        if (todoQuery.getUrgency() != -1) {
            sb.append(" and t.urgency = ?" + (++pos));
            params.add(todoQuery.getUrgency());
        }

        if (!todoQuery.getDeadlineFrom().equals("")) {
            sb.append(" and t.deadline >= ?" + (++pos));
            params.add(Utils.str2date(todoQuery.getDeadlineFrom()));
        }

        if (!todoQuery.getDeadlineTo().equals("")) {
            sb.append(" and t.deadline <= ?" + (++pos));
            params.add(Utils.str2date(todoQuery.getDeadlineTo()));
        }

        if (todoQuery.getDone() != null && todoQuery.getDone().equals("Y")) {
            sb.append(" and t.done = ?" + (++pos));
            params.add(todoQuery.getDone());
        }
        // order
        sb.append(" order by id");

        Query query = entityManager.createQuery(sb.toString());
        for (int i = 0; i < params.size(); ++i) {
            query = query.setParameter(i + 1, params.get(i));
        }

        @SuppressWarnings("unchecked")
        List<Todo> list = query.getResultList();
        return list;
    }

    // Criteria API
    @Override
    public List<Todo> findByCriteria(TodoQuery todoQuery) {
        return null;
    }
}

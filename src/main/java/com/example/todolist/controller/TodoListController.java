package com.example.todolist.controller;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.Model;
import com.example.todolist.entity.Todo;
import com.example.todolist.form.TodoData;
import com.example.todolist.form.TodoQuery;
import com.example.todolist.repository.TodoRepository;
import com.example.todolist.service.TodoService;
// import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import com.example.todolist.dao.TodoDaoImpl;



@Controller
@RequiredArgsConstructor
public class TodoListController {
    private final TodoRepository todoRepository;
    private final TodoService todoService;
    private final HttpSession session;

    @PersistenceContext
    private EntityManager entityManager;
    TodoDaoImpl todoDaoImpl;

    @PostConstruct
    public void init() {
        todoDaoImpl = new TodoDaoImpl(entityManager);
    }

    // Todo 一覧表示
    @GetMapping("/todo")
    public ModelAndView showTodoList(ModelAndView mv) {
        mv.setViewName("todoList");
        List<Todo> todoList = todoRepository.findAll();
        mv.addObject("todoList", todoList);
        mv.addObject("todoQuery", new TodoQuery());
        return mv;
    }

    // Todo 入力フォーム表示
    @PostMapping("todo/create/form")
    public ModelAndView createTodo(ModelAndView mv) {
        mv.setViewName("todoForm");
        mv.addObject("todoData", new TodoData());
        session.setAttribute("mode", "create");
        return mv;
    }

    // Todo 追加処理
    @PostMapping("/todo/create/do")
    public String createTodo(@ModelAttribute @Validated TodoData todoData,
                                   BindingResult result, Model model) {
        // エラーチェック
        boolean isValid = todoService.isValid(todoData, result);
        if (!result.hasErrors() && isValid) {
            Todo todo = todoData.toEntity();
            todoRepository.saveAndFlush(todo);
            return "redirect:/todo";
        } else {
            // mv.addObject("todoData", todoData);
            return "todoForm";
        }
    }
    
    // Todo 一覧へ戻る
    @PostMapping("/todo/cancel")
    public String cancel() {
        return "redirect:/todo";
    }

    @GetMapping("/todo/{id}")
    public ModelAndView todoById(@PathVariable(name = "id") int id, ModelAndView mv) {
        mv.setViewName("todoForm");
        Todo todo = todoRepository.findById(id).get();
        mv.addObject("todoData", todo);
        session.setAttribute("mode", "update");
        return mv;
    }    

    @PostMapping("/todo/update")
    public String updateTodo(@ModelAttribute @Validated TodoData todoData,
                              BindingResult result,
                              Model model) {
    // エラーチェック
    boolean isValid = todoService.isValid(todoData, result);
    if (!result.hasErrors() && isValid) {
        Todo todo = todoData.toEntity();
        todoRepository.saveAndFlush(todo);
        return "redirect:/todo";
    } else {
        // model.addAttribute("todoData", todoData);
        return "todoForm";
        }
    }

    @PostMapping("/todo/delete")
    public String deleteTodo(@ModelAttribute TodoData todoData) {
        todoRepository.deleteById(todoData.getId());
        return "redirect:/todo";
    }
    
    @PostMapping("/todo/query")
    public ModelAndView queryTodo(@ModelAttribute TodoQuery todoQuery, 
                                  BindingResult result,
                                  ModelAndView mv) {
        mv.setViewName("todoList");

        List<Todo> todoList = null;
        if (todoService.isValid(todoQuery, result)) {
            // todoList = todoService.doQuery(todoQuery);
            todoList = todoDaoImpl.findByCriteria(todoQuery);
        }
        mv.addObject("todoList", todoList);
        return mv;
    }
}

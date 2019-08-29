package com.wizerdshins.tasksmanager.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.Route;
import com.wizerdshins.tasksmanager.component.CompanyEditor;
import com.wizerdshins.tasksmanager.component.TaskEditor;
import com.wizerdshins.tasksmanager.entity.Company;
import com.wizerdshins.tasksmanager.entity.Task;
import com.wizerdshins.tasksmanager.repository.CompanyRepository;
import com.wizerdshins.tasksmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@Route
public class MainView extends VerticalLayout {

    private TaskRepository taskRepository;
    private CompanyRepository companyRepository;

    private Grid<Task> taskGrid;
    private TaskEditor taskEditor;

    @Autowired
    public MainView(TaskRepository taskRepository,
                    CompanyRepository companyRepository,
                    TaskEditor taskEditor) {

        this.companyRepository = companyRepository;
        this.taskRepository = taskRepository;

        this.taskEditor = taskEditor;

        taskGrid = new Grid<>(Task.class);
        taskGrid.setColumns("message", "company", "dateCreate", "dateComplete", "status");

        add(new H1("Task Manager"), buildPanel(), taskGrid, taskEditor);

        showAllTasks();

        taskEditor.setChangeHandler(() -> {
            taskEditor.setVisible(true);
            showAllTasks();
        });
    }

    private Component buildPanel() {

        TextField taskMessageField = new TextField("Task");
        ComboBox<Company> companySelect = new ComboBox<>("Company");
        companySelect.setItems(companyRepository.findAll());

        Button addTask = new Button("Add");

        Button addCompany = new Button("New Company");

        Binder<Task> taskBinder = new Binder<>(Task.class);
//        Binder<Company> companyBinder = new Binder<>(Company.class);

        taskBinder.forField(taskMessageField)
                .asRequired("Please, add task message")
                .bind("message");

        taskBinder.forField(companySelect)
                .asRequired("Please, select a company")
                .bind("company");

        /*
        listeners work description
         */

        // TODO how to write a comments in code

        addTask.addClickListener(click -> {
           Task newTask = new Task("", new Date(), "");
           try {
               taskBinder.writeBean(newTask);
               taskRepository.save(newTask);
               taskGrid.setItems(taskRepository.findAll());
               taskBinder.readBean(new Task());

               Notification.show(
                       "Task \'" + newTask.getMessage() + "\' has been added",
                       2000,
                       Notification.Position.TOP_END);

           } catch (ValidationException e) {
               e.printStackTrace();
           }
        });

        addCompany.addClickListener(click -> {

        });

        taskGrid.asSingleSelect().addValueChangeListener(event -> {
           taskEditor.editTask(event.getValue());
        });


        HorizontalLayout formLayout = new HorizontalLayout(
                taskMessageField, companySelect, addTask, addCompany);
        formLayout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);

        Div menuWrapper = new Div(formLayout);
        menuWrapper.setWidth("100%");

        return menuWrapper;
    }

    private void showAllTasks() {
        taskGrid.setItems(taskRepository.findAll());
    }
}

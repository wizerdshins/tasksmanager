package com.wizerdshins.tasksmanager.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import com.wizerdshins.tasksmanager.entity.Company;
import com.wizerdshins.tasksmanager.entity.Task;
import com.wizerdshins.tasksmanager.repository.CompanyRepository;
import com.wizerdshins.tasksmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;

@Route
public class MainView extends VerticalLayout {

    private TaskRepository taskRepository;
    private CompanyRepository companyRepository;

    private Grid<Task> taskGrid;

    @Autowired
    public MainView(TaskRepository taskRepository,
                    CompanyRepository companyRepository) {

        this.companyRepository = companyRepository;
        this.taskRepository = taskRepository;

        taskGrid = new Grid<>(Task.class);
        taskGrid.setColumns("message", "company", "dateCreate", "status");

        add(new H1("Task Manager"), buildPanel(), taskGrid);
    }

    private Component buildPanel() {

        TextField taskMessageField = new TextField("Task");
        ComboBox<Company> companySelect = new ComboBox<>("Company");
        companySelect.setItems(companyRepository.findAll());

        Button addTask = new Button("Add");
        Binder<Task> taskBinder = new Binder<>(Task.class);

        taskBinder.forField(taskMessageField)
                .asRequired("Please, add task message")
                .bind("message");

        taskBinder.forField(companySelect)
                .asRequired("Please, select a company")
                .bind("company");

        HorizontalLayout formLayout = new HorizontalLayout(
                taskMessageField, companySelect, addTask);
        formLayout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);

        Div wraper = new Div(formLayout);
        wraper.setWidth("100%");

        return wraper;
    }
}

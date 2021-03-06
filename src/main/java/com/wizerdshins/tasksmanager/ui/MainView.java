package com.wizerdshins.tasksmanager.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;

import com.wizerdshins.tasksmanager.component.TaskEditor;
import com.wizerdshins.tasksmanager.entity.Company;
import com.wizerdshins.tasksmanager.entity.Task;
import com.wizerdshins.tasksmanager.repository.CompanyRepository;
import com.wizerdshins.tasksmanager.repository.TaskRepository;

import com.wizerdshins.tasksmanager.util.ExcelWriter;
import com.wizerdshins.tasksmanager.util.JsonWriter;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@Route
public class MainView extends VerticalLayout {

    private static final Logger log = Logger.getLogger(MainView.class);

    private boolean isJson = false;
    private boolean isWriteEventEmpty = true;

    private TaskRepository taskRepository;
    private CompanyRepository companyRepository;

    private Grid<Task> taskGrid;
    private TaskEditor taskEditor;

    private JsonWriter jsonWriter;
    private ExcelWriter excelWriter;

    @Autowired
    public MainView(TaskRepository taskRepository,
                    CompanyRepository companyRepository,
                    TaskEditor taskEditor) {

        log.info("Application is running");

        this.companyRepository = companyRepository;
        this.taskRepository = taskRepository;
        this.taskEditor = taskEditor;

        taskGrid = new Grid<>(Task.class);
        taskGrid.setColumns("message", "company", "dateCreate", "dateComplete", "status");

        add(new H1("Task Manager"), buildPanel(), taskGrid, taskEditor);

        showAllTasks();

        jsonWriter = new JsonWriter();
        excelWriter = new ExcelWriter();

        taskEditor.setChangeHandler(() -> {
            taskEditor.setVisible(true);
            showAllTasks();
        });
    }

    private Component buildPanel() {

        /*
        components for adding new task
         */

        Button addTask = new Button("Add Task"); // old value "Add"

        ComboBox<Company> companySelect = new ComboBox<>("Company");
        companySelect.setItems(companyRepository.findAll());

        TextField taskMessageField = new TextField("Task");

        Binder<Task> taskBinder = new Binder<>(Task.class);

        taskBinder.forField(taskMessageField)
                .asRequired("Please, enter a task message")
                .bind("message");
        taskBinder.forField(companySelect)
                .asRequired("Please, select a company")
                .bind("company");

        /*
        components for adding new company
         */

        Button addCompany = new Button("Add Company"); // old value "New Company"

        Button companyEdit = new Button("Save");
        Button cancelCompanyEdit = new Button("Cancel");

        companyEdit.getElement().getThemeList().add("primary");
        cancelCompanyEdit.getElement().getThemeList().add("secondary");

        Grid<Company> companyGrid = new Grid<>(Company.class);
        companyGrid.setColumns("name", "address", "phone");
        companyGrid.setItems(companyRepository.findAll());

        TextField companyNameField = new TextField("Company name");
        TextField companyAddressField = new TextField("Address");
        TextField companyPhoneField = new TextField("Phone");

        Binder<Company> companyBinder = new Binder<>(Company.class);

        companyBinder.forField(companyNameField)
                     .asRequired("Please, enter a company name")
                     .bind(Company::getName, Company::setName);
        companyBinder.forField(companyAddressField)
                     .asRequired("Please, enter a company address")
                     .bind(Company::getAddress, Company::setAddress);
        companyBinder.forField(companyPhoneField)
                     .asRequired("Please, enter a company phone")
                     .bind(Company::getPhone, Company::setPhone);

        HorizontalLayout editCompanyFields = new HorizontalLayout(
                companyNameField, companyAddressField, companyPhoneField);
        HorizontalLayout companyGridLayout = new HorizontalLayout(companyGrid);
        HorizontalLayout editCompanyButtons = new HorizontalLayout(
                companyEdit, companyGridLayout, cancelCompanyEdit);

        Dialog companyEditDialog = new Dialog();

        companyEditDialog.setCloseOnEsc(true);
        companyEditDialog.add(editCompanyFields, companyGridLayout, editCompanyButtons);

        /*
        components for writing data to a file
         */

        Button writeDialogButton = new Button("Write"); // TODO fix this title
        Button startWriteButton = new Button("Write");
        Button cancelWriteButton = new Button("Cancel");

        startWriteButton.getElement().getThemeList().add("primary");
        cancelWriteButton.getElement().getThemeList().add("secondary");

        Dialog writeDataDialog = new Dialog();

        Label descriptionLabel = new Label("Please, enter a directory path");

        RadioButtonGroup<String> formatVariations = new RadioButtonGroup<>();
        formatVariations.setItems("JSON", "Excel file");
        formatVariations.setLabel("Format");

        TextField pathToFileField = new TextField();

        VerticalLayout writeLayout = new VerticalLayout(descriptionLabel, pathToFileField, formatVariations);

        writeDataDialog.setCloseOnEsc(true);
        writeDataDialog.add(writeLayout, startWriteButton, cancelWriteButton);

        /*
        button's listeners
         */

        addTask.addClickListener(click -> {
            log.info("Adding task... ");
            Task newTask = new Task("", new Date(), "");
            try {
               taskBinder.writeBean(newTask);
               taskRepository.save(newTask);
               taskGrid.setItems(taskRepository.findAll());
               taskBinder.readBean(new Task());

               log.info("Task \'" + newTask.getMessage() +
                       "\' from " + newTask.getCompany() +
                       " was successfully added");

               Notification.show(
                       "Task \'" + newTask.getMessage() + "\' has been added",
                       2000,
                       Notification.Position.TOP_END);

           } catch (ValidationException e) {
               log.warn("Task validation error", e);
               e.printStackTrace();
           }
        });

        addCompany.addClickListener(click -> {
            companyEditDialog.open();
        });

        companyEdit.addClickListener(click -> {
            log.info("Adding company... ");
            Company newCompany = new Company("", "", "");
            try {
               companyBinder.writeBean(newCompany);
               companyRepository.save(newCompany);
               companyBinder.readBean(new Company());

               companySelect.setItems(companyRepository.findAll());
               companyGrid.setItems(companyRepository.findAll());

               log.info("Company \'" + newCompany.getName() +
                       " was successfully added");

               Notification.show("Company \'" + newCompany.getName() + "\' has been added",
                       2000,
                       Notification.Position.TOP_END);

           } catch (ValidationException e) {
               log.warn("Company validation error", e);
               e.printStackTrace();
           }
        });

        cancelCompanyEdit.addClickListener(click -> {
           companyEditDialog.close();
        });

        /*
        writing to file
         */

        writeDialogButton.addClickListener(click -> {
            writeDataDialog.open();
        });

        formatVariations.addValueChangeListener(event -> {
            isWriteEventEmpty = event.getValue().isEmpty();
            isJson = event.getValue().equals("JSON");
        });

        startWriteButton.addClickListener(click -> {

            if (isWriteEventEmpty) {
                Notification.show("Please, select a file format",
                        2000,
                        Notification.Position.TOP_END);
                return;
            }

            String path = pathToFileField.getValue();
            if (path.equals("")) {
                Notification.show("Please, enter a directory path",
                        2000,
                        Notification.Position.TOP_END);
                return;
            }

            log.info("Start writing...");
            if (isJson) {
                try {
                    jsonWriter.write(path, companyRepository.findAll());
                    log.info("Results were recorder in .txt to \'" + path + "\' directory");

                    Notification.show("Results were recorded in .txt",
                            2000,
                            Notification.Position.TOP_END);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    excelWriter.write(path, taskRepository.findAll());
                    log.info("Results were recorder in .xls to \'" + path + "\' directory");

                    Notification.show("Results were recorded in .xls",
                            2000,
                            Notification.Position.TOP_END);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        cancelWriteButton.addClickListener(click -> {
           writeDataDialog.close();
        });

        /*
        event for task editor enabling
         */

        taskGrid.asSingleSelect().addValueChangeListener(event -> {
           taskEditor.editTask(event.getValue());
        });

        HorizontalLayout formLayout = new HorizontalLayout(
                taskMessageField, companySelect, addTask, addCompany, writeDialogButton);
        formLayout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);

        Div menuWrapper = new Div(formLayout);
        menuWrapper.setWidth("100%");

        return menuWrapper;
    }

    private void showAllTasks() {
        taskGrid.setItems(taskRepository.findAll());
    }
}

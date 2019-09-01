package com.wizerdshins.tasksmanager.component;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.wizerdshins.tasksmanager.entity.Task;
import com.wizerdshins.tasksmanager.repository.TaskRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

@SpringComponent
@UIScope
public class TaskEditor extends VerticalLayout implements KeyNotifier {

    private static final Logger log = Logger.getLogger(TaskEditor.class);

    private TaskRepository taskRepository;

    private Task addedTask;
    private String status;

    private Button saveButton = new Button("Save");
    private Button deleteButton = new Button("Delete");
    private Button cancelButton = new Button("Cancel");

    private ComboBox<String> statusSelect = new ComboBox<>(
            "Status", "open", "WIP", "done");

    private TextField editMessage = new TextField("Message");

    private Binder<Task> editTaskBinder = new Binder<>(Task.class);

    private HorizontalLayout editFormLayout = new HorizontalLayout(
            editMessage, statusSelect);
    private HorizontalLayout buttonLayout = new HorizontalLayout(
            saveButton, deleteButton, cancelButton);

    private Div editFormWrapper = new Div(editFormLayout, buttonLayout);

    private ChangeHandler changeHandler;

    public interface ChangeHandler {
        void onChange();
    }

    @Autowired
    public TaskEditor(TaskRepository taskRepository) {

        this.taskRepository = taskRepository;

        add(editFormWrapper);

        editTaskBinder.forField(editMessage)
                      .bind(Task::getMessage, Task::setMessage);
        editTaskBinder.forField(statusSelect)
                .bind(Task::getStatus, Task::setStatus);

        statusSelect.addValueChangeListener(event -> {
           status = event.getValue();
        });

        saveButton.getElement().getThemeList().add("primary");
        deleteButton.getElement().getThemeList().add("secondary");
        cancelButton.getElement().getThemeList().add("tertiary");

        addKeyPressListener(Key.ENTER, click -> save(addedTask));

        saveButton.addClickListener(click -> save(addedTask));
        deleteButton.addClickListener(click -> delete());
        cancelButton.addClickListener(click -> {
           setVisible(false);
        });

        setSpacing(true);
        setVisible(false);
    }

    private void save(Task updatedTask) {

        log.info("Start editing... ");

        boolean isDone = false;
        if (status.equals("done")) {
            isDone = true;
            log.info("Task \'" + updatedTask.getMessage() +
                    "\' from " + updatedTask.getCompany() +
                    " was successfully completed");
            updatedTask.setDateComplete(LocalDate.now());
        }

        updatedTask.setMessage(editMessage.getValue());
        updatedTask.setStatus(statusSelect.getValue());

        taskRepository.save(updatedTask);
        if (!isDone) {
            log.info("Task \'" + addedTask.getMessage() +
                    "\' from " + addedTask.getCompany() +
                    " has been edited");
        }

        Notification.show(
                "Task \'" + addedTask.getMessage() + "\' has been edited",
                2000,
                Notification.Position.TOP_END);

        changeHandler.onChange();
    }

    private void delete() {
        taskRepository.delete(addedTask);

        log.info("Task \'" + addedTask.getMessage() +
                "\' from " + addedTask.getCompany() +
                " has been deleted");

        Notification.show(
                "Task \'" + addedTask.getMessage() + "\' has been deleted",
                2000,
                Notification.Position.TOP_END);

        changeHandler.onChange();
    }

    public void editTask(Task task) {

        if (task == null) {
            setVisible(false);
            return;
        }

        if (task.getId() != null) {
            this.addedTask = taskRepository.findById(task.getId()).orElse(task);
        } else {
            this.addedTask = task;
        }
        editTaskBinder.setBean(task);

        Notification.show(
                "Task from " + addedTask.getCompany() + " was selected",
                2000,
                Notification.Position.TOP_END);

        setVisible(true);
        editMessage.focus();
    }

    public void setChangeHandler(ChangeHandler changeHandler) {
        this.changeHandler = changeHandler;
    }
}

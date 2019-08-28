package com.wizerdshins.tasksmanager.component;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.wizerdshins.tasksmanager.entity.Task;
import com.wizerdshins.tasksmanager.repository.CompanyRepository;
import com.wizerdshins.tasksmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class TaskEditor extends VerticalLayout implements KeyNotifier {

    private TaskRepository taskRepository;
//    private CompanyRepository companyRepository;

    private Task removeTask;
    private String status;

    private Button saveButton = new Button("Save");
    private Button deleteButton = new Button("Delete");
    private Button cancelButton = new Button("Cancel");

    private ComboBox<String> statusSelect = new ComboBox<>("Status", "open", "WIP", "done");

    private TextField editMessage = new TextField();

    private HorizontalLayout editFormLayout = new HorizontalLayout(
            editMessage, saveButton, deleteButton, cancelButton, statusSelect);

    /* TODO add more components */

    private Binder<Task> editTaskBinder = new Binder<>(Task.class);

    private ChangeHandler changeHandler;

    public interface ChangeHandler {
        void onChange();
    }


    @Autowired
    public TaskEditor(TaskRepository taskRepository) {

        this.taskRepository = taskRepository;

        add(editFormLayout);

        editTaskBinder.forField(editMessage)
                      .bind(Task::getMessage, Task::setMessage);
        editTaskBinder.forField(statusSelect)
                .bind(Task::getStatus, Task::setStatus);

        saveButton.getElement().getThemeList().add("primary");
        deleteButton.getElement().getThemeList().add("secondary");
        cancelButton.getElement().getThemeList().add("tertiary");

        addKeyPressListener(Key.ENTER, click -> save(removeTask));

        saveButton.addClickListener(click -> save(removeTask));
        deleteButton.addClickListener(click -> delete());
        cancelButton.addClickListener(click -> {
           setVisible(false);
        });

        setSpacing(true);
        setVisible(false);
    }

    private void save(Task updatedTask) {

        Task task = updatedTask;
        String oldStatus = updatedTask.getStatus();
        statusSelect.addValueChangeListener(event -> {
           if (event.getValue().isEmpty()) {
               status = oldStatus;
           } else {
               status = event.getValue();
           }
        });

        // TODO fix update status

        updatedTask.setMessage(editMessage.getValue());
        updatedTask.setStatus(status);

        taskRepository.save(task);
        Notification.show(
                "Task \'" + removeTask.getMessage() + "\' has been edited",
                2000,
                Notification.Position.TOP_END);

        changeHandler.onChange();
    }

    private void delete() {
        taskRepository.delete(removeTask);
        Notification.show(
                "Task \'" + removeTask.getMessage() + "\' has been deleted",
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
            this.removeTask = taskRepository.findById(task.getId()).orElse(task);
        } else {
            this.removeTask = task;
        }
        editTaskBinder.setBean(task);

        Notification.show(
                "Task from " + removeTask.getCompany() + " was selected",
                2000,
                Notification.Position.TOP_END);

        setVisible(true);
        editMessage.focus();
    }

    public void setChangeHandler(ChangeHandler changeHandler) {
        this.changeHandler = changeHandler;
    }
}

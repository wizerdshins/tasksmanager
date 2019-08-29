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
import com.wizerdshins.tasksmanager.repository.CompanyRepository;
import com.wizerdshins.tasksmanager.repository.TaskRepository;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Date;

@SpringComponent
@UIScope
public class TaskEditor extends VerticalLayout implements KeyNotifier {

    private TaskRepository taskRepository;
//    private CompanyRepository companyRepository;

    /* TODO fix Task name */

    private Task removeTask;
    private String status;

    private Button saveButton = new Button("Save");
    private Button deleteButton = new Button("Delete");
    private Button cancelButton = new Button("Cancel");

    private ComboBox<String> statusSelect = new ComboBox<>(
            "Status", "open", "WIP", "done");

    private TextField editMessage = new TextField("Message");

    private HorizontalLayout editFormLayout = new HorizontalLayout(
            editMessage, statusSelect);
    private HorizontalLayout buttonLayout = new HorizontalLayout(
            saveButton, deleteButton, cancelButton);

    private Div editFormWrapper = new Div(editFormLayout, buttonLayout);

    /* TODO add more components */

    private Binder<Task> editTaskBinder = new Binder<>(Task.class);

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

        if (status.equals("done")) {
            updatedTask.setDateComplete(LocalDate.now());
        }

        updatedTask.setMessage(editMessage.getValue());
        updatedTask.setStatus(statusSelect.getValue());

        // TODO fix date decrement bug

        taskRepository.save(updatedTask);
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

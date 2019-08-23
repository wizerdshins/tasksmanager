package com.wizerdshins.tasksmanager.component;

import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
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

    private Button saveButton = new Button("Delete");
    private TextField editMessage = new TextField();
    private HorizontalLayout editFormLayout = new HorizontalLayout(editMessage, saveButton);

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
//        editTaskBinder.bindInstanceFields(this);
        editTaskBinder.forField(editMessage)
                      .bind(Task::getMessage, Task::setMessage);

        saveButton.getElement().getThemeList().add("primary"); // wtf?
        saveButton.addClickListener(click -> delete());

        setSpacing(true);

        setVisible(false);
    }

    private void delete() {
        taskRepository.delete(removeTask);
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

        Notification.show("Task from " + removeTask.getCompany() + " was selected");

        setVisible(true);
        editMessage.focus();
    }

    public void setChangeHandler(ChangeHandler changeHandler) {
        this.changeHandler = changeHandler;
    }
}

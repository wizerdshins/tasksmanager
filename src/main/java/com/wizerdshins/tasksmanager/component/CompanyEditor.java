package com.wizerdshins.tasksmanager.component;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.wizerdshins.tasksmanager.entity.Company;
import com.wizerdshins.tasksmanager.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class CompanyEditor extends VerticalLayout implements KeyNotifier {

    private CompanyRepository companyRepository;
    private Company updatedCompany;

    private Button saveButton = new Button("Save");
    private Button deleteButton = new Button("Delete");
    private Button cancelButton = new Button("Cancel");

    private TextField companyNameField = new TextField();
    private TextField companyAddressField = new TextField();
    private TextField companyPhoneField = new TextField();

    private HorizontalLayout editCompanyFormLayout = new HorizontalLayout(
            companyNameField, companyAddressField, companyPhoneField, saveButton, deleteButton, cancelButton);

    private Binder<Company> editCompanyBinder = new Binder<>(Company.class);

    private ChangeHandler changeHandler;

    public interface ChangeHandler {
        void onChange();
    }

    @Autowired
    public CompanyEditor(CompanyRepository companyRepository) {

        this.companyRepository = companyRepository;
        add(editCompanyFormLayout);

        editCompanyBinder.forField(companyNameField)
                         .bind(Company::getName, Company::setName);

        editCompanyBinder.forField(companyAddressField)
                         .bind(Company::getAddress, Company::setAddress);

        editCompanyBinder.forField(companyPhoneField)
                         .bind(Company::getPhone, Company::setPhone);

        saveButton.getElement().getThemeList().add("primary");
        deleteButton.getElement().getThemeList().add("secondary");
        cancelButton.getElement().getThemeList().add("tertiary");

        addKeyPressListener(Key.ENTER, event -> save(updatedCompany));

        saveButton.addClickListener(click -> save(updatedCompany));
        deleteButton.addClickListener(click -> delete());
        cancelButton.addClickListener(click -> {
            setVisible(false);
        });

        setSpacing(true);
        setVisible(false);
    }

    private void save(Company newCompany) {

        Company company = newCompany;

        newCompany.setName(companyNameField.getValue());
        newCompany.setAddress(companyAddressField.getValue());
        newCompany.setPhone(companyPhoneField.getValue());
        companyRepository.save(company);

        Notification.show(
                "Company \'" + updatedCompany.getName() + "\' has been edited",
                2000,
                Notification.Position.TOP_END);

        changeHandler.onChange();
    }

    private void delete() {

        companyRepository.delete(updatedCompany);
        Notification.show(
                "Company \'" + updatedCompany.getName() + "\' has been deleted",
                2000,
                Notification.Position.TOP_END);

        changeHandler.onChange();
    }

    public void editCompany(Company company) {

        if (company == null) {
            setVisible(false);
            return;
        }

        if (company.getId() != null) {
            this.updatedCompany = companyRepository.findById(company.getId()).orElse(company);
        } else {
            this.updatedCompany = company;
        }
        editCompanyBinder.setBean(company);

        Notification.show(
                "Company named \'" + updatedCompany.getName() + "\' was selected",
                2000,
                Notification.Position.TOP_END);

        setVisible(true);
        companyNameField.focus();
    }


}

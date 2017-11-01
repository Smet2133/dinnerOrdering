package com.dinnerOrdering.ui.views;

import com.dinnerOrdering.ui.SecuredUI;
import com.dinnerOrdering.ui.forms.AdminForm;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SpringView
public class AdminView extends VerticalLayout implements View {
	public AdminForm getAdminForm() {
		return adminForm;
	}

	AdminForm adminForm;

	public AdminView(SecuredUI ui) {
		setMargin(true);
		adminForm = new AdminForm();
		addComponent(adminForm);
		setExpandRatio(adminForm, 1);

		Button logoutBtn = new Button("logout");
		logoutBtn.addClickListener(e -> ui.logout());
		addComponent(logoutBtn);
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		// NOP
	}
}

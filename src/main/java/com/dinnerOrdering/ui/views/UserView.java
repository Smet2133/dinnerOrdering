package com.dinnerOrdering.ui.views;

import com.dinnerOrdering.db.DbController;
import com.dinnerOrdering.entities.UserEntity;
import com.dinnerOrdering.ui.SecuredUI;
import com.dinnerOrdering.ui.forms.UserForm;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;


@SpringView(name = "") // Root view
public class UserView extends VerticalLayout implements View {

    //@Autowired
   // DbController db = new DbController();
   /* @Autowired
    DbController db;*/



    public UserView(SecuredUI ui, DbController db) {
        //setMargin(true);
        setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
     //   addComponent(new Label("View header in UserView: " + SecurityContextHolder.getContext().getAuthentication().getName()));
        UserEntity userEntity = db.getCurrentUserEntity();
        UserForm userForm = new UserForm(userEntity);
        addComponent(userForm);
        Button logoutBtn = new Button("logout");
        logoutBtn.addClickListener(e -> ui.logout());
        addComponent(logoutBtn);
       // addComponent(new Label(db.getMenuDataEntity().getSalads().get(0)));


    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // NOP
    }
}

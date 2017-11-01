package com.dinnerOrdering.ui;

import com.dinnerOrdering.db.DbController;
import com.dinnerOrdering.security.BackendService;
import com.dinnerOrdering.security.SecurityUtils;
import com.dinnerOrdering.ui.views.*;
import com.dinnerOrdering.ui.views.LoginForm;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.communication.PushMode;
//import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
/*
import ru.k0ba32.db.DbController;
import ru.k0ba32.entity.ModuleDataEntity;
import ru.k0ba32.security.BackendService;
import ru.k0ba32.security.SecurityUtils;
import ru.k0ba32.ui.custom.CustomMenu;
import ru.k0ba32.ui.views.menu.DataView;
import ru.k0ba32.ui.views.menu.PowerChartView;
import ru.k0ba32.ui.views.menu.SettingsView;
import ru.k0ba32.ui.views.menu.TemperatureChartView;
import ru.k0ba32.ui.views.secured.ErrorView;
*/

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

@SpringUI(path = "/")
//@Theme("mytheme")
public class SecuredUI extends UI {
    private static final String TITLE = "Dinner ordering";
    private static final String LOADING_TITLE = "Вход в панель";

    public AdminView getAdminView() {
        return adminView;
    }

    AdminView adminView;


    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    BackendService backendService;

    @Autowired
    ErrorView errorView;

    @Autowired
    SpringViewProvider viewProvider;
/*
    @Autowired
    CustomMenu layout;
    */

    @Autowired
    DbController db;


   // MainForm mainForm = new MainForm(this);

    @Override
    protected void init(VaadinRequest request) {
/*
        File errOut = new File("/opt/tomcat/webapps/outp.txt");
        File inTomcat = new File("/opt/tomcat/webapps/workbooks/workbook.xls");
        //f.getParentFile().mkdirs();
        try {
            errOut.getParentFile().mkdirs();
            inTomcat.getParentFile().mkdirs();
            errOut.createNewFile();
            inTomcat.createNewFile();
            //OutputStream output = new FileOutputStream(errOut);
            //PrintStream printOut = new PrintStream(output);
            //System.setOut(printOut);
        } catch (IOException e) {
            Notification.show("exc");
            Notification.show(e.toString());
            e.printStackTrace();

        }
        */

        getPage().setTitle(LOADING_TITLE);
        if (SecurityUtils.isLoggedIn()) {

            showMain();
        } else {
            showLogin();
        }
    }

    private void showLogin() {
        HorizontalLayout layoutForLogin = new HorizontalLayout();
        layoutForLogin.setSizeFull();
        layoutForLogin.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        layoutForLogin.addComponent(new LoginForm(this::login));
        setContent(layoutForLogin);
    }

    private void showMain() {
        getPage().setTitle(TITLE);

        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();

        /*
        Panel viewContainer = new Panel();
        viewContainer.setSizeFull();
        layout.addComponent(viewContainer);
        layout.setExpandRatio(viewContainer, 1.0f);
        */

        setContent(layout);
        setErrorHandler(this::handleError);

        //Navigator navigator = new Navigator(this, viewContainer);
        Navigator navigator = new Navigator(this, this);
        navigator.addProvider(viewProvider);
        navigator.setErrorView(errorView);
        viewProvider.setAccessDeniedViewClass(AccessDeniedView.class);

        adminView = new AdminView(this);
        navigator.addView("userView", new UserView(this,db));
        navigator.addView("adminView", adminView);

        if(SecurityUtils.hasRole("ROLE_ADMIN"))
            UI.getCurrent().getNavigator().navigateTo("adminView");
        else
            UI.getCurrent().getNavigator().navigateTo("userView");
        /*
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.setSizeFull();

        VerticalLayout leftButtonsLayout = new VerticalLayout();


        layout.addComponent();

        Panel viewContainer = new Panel();
        viewContainer.setSizeFull();
        layout.addComponent(viewContainer);
        layout.setExpandRatio(viewContainer, 1.0f);

        setContent(layout);

        layout.addComponent(new Label(SecurityContextHolder.getContext().getAuthentication().getCredentials().toString()));
*/
/*
        setErrorHandler(this::handleError);
        Navigator navigator = new Navigator(this, viewContainer);
        navigator.addProvider(viewProvider);
        navigator.setErrorView(errorView);
        viewProvider.setAccessDeniedViewClass(AccessDeniedView.class);
        */


/*      //VOVA
        Navigator navigator = new Navigator(this, layout.contentLayout());

        int moduleId = db.getModuleIdByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        List<ModuleDataEntity> list = db.getModuleDataById(moduleId);

        navigator.addView(TEMPERATURE_CHART_PATH, new TemperatureChartView(db, moduleId));
        navigator.addView(POWER_CHART_PATH, new PowerChartView(db, moduleId));
        navigator.addView(DATA_PATH, new DataView(db, moduleId));
        navigator.addView(SETTINGS_PATH, new SettingsView(db, moduleId));
        if (navigator.getState().isEmpty()) {
            doNavigate(TEMPERATURE_CHART_PATH, TEMPERATURE_CHART_TITLE);
        }
        else
            layout.viewTitle().setValue(RELOAD_DEFAULT_TITLE); //TODO reload bug

        navigator.addProvider(viewProvider);

        layout.conditionLabel().setContentMode(ContentMode.HTML);
        layout.conditionLabel().setValue(getCurrentState(moduleId));


        layout.temperatureButton().addClickListener(clickEvent -> {doNavigate(TEMPERATURE_CHART_PATH, TEMPERATURE_CHART_TITLE); layout.conditionLabel().setValue(getCurrentState(moduleId));});
        layout.powerButton().addClickListener(clickEvent -> {doNavigate(POWER_CHART_PATH, POWER_CHART_TITLE); layout.conditionLabel().setValue(getCurrentState(moduleId));});
        layout.dataButton().addClickListener(clickEvent -> {doNavigate(DATA_PATH, DATA_TITLE); layout.conditionLabel().setValue(getCurrentState(moduleId));});
        layout.settingsButton().addClickListener(clickEvent -> {doNavigate(SETTINGS_PATH, SETTINGS_TITLE); layout.conditionLabel().setValue(getCurrentState(moduleId));});

        setContent(layout);*/

    }

    private boolean login(String username, String password) {
        try {
            Authentication token = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));
            // Reinitialize the session to protect against session fixation attacks. This does not work
            // with websocket communication.
            VaadinService.reinitializeSession(VaadinService.getCurrentRequest());
            SecurityContextHolder.getContext().setAuthentication(token);
            // Now when the session is reinitialized, we can enable websocket communication. Or we could have just
            // used WEBSOCKET_XHR and skipped this step completely.
            getPushConfiguration().setTransport(Transport.WEBSOCKET);
            getPushConfiguration().setPushMode(PushMode.AUTOMATIC);
            // Show the main UI
            showMain();
            return true;
        } catch (AuthenticationException ex) {
            return false;
        }
    }

    public void logout() {
        getPage().reload();
        getSession().close();
    }

    //из демки, нужен для ограничения для ролей
    private void handleError(com.vaadin.server.ErrorEvent event) {
        Throwable t = DefaultErrorHandler.findRelevantThrowable(event.getThrowable());
        if (t instanceof AccessDeniedException) {
            Notification.show("You do not have permission to perform this operation",
                Notification.Type.WARNING_MESSAGE);
        } else {
            DefaultErrorHandler.doDefault(event);
        }
    }
/*
    private void doNavigate(String viewName, String viewTitle) {
        UI.getCurrent().getNavigator().navigateTo(viewName);
        layout.viewTitle().setValue(viewTitle);
    }

    private String getCurrentState(int moduleId) {

        Date lastDate = db.getModuleDateById(moduleId, true);
        if(java.util.concurrent.TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - lastDate.getTime() ) < db.getRequestPeriodById(moduleId))
            return "<font color=\"green\">⚫ Устройство включено</font> <br><font size=\"2\"> Обновлено: " + dateFormat.format(lastDate) + " </font>";
        else
            return "<font color=\"red\">⚫ Устройство неактивно</font> <br><font size=\"2\"> Обновлено: "  + dateFormat.format(lastDate) + " </font>";

    }
    */
}

package com.dinnerOrdering.security;

import com.vaadin.ui.UI;
import org.springframework.stereotype.Component;

/**
 * This demonstrates how you can control access to views.
 */
@Component
public class ViewAccessControl implements com.vaadin.spring.access.ViewAccessControl {

    @Override
    public boolean isAccessGranted(UI ui, String beanName) {
        if (beanName.equals("adminView")) {
            return SecurityUtils.hasRole("ROLE_ADMIN");
        } else {
            return SecurityUtils.hasRole("ROLE_USER");
        }
    }
}

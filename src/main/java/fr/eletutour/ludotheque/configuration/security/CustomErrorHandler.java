package fr.eletutour.ludotheque.configuration.security;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.ErrorHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomErrorHandler implements ErrorHandler {

    @Override
    public void error(com.vaadin.flow.server.ErrorEvent event) {
        UI.getCurrent().navigate("error");
    }
}

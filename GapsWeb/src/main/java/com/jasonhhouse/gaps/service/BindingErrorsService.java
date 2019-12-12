package com.jasonhhouse.gaps.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.ModelAndView;

@Service
public class BindingErrorsService {

    private final MessageSource messageSource;

    private final Logger logger = LoggerFactory.getLogger(BindingErrorsService.class);

    public BindingErrorsService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public boolean hasBindingErrors(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            for (Object object : bindingResult.getAllErrors()) {
                if (object instanceof FieldError) {
                    FieldError fieldError = (FieldError) object;
                    String message = messageSource.getMessage(fieldError, null);
                    logger.error(message);
                }
            }
            return true;
        }
        return false;
    }

    public ModelAndView getErrorPage() {
        return new ModelAndView("error");
    }
}

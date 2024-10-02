package com.dlshopping.shoppingcart.controller;

import jakarta.persistence.EntityNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.nio.file.AccessDeniedException;
import java.sql.SQLException;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(NoHandlerFoundException.class)
    public String handle404(NoHandlerFoundException ex, Model model) {
        model.addAttribute("message", "Page non trouvée !"+ ex.getMessage());
        return "errorpage";
    }


    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDenied(AccessDeniedException ex, Model model) {
        model.addAttribute("message", "Accès refusé !"+ ex.getMessage());
        return "errorpage";
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public String handleConstraintViolation(ConstraintViolationException ex, Model model) {
        model.addAttribute("message", "Violation de contrainte !"+ ex.getMessage());
        return "errorpage";
    }

    @ExceptionHandler(SQLException.class)
    public String handleSQLException(SQLException ex, Model model) {
        model.addAttribute("message", "Erreur de base de données !"+ ex.getMessage());
        return "errorpage";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException ex, Model model) {
        model.addAttribute("message", "Argument invalide! " + ex.getMessage());
        return "errorpage";
    }

    @ExceptionHandler(NullPointerException.class)
    public String handleNullPointerException(NullPointerException ex, Model model) {
        model.addAttribute("message", "Erreur de référence nulle."+ ex.getMessage());
        return "errorpage";
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public String handleException(Exception ex, Model model) {
        model.addAttribute("message", "Entité introuvable." + ex.getMessage());
        return "errorpage";
    }

    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception ex, Model model) {
        model.addAttribute("message", "Une erreur est survenue."+ ex.getMessage());
        return "errorpage";
    }

}

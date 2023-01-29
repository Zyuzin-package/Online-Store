package com.example.diplom.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ErrorControllerAdvice {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exception (Exception ex, Model model){
        String errorMessage = (ex !=null ? ex.getMessage() : "Unknown error");
        model.addAttribute("errorMessage",errorMessage);
        return "error";
    }
}

package br.com.oruspay.lambda.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @RequestMapping(value = "/spring", method = RequestMethod.GET)
    @ResponseBody
    public String hello() {
        return "Hello World ! PORRA !";
    }
}

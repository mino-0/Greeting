package com.example.Test;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@Controller
public class GreetingController {
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/greeting")
    @ResponseBody
    public String greeting(@RequestParam(value = "name",required = false,
            defaultValue = "World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }
    @GetMapping("/*")
    public String greeting1(@RequestParam(value = "name",required = false) String name, Model model) {
        model.addAttribute("name", null);
        return "greeting";
    }
}

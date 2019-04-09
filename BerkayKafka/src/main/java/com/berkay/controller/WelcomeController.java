package com.berkay.controller;

import com.berkay.Kafka.SampleConsumer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class WelcomeController {

    private int tasks = 100;
    private String merhaba = "merhaba";

    @GetMapping("/")
    public String main(Model model) {
        model.addAttribute("tasks", tasks);
        model.addAttribute("merhaba", merhaba);
        model.addAttribute("array", SampleConsumer.arrayForIter);
        model.addAttribute("istanbul", SampleConsumer.istCounter);
        model.addAttribute("moscow", SampleConsumer.mosCounter);
        model.addAttribute("beijing", SampleConsumer.beiCounter);
        model.addAttribute("tokyo", SampleConsumer.tokCounter);
        model.addAttribute("london", SampleConsumer.lonCounter);


        return "index"; //view
    }



}
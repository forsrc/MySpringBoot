package com.forsrc.boot.web.streaming.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.forsrc.boot.web.streaming.service.StreamingService;

@RestController
public class StreamingController {

    @Autowired
    private StreamingService service;

    @RequestMapping(path = "/api/batch/target", method = RequestMethod.GET)
    public ResponseEntity<List<String>> list() {
       
        return new ResponseEntity<>(service.in(), HttpStatus.OK);
    }


}

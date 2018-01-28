package com.forsrc.boot.web.streaming.service;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface StreamingService {

    public List<String> in();

    public void out(List<String> list);
}

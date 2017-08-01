package com.forsrc.boot.batch.rest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.forsrc.boot.batch.pojo.BatchTarget;

public class BatchTargetItemReader implements ItemReader<BatchTarget> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BatchTargetItemReader.class);
    private String url;

    private RestTemplate restTemplate;

    private List<BatchTarget> list;
    private int next;

    public BatchTargetItemReader(String url, RestTemplate restTemplate) {
        this.url = url;
        this.restTemplate = restTemplate;
    }

    @Override
    public BatchTarget read()
            throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

        if (list == null) {
            list = getList();
            next = 0;
        }
        BatchTarget item = null;
        if (next < list.size()) {
            item = list.get(next++);
            LOGGER.info("BatchTargetItemReader: {} --> {}", next - 1, item);
        }
        return item;
    }

    // @formatter:off
    private List<BatchTarget> getList() {
        ResponseEntity<BatchTarget[]> response = restTemplate.getForEntity(url, BatchTarget[].class);
        BatchTarget[] arr = response.getBody();
        List<BatchTarget> list = Arrays.asList(arr);
        list.stream().forEach(item -> {
            List<String> items = list.stream()
                    .filter(i -> i.getParentId().equals(item.getId()))
                    .collect(Collectors.toList())
                    .stream()
                    .map(j -> j.getId().toString())
                    .collect(Collectors.toList());
            
            item.setChildren(items.stream().collect(Collectors.joining(",")));
        });
        return Arrays.asList(arr);
    }
    // @formatter:on
}

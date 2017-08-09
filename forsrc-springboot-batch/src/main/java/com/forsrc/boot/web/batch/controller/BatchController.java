package com.forsrc.boot.web.batch.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.forsrc.boot.batch.pojo.BatchTarget;
import com.forsrc.boot.batch.rest.BatchTargetJobLauncher;

@RestController
public class BatchController {

    @RequestMapping(path = "/api/batch/target", method = RequestMethod.GET)
    public ResponseEntity<List<BatchTarget>> list() {
        List<BatchTarget> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            BatchTarget batchTarget = new BatchTarget(i * 1L, "B-" + i);
            batchTarget.setParentId(i / 3 * 1L);
            batchTarget.setStart(new Date());
            batchTarget.setEnd(new Date());
            list.add(batchTarget);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> run() {
        String rt = "OK";
        BatchTargetJobLauncher batchTargetJobLauncher = new BatchTargetJobLauncher();
        try {
            batchTargetJobLauncher.doMain();
        } catch (JobParametersInvalidException | JobExecutionAlreadyRunningException | JobRestartException
                | JobInstanceAlreadyCompleteException e) {
            rt = "NG";
            e.printStackTrace();
        }
        return new ResponseEntity<>(rt, HttpStatus.OK);
    }
}

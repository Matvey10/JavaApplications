package com.maga.saiapp.controller;

import com.maga.saiapp.dto.author.AuthorDto;
import com.maga.saiapp.dto.OperationResult;
import com.maga.saiapp.service.AuthorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/rest/author")
@Slf4j
public class AuthorController {
    @Resource
    private AuthorService authorService;

    @PostMapping("/add")
    public ResponseEntity<OperationResult> addAuthor(@RequestBody AuthorDto authorDto) {
        OperationResult operationResult = new OperationResult();
        try {
           authorService.addAuthor(authorDto);
           operationResult.setErrorCode("0");
           operationResult.setErrorMessage("Operation successful");
        } catch (Exception ex) {
            log.error("Error during author saving", ex);
            operationResult.setErrorCode("1");
            operationResult.setErrorMessage(ex.getMessage());
        }
        return new ResponseEntity<>(operationResult, HttpStatus.OK);
    }
}

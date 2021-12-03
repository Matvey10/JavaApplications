package com.maga.saiapp.controller;

import com.maga.saiapp.dto.development.DevelopmentDto;
import com.maga.saiapp.dto.development.FindDevelopmentResult;
import com.maga.saiapp.dto.development.FindDevelopmentsSearchFilter;
import com.maga.saiapp.dto.OperationResult;
import com.maga.saiapp.dto.development.FindSaiPropertiesResult;
import com.maga.saiapp.dto.development.FindTechnologiesResult;
import com.maga.saiapp.dto.development.SaiPropertyDto;
import com.maga.saiapp.dto.development.SearchDevelopmentsResult;
import com.maga.saiapp.dto.development.SimpleDevelopment;
import com.maga.saiapp.dto.development.TechnologyDto;
import com.maga.saiapp.service.DevelopmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping(value = "/rest/development")
@Slf4j
public class DevelopmentController {
    @Resource
    private DevelopmentService developmentService;

    @PostMapping("/add")
    public ResponseEntity<OperationResult> add(@RequestBody DevelopmentDto developmentDto) {
        OperationResult operationResult = new OperationResult();
        try {
            developmentService.addDevelopment(developmentDto);
            operationResult.setErrorCode("0");
            operationResult.setErrorMessage("Operation successful");
        } catch (Exception ex) {
            log.error("Error during development saving", ex);
            operationResult.setErrorCode("1");
            operationResult.setErrorMessage(ex.getMessage());
        }
        return new ResponseEntity<>(operationResult, HttpStatus.OK);
    }

    @GetMapping("/find")
    public ResponseEntity<FindDevelopmentResult> find(@RequestParam Long id) {
        FindDevelopmentResult findDevelopmentResult = new FindDevelopmentResult();
        try {
            findDevelopmentResult = developmentService.find(id);
            findDevelopmentResult.setErrorCode("0");
            findDevelopmentResult.setErrorMessage("Operation successful");
        } catch (Exception ex) {
            log.error("Error during finding development", ex);
            findDevelopmentResult.setErrorCode("1");
            findDevelopmentResult.setErrorMessage(ex.getMessage());
        }
        return new ResponseEntity<>(findDevelopmentResult, HttpStatus.OK);
    }

    @PostMapping("/find")
    public ResponseEntity<SearchDevelopmentsResult> findAll(@RequestBody FindDevelopmentsSearchFilter filter) {
        SearchDevelopmentsResult searchDevelopmentsResult = new SearchDevelopmentsResult();
        try {
            List<SimpleDevelopment> developments = developmentService.findAll(filter);
            searchDevelopmentsResult.setDevelopments(developments);
            searchDevelopmentsResult.setErrorCode("0");
            searchDevelopmentsResult.setErrorMessage("Operation successful");
        } catch (Exception ex) {
            log.error("Error during searching developments", ex);
            searchDevelopmentsResult.setErrorCode("1");
            searchDevelopmentsResult.setErrorMessage(ex.getMessage());
        }
        return new ResponseEntity<>(searchDevelopmentsResult, HttpStatus.OK);
    }

    @GetMapping(value = "/saiProperties/{id}")
    public ResponseEntity<FindSaiPropertiesResult> findProperty(@PathVariable Long id) {
        FindSaiPropertiesResult findSaiPropertiesResult = new FindSaiPropertiesResult();
        try {
            SaiPropertyDto saiPropertyDto = developmentService.findSaiProperty(id);
            findSaiPropertiesResult.setSaiPropertyDtos(List.of(saiPropertyDto));
            findSaiPropertiesResult.setErrorCode("0");
            findSaiPropertiesResult.setErrorMessage("Operation successful");
        } catch (Exception ex) {
            log.error("Error during searching saiProperty", ex);
            findSaiPropertiesResult.setErrorCode("1");
            findSaiPropertiesResult.setErrorMessage(ex.getMessage());
        }
        return new ResponseEntity<>(findSaiPropertiesResult, HttpStatus.OK);
    }

    @GetMapping(value = "/saiProperties")
    public ResponseEntity<FindSaiPropertiesResult> findProperties() {
        FindSaiPropertiesResult findSaiPropertiesResult = new FindSaiPropertiesResult();
        try {
            List<SaiPropertyDto> saiPropertyDtos = developmentService.findSaiProperties();
            findSaiPropertiesResult.setSaiPropertyDtos(saiPropertyDtos);
            findSaiPropertiesResult.setErrorCode("0");
            findSaiPropertiesResult.setErrorMessage("Operation successful");
        } catch (Exception ex) {
            log.error("Error during searching saiProperties", ex);
            findSaiPropertiesResult.setErrorCode("1");
            findSaiPropertiesResult.setErrorMessage(ex.getMessage());
        }
        return new ResponseEntity<>(findSaiPropertiesResult, HttpStatus.OK);
    }

    @GetMapping(value = "/technologies/{id}")
    public ResponseEntity<FindTechnologiesResult> findTechnology(@PathVariable Long id) {
        FindTechnologiesResult findTechnologiesResult = new FindTechnologiesResult();
        try {
            TechnologyDto technologyDto = developmentService.findTechnology(id);
            findTechnologiesResult.setTechnologyDtos(List.of(technologyDto));
            findTechnologiesResult.setErrorCode("0");
            findTechnologiesResult.setErrorMessage("Operation successful");
        } catch (Exception ex) {
            log.error("Error during searching technology", ex);
            findTechnologiesResult.setErrorCode("1");
            findTechnologiesResult.setErrorMessage(ex.getMessage());
        }
        return new ResponseEntity<>(findTechnologiesResult, HttpStatus.OK);
    }

    @GetMapping(value = "/technologies")
    public ResponseEntity<FindTechnologiesResult> findTechnologies() {
        FindTechnologiesResult findTechnologiesResult = new FindTechnologiesResult();
        try {
            List<TechnologyDto> technologyDtos = developmentService.findTechnologies();
            findTechnologiesResult.setTechnologyDtos(technologyDtos);
            findTechnologiesResult.setErrorCode("0");
            findTechnologiesResult.setErrorMessage("Operation successful");
        } catch (Exception ex) {
            log.error("Error during searching technologies", ex);
            findTechnologiesResult.setErrorCode("1");
            findTechnologiesResult.setErrorMessage(ex.getMessage());
        }
        return new ResponseEntity<>(findTechnologiesResult, HttpStatus.OK);
    }
}

//добавить разработку

//на странице разработки - все связанные статьи + переход на статью с ее инфой (добавить ее описание)
//+ все используемые технологии + переход на нее с описанием
//+ все saiProperties связанные с разработкой + описание

//Поиск
//поиск по имени, по году, по технологиям, по SaiProperties

//вкладка с saiProperties
//вкладка с технологиями

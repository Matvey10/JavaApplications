package org.example.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.RequestData;
import org.example.service.RService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.script.ScriptException;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class RController {
    @Resource
    private RService rService;

    @PostMapping("/statistic")
    public String getStatistics(@RequestBody RequestData requestData) throws ScriptException, FileNotFoundException, URISyntaxException, JsonProcessingException {
        List<Double> data = getDataFromRequest(requestData.getData());
        Map<String, Double> statistics = rService.getStatistics(data);
        ObjectMapper mapper = new ObjectMapper();
        String response = mapper.writeValueAsString(statistics);
        return response;
    }

    private List<Double> getDataFromRequest(String data) {
        return Arrays.asList(data.replaceAll(" ", "")
            .split(","))
            .stream().map(s -> Double.valueOf(s))
            .collect(Collectors.toList());
    }
}

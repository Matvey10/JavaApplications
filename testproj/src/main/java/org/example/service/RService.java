package org.example.service;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.renjin.eval.Context;
import org.renjin.script.RenjinScriptEngine;
import org.renjin.script.RenjinScriptEngineFactory;
import org.renjin.sexp.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.script.ScriptException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RService {
    private RenjinScriptEngine engine;
    private List<String> parameters;

    @PostConstruct
    public void init(){
        engine = new RenjinScriptEngineFactory().getScriptEngine();
        parameters = List.of("data_mean", "data_median", "data_var", "data_sd");
    }

    public Map<String, Double> getStatistics(List<Double> data) throws ScriptException, FileNotFoundException, URISyntaxException {
        URI uri = getClass().getClassLoader().getResource("statistics.R").toURI();
        FileReader fileReader = new FileReader(new File(uri));
        Double[] values = data.stream()
            .toArray(Double[]::new);
        engine.put("y", values);
        engine.eval(fileReader);

        Environment global = engine.getSession().getGlobalEnvironment();
        Context topContext = engine.getSession().getTopLevelContext();
        Map<String, Double> statistics = parameters.stream()
            .map(s -> new ImmutablePair<>(s, global.getVariable(topContext, s).asReal()))
            .collect(Collectors.toMap(ImmutablePair::getKey, ImmutablePair::getValue));
        return statistics;
    }
}

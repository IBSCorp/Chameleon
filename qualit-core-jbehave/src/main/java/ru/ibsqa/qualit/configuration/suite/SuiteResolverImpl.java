package ru.ibsqa.qualit.configuration.suite;

import ru.ibsqa.qualit.configuration.suites.ConfigLevelEnum;
import ru.ibsqa.qualit.configuration.suites.ISuiteConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class SuiteResolverImpl implements ISuiteResolver {

    private List<ISuiteConfig> suiteConfigs;

    @Autowired
    private void collectSuiteConfig(List<ISuiteConfig> suiteConfigs){
        this.suiteConfigs = suiteConfigs;
    }

    @Override
    public List<String> getSuites(){
        Map<ConfigLevelEnum, List<String>> configMap = new HashMap<>();
        for (ISuiteConfig suiteConfig: suiteConfigs){
            String suite = suiteConfig.getSuite();
            if (suite != null){
                List<String> suites = Arrays.asList(suite.split("\\s*,\\s*"));
                List<String> alreadySuits = configMap.get(suiteConfig.getLevel());
                if (null == alreadySuits) {
                    configMap.put(suiteConfig.getLevel(), suites);
                } else {
                    alreadySuits.addAll(suites);
                    configMap.put(suiteConfig.getLevel(), alreadySuits);
                }
            }
        }
        ConfigLevelEnum primaryLevel = null;
        for (ConfigLevelEnum level : configMap.keySet()) {
            if (null == primaryLevel || level.compareTo(primaryLevel) < 0) {
                primaryLevel = level;
            }
        }
        Optional<List<String>> result = Optional.ofNullable(configMap.get(primaryLevel));
        List<String> resultList = result.orElse(new ArrayList<String>());
        log.debug(String.format("List of suites level %s:", primaryLevel));
        resultList.stream().forEach(System.out::println);
        return resultList;
    }
}

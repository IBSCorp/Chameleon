package ru.ibsqa.qualit.runners;

import ru.ibsqa.qualit.configuration.suite.ISuiteResolver;
import ru.ibsqa.qualit.converters.IConverterCollector;

import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.embedder.StoryControls;
import org.jbehave.core.i18n.LocalizedKeywords;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.model.ExamplesTableFactory;
import org.jbehave.core.model.TableParsers;
import org.jbehave.core.model.TableTransformers;
import org.jbehave.core.reporters.Format;
import org.jbehave.core.reporters.FreemarkerViewGenerator;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.ParameterControls;
import org.jbehave.core.steps.ParameterConverters;
import org.jbehave.core.steps.spring.SpringStepsFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import ru.ibsqa.qualit.reporter.IJBehaveReporterManager;

import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static java.util.Arrays.asList;
import static org.jbehave.core.io.CodeLocations.codeLocationFromClass;

/**
 * Этот класс наследуют запускалки BDD тестов. В каждом модуле, где используется BDD присутствует класс-потомок.
 * Имя класса-потомка прописывается в pom в конфиге плагина maven-surefire-plugin, но обычно это TestRunner
 *
 * Пример:
 * import ru.ibsqa.qualit.runners.JBehaveRunner;
 * import org.junit.runner.RunWith;
 * import org.springframework.test.context.ContextConfiguration;
 * import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
 * @RunWith(SpringJUnit4ClassRunner.class)
 * @ContextConfiguration("classpath:config/spring.xml")
 * public class TestRunner extends JBehaveRunner {}
 *
 */
@Slf4j
public abstract class JBehaveRunner extends JUnitStories {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private ISuiteResolver suiteResolver;

    @Autowired
    private IJBehaveReporterManager jBehaveReporterManager;

    @Autowired
    private IConverterCollector converterCollector;

    public static Configuration configuration;

    private static Class thisClass;

    public static InputStream getResourceAsStream(String fileName) {
        return thisClass.getResourceAsStream(fileName);
    }

    public JBehaveRunner(){}

    @PostConstruct
    private void initialization() {
        thisClass = this.getClass();
        configuredEmbedder().embedderControls().doGenerateViewAfterStories(true).doIgnoreFailureInStories(true)
                .doIgnoreFailureInView(true).useStoryTimeouts(Long.toString(3600));
        configuredEmbedder().useMetaFilters(asList("-ignored true"));
    }

    @Override
    public Configuration configuration() {
        Configuration config = new MostUsefulConfiguration();
//      нужно для подключения Examples table из файла и использования трансформера, вычисляющего значения в ExamplesTable:

        ParameterConverters parameterConverters = new ParameterConverters().addConverters(converterCollector.getParameterConverters());

        //TODO добавить CustomStoryParser
        config.useStoryParser(new CustomStoryParser(new ExamplesTableFactory(
                new LocalizedKeywords(),
                new LoadFromClasspath(this.getClass()),
                parameterConverters,
                new ParameterControls().useDelimiterNamedParameters(true),
                new TableParsers(),
                new TableTransformers() {{useTransformer("EVALUATE", new EvaluateTableTransformer());}}
        )));
//      продолжаем выполнение теста для следующей строки таблицы примеров после фейла
        config.useStoryControls(new StoryControls().doResetStateBeforeScenario(true));
        config.useViewGenerator(new FreemarkerViewGenerator());
        config.useParameterConverters(parameterConverters);
        config.useStoryReporterBuilder(new StoryReporterBuilder().withReporters(jBehaveReporterManager.getReporters()).withFormats(Format.CONSOLE, Format.TXT,
                Format.XML, Format.HTML).withFailureTrace(true));
        config.storyReporterBuilder().viewResources().setProperty("encoding", "UTF-8");
        configuration = config;
        return config;
    }

    @Override
    public List<String> storyPaths() {
        String paths = null;
        paths = URLDecoder.decode(codeLocationFromClass(this.getClass()).getFile(), StandardCharsets.UTF_8);
        return new StoryFinder().findPaths(paths, suiteResolver.getSuites(), null);
    }

    @Override
    public InjectableStepsFactory stepsFactory() {
        return new SpringStepsFactory(configuration(), context);
    }


}

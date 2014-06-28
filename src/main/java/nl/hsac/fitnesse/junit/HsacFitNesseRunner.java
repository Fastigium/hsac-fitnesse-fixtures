package nl.hsac.fitnesse.junit;

import fitnesse.FitNesseContext;
import fitnesse.components.PluginsClassLoader;
import fitnesse.junit.FitNesseSuite;
import nl.hsac.fitnesse.junit.patchFor486.FitNesseRunner;
import org.apache.commons.lang3.StringUtils;
import org.junit.runners.model.InitializationError;

/**
 * JUnit Runner to run a FitNesse suite or page as JUnit test.
 *
 * The suite/page to run must be specified either via the Java property
 * 'fitnesseSuiteToRun', or by adding a FitNesseSuite.Name annotation to the test class.
 * If both are present the environment variable is used.
 *
 * The HTML generated for each page is saved in target/fitnesse-results
 */
public class HsacFitNesseRunner extends FitNesseRunner {
    private final static String suiteOverrideVariableName = "fitnesseSuiteToRun";

    public HsacFitNesseRunner(Class<?> suiteClass) throws InitializationError {
        super(suiteClass);
    }

    @Override
    protected String getSuiteName(Class<?> klass) throws InitializationError {
        String name = System.getProperty(suiteOverrideVariableName);
        if (StringUtils.isEmpty(name)) {
            FitNesseSuite.Name nameAnnotation = klass.getAnnotation(FitNesseSuite.Name.class);
            if (nameAnnotation == null) {
                throw new InitializationError("There must be a @Name annotation");
            }
            name = nameAnnotation.value();
        }
        return name;
    }

    @Override
    protected String getFitNesseDir(Class<?> suiteClass) throws Exception {
        return "wiki";
    }

    @Override
    protected String getOutputDir(Class<?> klass) throws InitializationError {
        return "target/fitnesse-results";
    }

    @Override
    protected FitNesseContext createContext(Class<?> suiteClass) throws Exception {
        new PluginsClassLoader(getFitNesseDir(suiteClass)).addPluginsToClassLoader();

        return super.createContext(suiteClass);
    }
}

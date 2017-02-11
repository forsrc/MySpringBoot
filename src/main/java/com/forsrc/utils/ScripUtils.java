package com.forsrc.utils;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * The type Scrip utils.
 */
public class ScripUtils {
    /**
     * Run rtn.
     *
     * @param <RTN>   the type parameter
     * @param name    the name
     * @param src     the src
     * @param adapter the adapter
     * @return the rtn
     * @throws IOException           the io exception
     * @throws ScriptException       the script exception
     * @throws NoSuchMethodException the no such method exception
     */
    public static <RTN> RTN run(ScripName name, String src, ScripUtilsAdapter adapter)
            throws IOException, ScriptException, NoSuchMethodException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName(name.getName());
        engine.eval(src);
        ScripConfig config = new ScripConfig();
        adapter.todo(engine, config);
        RTN rtn = null;
        if (engine instanceof Invocable) {
            Invocable invoke = (Invocable) engine;
            rtn = (RTN) invoke.invokeFunction(config.getFuncName(), config.getArgs());
        }
        return rtn;
    }

    /**
     * Run rtn.
     *
     * @param <RTN>   the type parameter
     * @param name    the name
     * @param src     the src
     * @param adapter the adapter
     * @return the rtn
     * @throws IOException           the io exception
     * @throws ScriptException       the script exception
     * @throws NoSuchMethodException the no such method exception
     */
    public static <RTN> RTN run(ScripName name, File src, ScripUtilsAdapter adapter)
            throws IOException, ScriptException, NoSuchMethodException {
        return run(name, FileUtils.getFileTxt(src, Charset.forName("UTF-8")), adapter);
    }

    /**
     * Run rtn.
     *
     * @param <RTN>    the type parameter
     * @param name     the name
     * @param src      the src
     * @param funcName the func name
     * @param args     the args
     * @return the rtn
     * @throws IOException           the io exception
     * @throws ScriptException       the script exception
     * @throws NoSuchMethodException the no such method exception
     */
    public static <RTN> RTN run(ScripName name, File src, final String funcName, final Object... args)
            throws IOException, ScriptException, NoSuchMethodException {

        return ScripUtils.run(ScripName.JAVASCRIPT,
                src,
                new ScripUtilsAdapter() {

                    @Override
                    public void todo(ScriptEngine scriptEngine, ScripConfig config) {
                        config.setFuncName(funcName);
                        config.setArgs(args);
                    }

                });
    }

    /**
     * The enum Scrip name.
     */
    public enum ScripName {
        /**
         * Javascript scrip name.
         */
        JAVASCRIPT("javascript"), /**
         * Groovy scrip name.
         */
        GROOVY("groovy");
        private String name;

        ScripName(String name) {
            this.name = name;
        }

        /**
         * Gets name.
         *
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * Sets name.
         *
         * @param name the name
         */
        public void setName(String name) {
            this.name = name;
        }
    }

    /**
     * The interface Scrip utils adapter.
     *
     * @param <RTN> the type parameter
     */
    public interface ScripUtilsAdapter<RTN> {
        /**
         * Todo.
         *
         * @param scriptEngine the script engine
         * @param config       the config
         */
        void todo(ScriptEngine scriptEngine, ScripConfig config);
    }

    /**
     * The type Scrip config.
     */
    public static class ScripConfig {
        private String funcName;
        private Object[] args;

        /**
         * Instantiates a new Scrip config.
         */
        public ScripConfig() {

        }

        /**
         * Instantiates a new Scrip config.
         *
         * @param funcName the func name
         * @param args     the args
         */
        public ScripConfig(String funcName, Object[] args) {
            super();
            this.funcName = funcName;
            this.args = args;
        }

        /**
         * Gets func name.
         *
         * @return the func name
         */
        public String getFuncName() {
            return this.funcName;
        }

        /**
         * Sets func name.
         *
         * @param funcName the func name
         */
        public void setFuncName(String funcName) {
            this.funcName = funcName;
        }

        /**
         * Get args object [ ].
         *
         * @return the object [ ]
         */
        public Object[] getArgs() {
            return this.args;
        }

        /**
         * Sets args.
         *
         * @param args the args
         */
        public void setArgs(Object[] args) {
            this.args = args;
        }

    }

}

package com.mamaspapas.api;

/**
 * Created by can on 20/08/16.
 */

import org.apache.log4j.Logger;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public abstract class AbstractApiTest
{
    private static final Logger logger = Logger.getLogger(AbstractApiTest.class);
    @Rule
    public TestRule testRule = new TestWatcher()
    {
        @Override
        protected void starting(Description description)
        {
            logger.info(" === TEST STARTED === " + description.getDisplayName());
        }

        @Override
        protected void succeeded(Description description)
        {
            logger.info(" === TEST PASSED === " + description.getDisplayName());
        }

        @Override
        protected void failed(Throwable e, Description description)
        {
            logger.error(" === TEST FAILED === " + description.getDisplayName());
        }

        @Override
        protected void finished(Description description)
        {
            logger.info(" === TEST FINISHED === " + description.getDisplayName() + "\n\n");
        }
    };
}

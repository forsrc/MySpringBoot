package com.forsrc.tools;


import org.springframework.context.MessageSource;

/**
 * The type My message source.
 */
public class MyMessageSource {
    private MessageSource MessageSource;

    private MyMessageSource() {

    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static MyMessageSource getInstance() {
        return MyMessageSourceClass.INSTANCE;
    }

    /**
     * Gets message source.
     *
     * @return the message source
     */
    public org.springframework.context.MessageSource getMessageSource() {
        return MessageSource;
    }

    /**
     * Sets message source.
     *
     * @param messageSource the message source
     */
    public void setMessageSource(org.springframework.context.MessageSource messageSource) {
        MessageSource = messageSource;
    }

    private static class MyMessageSourceClass {
        private static final MyMessageSource INSTANCE = new MyMessageSource();
    }
}

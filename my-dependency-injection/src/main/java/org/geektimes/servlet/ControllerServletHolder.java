package org.geektimes.servlet;

import org.eclipse.microprofile.config.Config;

/**
 * @author xubin
 * @date 2021/3/24 18:27
 */
public class ControllerServletHolder {

    private static final ThreadLocal<Config> CONFIG_THREAD_LOCAL = new InheritableThreadLocal();

    public static ThreadLocal<Config> getConfigThreadLocal() {
        return CONFIG_THREAD_LOCAL;
    }
}

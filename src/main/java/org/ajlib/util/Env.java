package org.ajlib.util;


import org.ajlib.Agent;

import java.io.File;

public class Env {

    public static String agentJarSource() {
        return System.getProperty(Agent.HOME);
    }

    public static File agentBaseDir() {
        return new File(agentJarSource()).getParentFile();
    }

}

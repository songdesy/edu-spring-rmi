package com.wssong.server;
import org.apache.catalina.Globals;
import org.apache.catalina.startup.Bootstrap;

/**
 * 设置Run Configurations... > Argument > VM arguments
 * -Xms256m -Xmx512m -XX:PermSize=64m -XX:MaxPermSize=256m -Dlikegene=true
 * 
 * @author lxt
 * @version 1.0
 * @since 2012-12-13
 */

public class WebServer
{
    public static void main(String[] args) throws Exception
    {
        String workDir = "webserver";
        if (args != null && args.length == 1)
            workDir = args[0];
        System.setProperty(Globals.CATALINA_BASE_PROP, workDir);
        
        System.setProperty("contextPath", "/");
        System.setProperty("docBase", "src/main/webapp");
        System.setProperty("classes.dir", "target/classes");
        System.setProperty("virtualClasspath", "../etc/lib/jars/*.jar");
        
        Bootstrap daemon = new Bootstrap();
        daemon.init();
        daemon.setAwait(true);
        daemon.start();
    }
}

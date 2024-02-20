package im.zhaojun.zfile;

import im.zhaojun.zfile.webdav.HTTPBasicAuthorizeAttribute;
import org.apache.catalina.Context;
import org.apache.catalina.servlets.WebdavServlet;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.SpringServletContainerInitializer;

import javax.servlet.ServletContainerInitializer;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * @author zhaojun
 */
@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
@EnableAsync
@EnableScheduling
@ServletComponentScan(basePackages = {"im.zhaojun.zfile.core.filter", "im.zhaojun.zfile.module.storage.filter","im.zhaojun.zfile.webdav"})
@ComponentScan(basePackages = "im.zhaojun.zfile.*")
public class ZfileApplication {

    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(ZfileApplication.class);
        builder.headless(false).run(args);
//        ConfigurableApplicationContext app = SpringApplication.run(ZfileApplication.class, args);
    }


//    @Bean
//    public TomcatServletWebServerFactory servletContainer() {
//        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
//            @Override
//            protected void postProcessContext(Context context) {
//                SecurityConstraint constraint = new SecurityConstraint();
//                constraint.setUserConstraint("CONFIDENTIAL");
//                SecurityCollection collection = new SecurityCollection();
//                collection.addPattern("/*");
//                constraint.addCollection(collection);
//                context.addConstraint(constraint);
//            }
//        };
//        tomcat.setDocumentRoot(new File("d:\\"));
//        //tomcat.addAdditionalTomcatConnectors(httpConnector());
//        return tomcat;
//    }
//
//    @Bean
//    public ServletRegistrationBean servletRegistrationBean() {
//        ServletRegistrationBean bean = new ServletRegistrationBean(new WebdavServlet(), "/webdav/*");
//        bean.addInitParameter("bbbController", "bbbController");
//        return bean;
//    }
//
//    @Bean
//    public FilterRegistrationBean filterRegistrationBean() {
//        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
//        HTTPBasicAuthorizeAttribute httpBasicFilter = new HTTPBasicAuthorizeAttribute();
//        registrationBean.setFilter(httpBasicFilter);
//        List<String> urlPatterns = new ArrayList<String>();
//        urlPatterns.add("/webdav/*");
//        registrationBean.setUrlPatterns(urlPatterns);
//        return registrationBean;
//    }
}
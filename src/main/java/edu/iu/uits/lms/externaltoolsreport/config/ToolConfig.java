package edu.iu.uits.lms.externaltoolsreport.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "external-tools-report")
@Getter
@Setter
public class ToolConfig {

   private String version;
   private String env;
   
   private String[] reportUsers;
}

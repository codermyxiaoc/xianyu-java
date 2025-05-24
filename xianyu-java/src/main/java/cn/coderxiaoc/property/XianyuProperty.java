package cn.coderxiaoc.property;


import lombok.Data;
import org.springframework.boot.admin.SpringApplicationAdminMXBeanRegistrar;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "xianyu")
@Component
public class XianyuProperty {
    private String cookieStr;
    private Boolean enableLog = true;
}

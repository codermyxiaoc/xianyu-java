package cn.coderxiaoc.config;

import cn.coderxiaoc.handler.MessageHandler;
import cn.coderxiaoc.main.XianyuMain;
import cn.coderxiaoc.property.XianyuProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class XianyuConfig {
    @Autowired
    private XianyuProperty xianyuProperty;
    @Autowired(required = false)
    MessageHandler handler;
    @Bean

    public XianyuMain xianyuMain() {
        if (xianyuProperty.getCookieStr() == null) {
            throw new RuntimeException("请配置cookie");
        }
        XianyuMain xianyuMain = new XianyuMain(xianyuProperty.getCookieStr(), handler);
        try {
            xianyuMain.connect();
            if (handler != null) {
                handler.setXianyuMain(xianyuMain);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return xianyuMain;
    }
}

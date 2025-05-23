package cn.coderxiaoc.config;

import cn.coderxiaoc.property.XianyuProperty;
import org.springframework.context.annotation.Import;

@Import({XianyuConfig.class, XianyuProperty.class})
public class XianyuAutoConfig {
}

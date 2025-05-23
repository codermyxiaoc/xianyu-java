package cn.coderxiaoc.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

@Data
public class Headers {
    @JSONField(name = "app-key")
    private String appKey;
    private String mid;
    private String ua;
    private String sid;
}

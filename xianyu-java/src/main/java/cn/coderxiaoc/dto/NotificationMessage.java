package cn.coderxiaoc.dto;


import com.alibaba.fastjson2.JSONObject;

public class NotificationMessage {
    private String cid;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    private String _platform;
    private BizTag bizTag;
    private String clientIp;
    private String detailNotice;
    private ExtJson extJson;
    private String port;
    private String reminderContent;
    private String reminderNotice;
    private String reminderTitle;
    private String reminderUrl;
    private String senderUserId;
    private String senderUserType;
    private String sessionType;
    private String umid;
    private String umidToken;
    private String utdid;

    // Getters and Setters
    public String get_platform() {
        return _platform;
    }

    public void set_platform(String _platform) {
        this._platform = _platform;
    }

    public BizTag getBizTag() {
        return bizTag;
    }

    public void setBizTag(BizTag bizTag) {
        this.bizTag = bizTag;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getDetailNotice() {
        return detailNotice;
    }

    public void setDetailNotice(String detailNotice) {
        this.detailNotice = detailNotice;
    }

    public ExtJson getExtJson() {
        return extJson;
    }

    public void setExtJson(ExtJson extJson) {
        this.extJson = extJson;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getReminderContent() {
        return reminderContent;
    }

    public void setReminderContent(String reminderContent) {
        this.reminderContent = reminderContent;
    }

    public String getReminderNotice() {
        return reminderNotice;
    }

    public void setReminderNotice(String reminderNotice) {
        this.reminderNotice = reminderNotice;
    }

    public String getReminderTitle() {
        return reminderTitle;
    }

    public void setReminderTitle(String reminderTitle) {
        this.reminderTitle = reminderTitle;
    }

    public String getReminderUrl() {
        return reminderUrl;
    }

    public void setReminderUrl(String reminderUrl) {
        this.reminderUrl = reminderUrl;
    }

    public String getSenderUserId() {
        return senderUserId;
    }

    public void setSenderUserId(String senderUserId) {
        this.senderUserId = senderUserId;
    }

    public String getSenderUserType() {
        return senderUserType;
    }

    public void setSenderUserType(String senderUserType) {
        this.senderUserType = senderUserType;
    }

    public String getSessionType() {
        return sessionType;
    }

    public void setSessionType(String sessionType) {
        this.sessionType = sessionType;
    }

    public String getUmid() {
        return umid;
    }

    public void setUmid(String umid) {
        this.umid = umid;
    }

    public String getUmidToken() {
        return umidToken;
    }

    public void setUmidToken(String umidToken) {
        this.umidToken = umidToken;
    }

    public String getUtdid() {
        return utdid;
    }

    public void setUtdid(String utdid) {
        this.utdid = utdid;
    }

    // Inner classes for nested JSON objects
    public static class BizTag {
        private String sourceId;
        private String messageId;

        // Getters and Setters
        public String getSourceId() {
            return sourceId;
        }

        public void setSourceId(String sourceId) {
            this.sourceId = sourceId;
        }

        public String getMessageId() {
            return messageId;
        }

        public void setMessageId(String messageId) {
            this.messageId = messageId;
        }
    }

    public static class ExtJson {
        private String quickReply;
        private String utdid;
        private String messageId;
        private String umidToken;
        private String tag;

        // Getters and Setters
        public String getQuickReply() {
            return quickReply;
        }

        public void setQuickReply(String quickReply) {
            this.quickReply = quickReply;
        }

        public String getUtdid() {
            return utdid;
        }

        public void setUtdid(String utdid) {
            this.utdid = utdid;
        }

        public String getMessageId() {
            return messageId;
        }

        public void setMessageId(String messageId) {
            this.messageId = messageId;
        }

        public String getUmidToken() {
            return umidToken;
        }

        public void setUmidToken(String umidToken) {
            this.umidToken = umidToken;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }
    }

    // Utility method to parse from JSON string
    public static NotificationMessage fromJson(String json) {
        NotificationMessage message = new NotificationMessage();
        JSONObject jsonObject = JSONObject.parseObject(json);

        message.set_platform(jsonObject.getString("_platform"));
        message.setClientIp(jsonObject.getString("clientIp"));
        message.setDetailNotice(jsonObject.getString("detailNotice"));
        message.setPort(jsonObject.getString("port"));
        message.setReminderContent(jsonObject.getString("reminderContent"));
        message.setReminderNotice(jsonObject.getString("reminderNotice"));
        message.setReminderTitle(jsonObject.getString("reminderTitle"));
        message.setReminderUrl(jsonObject.getString("reminderUrl"));
        message.setSenderUserId(jsonObject.getString("senderUserId"));
        message.setSenderUserType(jsonObject.getString("senderUserType"));
        message.setSessionType(jsonObject.getString("sessionType"));
        message.setUmid(jsonObject.getString("umid"));
        message.setUmidToken(jsonObject.getString("umidToken"));
        message.setUtdid(jsonObject.getString("utdid"));

        // Parse nested objects
        BizTag bizTag = new BizTag();
        JSONObject bizTagJson = JSONObject.parseObject(jsonObject.getString("bizTag"));
        bizTag.setSourceId(bizTagJson.getString("sourceId"));
        bizTag.setMessageId(bizTagJson.getString("messageId"));
        message.setBizTag(bizTag);

        ExtJson extJson = new ExtJson();
        JSONObject extJsonObj = JSONObject.parseObject(jsonObject.getString("extJson"));
        extJson.setQuickReply(extJsonObj.getString("quickReply"));
        extJson.setUtdid(extJsonObj.getString("utdid"));
        extJson.setMessageId(extJsonObj.getString("messageId"));
        extJson.setUmidToken(extJsonObj.getString("umidToken"));
        extJson.setTag(extJsonObj.getString("tag"));
        message.setExtJson(extJson);

        return message;
    }
}

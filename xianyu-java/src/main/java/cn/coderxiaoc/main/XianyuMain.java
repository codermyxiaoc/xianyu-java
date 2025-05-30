package cn.coderxiaoc.main;

import cn.coderxiaoc.api.XianyuApis;
import cn.coderxiaoc.dto.Headers;
import cn.coderxiaoc.dto.Message;
import cn.coderxiaoc.dto.NotificationMessage;
import cn.coderxiaoc.dto.TokenResponse;
import cn.coderxiaoc.handler.AbstractMessageHandler;
import cn.coderxiaoc.handler.MessageHandler;
import cn.coderxiaoc.script.XianyuScript;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class XianyuMain {

    private final String wsUrl = "wss://wss-goofish.dingtalk.com/";
    private final String cookieStr;
    private final Map<String, String> cookies;
    private final String myId;
    private final String deviceId;
    private WebSocketClient webSocketClient;
    private final XianyuApis xianyuApis;
    private final MessageHandler messageHandler;
    private final boolean log;


    public XianyuMain(String cookieStr, MessageHandler messageHandler, boolean log) {
        this.cookieStr = cookieStr;
        this.cookies = XianyuScript.transCookies(cookieStr);
        this.myId = cookies.get("unb");
        this.deviceId = XianyuScript.generateDeviceId(myId);
        this.xianyuApis = new XianyuApis();
        this.messageHandler = messageHandler;
        this.log = log;
    }

    public void connect() throws Exception {
        webSocketClient = new WebSocketClient(new URI(wsUrl)) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                System.out.println("✅ WebSocket Connected");
                try {
                    init();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                heartBeat();
            }

            @Override
            public void onMessage(String message) {
                if (log) {
                    System.out.println("📩 Message received: " + message);
                }
                handleIncomingMessage(message);
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                System.out.println("❌ WebSocket Closed: " + s);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        };

        webSocketClient.addHeader("Cookie", cookieStr);
        webSocketClient.addHeader("Host", "wss-goofish.dingtalk.com");
        webSocketClient.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 Chrome/133.0.0.0 Safari/537.36");
        webSocketClient.addHeader("Origin", "https://www.goofish.com");
        webSocketClient.connectBlocking();
    }

    private void init() throws Exception {
        TokenResponse token = xianyuApis.getToken(cookies, deviceId);

        JSONObject msg = new JSONObject();
        msg.put("lwp", "/reg");
        JSONObject headers = new JSONObject();
        headers.put("app-key", "444e9908a51d1cb236a27862abc769c9");
        headers.put("token", token.getData().getAccessToken());
        headers.put("ua", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) Chrome/133.0.0.0 Safari/537.36");
        headers.put("dt", "j");
        headers.put("wv", "im:3,au:3,sy:6");
        headers.put("did", deviceId);
        headers.put("mid", XianyuScript.generateMid());
        headers.put("cache-header", "app-key token ua wv");
        msg.put("headers", headers);

        webSocketClient.send(msg.toString());

        JSONObject syncMsg = new JSONObject();
        syncMsg.put("lwp", "/r/SyncStatus/ackDiff");
        headers = new JSONObject();
        headers.put("mid", XianyuScript.generateMid());
        syncMsg.put("headers", headers);
        JSONObject body = new JSONObject();
        long currentTime = System.currentTimeMillis();
        body.put("pipeline", "sync");
        body.put("tooLong2Tag", "PNM,1");
        body.put("channel", "sync");
        body.put("topic", "sync");
        body.put("highPts", 0);
        body.put("pts", currentTime * 1000);
        body.put("seq", 0);
        body.put("timestamp", currentTime);
        syncMsg.put("body", new JSONArray().put(body));

        webSocketClient.send(syncMsg.toString());
        System.out.println("Initialization completed");
    }

    private void heartBeat() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                JSONObject hb = new JSONObject();
                hb.put("lwp", "/!");
                JSONObject headers = new JSONObject();
                headers.put("mid", XianyuScript.generateMid());
                hb.put("headers", headers);
                webSocketClient.send(hb.toString());
                if (log) {
                    System.out.println("Heartbeat sent");
                }
            }
        }, 0, TimeUnit.SECONDS.toMillis(15));
    }

    private void handleIncomingMessage(String message) {
        try {
            Message messageData = com.alibaba.fastjson2.JSONObject.parseObject(message, Message.class);
            Headers headers = messageData.getHeaders();
            if (headers != null) {
                JSONObject ack = new JSONObject();
                ack.put("code", 200);
                JSONObject ackHeaders = new JSONObject();
                ackHeaders.put("mid", headers != null ? headers.getMid() : XianyuScript.generateMid());
                ackHeaders.put("sid", headers.getSid() != null ? headers.getSid() : "");
                ack.put("headers", ackHeaders);
                webSocketClient.send(ack.toString());
            }

            if (messageData.getBody() != null && messageData.getBody().getSyncPushPackage() != null &&
                    messageData.getBody().getSyncPushPackage().getData() != null &&  messageData.getBody().getSyncPushPackage().getData().size() > 0) {
                String decryptedData = XianyuScript.decrypt(messageData.getBody().getSyncPushPackage().getData().get(0).getData());

                if (messageHandler != null) {
                    messageDispatch(decryptedData);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void messageDispatch(String dataStr) {
        if (dataStr == null || dataStr.isEmpty()) {
            return;
        }
        messageHandler.allMessage(dataStr);
        JSONObject data = new JSONObject(dataStr);
        if (data.has("1")) {
            Object var1 = data.get("1");
            if (var1 instanceof String) {

            } else if (var1 instanceof JSONArray) {
                JSONArray varObj1 = data.getJSONArray("1");
                if (varObj1.get(0) instanceof JSONObject) {
                    if (varObj1.getJSONObject(0).getString("1").contains("@goofish")) {
                        messageHandler.userInput(varObj1.getJSONObject(0).getString("1").split("@")[0]);
                    }
                } else {
                    messageHandler.defaultMessage(dataStr);
                }
            } else if (var1 instanceof JSONObject) {
                JSONObject var11 = data.getJSONObject("1");
                NotificationMessage notificationMessage = NotificationMessage.fromJson(var11.getJSONObject("10").toString());
                notificationMessage.setCid(var11.getString("2").split("@")[0]);
                if (!notificationMessage.getSenderUserId().equals(myId)) {
                    if (var11.getInt("7") == 1) {
                        String var3 = var11.getJSONObject("6").getJSONObject("3").getString("2");
                        if (var3.equals("[我已拍下，待付款]")) {
                            messageHandler.obligation(notificationMessage);
                        } else if (var3.contains("[我已付款，等待你发货]")) {
                            messageHandler.userPay(notificationMessage);
                        } else if (var3.contains("[记得及时发货]")) {
                            messageHandler.remindOfShipment(notificationMessage);
                        } else if (var3.contains("[未付款，买家关闭了订单]")) {
                            messageHandler.userCloseTransaction(notificationMessage);
                        }else if (var3.contains("[我发起了退款申请]")) {
                            messageHandler.userUnPay(notificationMessage);
                        } else {
                            messageHandler.defaultMessage(dataStr);
                        }
                    } else if (var11.getInt("7") == 2) {
                        messageHandler.onMessage(notificationMessage);
                    }else {
                        messageHandler.defaultMessage(dataStr);
                    }
                } else {
                    messageHandler.myUserMessage(notificationMessage);
                }
            } else {
                messageHandler.defaultMessage(dataStr);
            }
        }
    }
    public void sendMsg(String cid, String toId, String text) {
        JSONObject textJson = new JSONObject();
        textJson.put("contentType", 1);
        textJson.put("text", new JSONObject().put("text", text));

        String textBase64 = Base64.getEncoder().encodeToString(textJson.toString().getBytes(StandardCharsets.UTF_8));

        JSONObject msg = new JSONObject();
        msg.put("lwp", "/r/MessageSend/sendByReceiverScope");
        JSONObject headers = new JSONObject();
        headers.put("mid", XianyuScript.generateMid());
        msg.put("headers", headers);
        JSONArray body = new JSONArray();
        JSONObject content = new JSONObject();
        content.put("uuid", XianyuScript.generateUuid());
        content.put("cid", cid + "@goofish");
        content.put("conversationType", 1);
        content.put("content", new JSONObject()
                .put("contentType", 101)
                .put("custom", new JSONObject().put("type", 1).put("data", textBase64))
        );
        content.put("ctx", new JSONObject().put("appVersion", "1.0").put("platform", "web"));
        body.put(content);
        body.put(new JSONObject().put("actualReceivers", new JSONArray().put(toId + "@goofish").put(myId + "@goofish")));
        msg.put("body", body);

        webSocketClient.send(msg.toString());
    }

}

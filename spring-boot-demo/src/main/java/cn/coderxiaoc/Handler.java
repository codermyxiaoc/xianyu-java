package cn.coderxiaoc;

import cn.coderxiaoc.dto.NotificationMessage;
import cn.coderxiaoc.handler.AbstractMessageHandler;
import org.springframework.stereotype.Component;

@Component
public class Handler extends AbstractMessageHandler {
    @Override
    public void oneMessage(NotificationMessage message) {
        this.xianyuMain.sendMsg(message.getCid(), message.getSenderUserId(), "hello");
        System.out.println("oneMessage: " + message.getReminderContent());
    }

    @Override
    public void userInput(String userId) {
        System.out.println("用户正在输入: " + userId);
    }

    @Override
    public void userPay(NotificationMessage message) {
        System.out.println("用户正在付款: " + message.getReminderContent());
    }

    @Override
    public void userUnPay(NotificationMessage message) {
        System.out.println("用户正在付款: " + message.getReminderContent());

    }

    @Override
    public void userCloseTransaction(NotificationMessage message) {
        System.out.println("用户关闭交易: " + message.getReminderContent());

    }

    @Override
    public void obligation(NotificationMessage message) {
        System.out.println("等待付款 " + message.getReminderContent());

    }

    @Override
    public void remindOfShipment(NotificationMessage message) {
        System.out.println("提醒发货 " + message.getReminderContent());
    }
}

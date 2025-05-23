package cn.coderxiaoc.handler;

import cn.coderxiaoc.dto.NotificationMessage;
import cn.coderxiaoc.main.XianyuMain;

public interface MessageHandler {
    public void setXianyuMain(XianyuMain xianyuMain);
    public void oneMessage(NotificationMessage message);
    public void userInput(String userId);
    public void userPay(NotificationMessage message);
    public void userUnPay(NotificationMessage message);
    public void userCloseTransaction(NotificationMessage message);
    public void obligation(NotificationMessage message);
    public void remindOfShipment(NotificationMessage message);
}

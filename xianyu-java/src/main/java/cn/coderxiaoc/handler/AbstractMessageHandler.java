package cn.coderxiaoc.handler;

import cn.coderxiaoc.main.XianyuMain;

public abstract class AbstractMessageHandler implements MessageHandler {
    protected XianyuMain xianyuMain;

    public void setXianyuMain(XianyuMain xianyuMain) {
        this.xianyuMain = xianyuMain;
    }
}

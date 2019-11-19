package com.qgailab.authsystem.constance;

/**
 * @author linxu
 * @date 2019/11/12
 * <tip>take care of yourself.everything is no in vain.</tip>
 * <p>
 *     嵌入式组的通信口令
 * </p>
 */
public enum Command {
    ACCPECT_OK("1"),
    ACCPECT_ERROR("2"),
    READY("ready"),
    LOAD("load"),
    UNAUTHORIZED("unauthorized"),
    ACK("ack");
    private final String command;

    Command(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}

package com.qgailab.authsystem.constance;


/**
 * @Description : 机器类型的枚举类
 * @Param :
 * @Return :
 * @Author : SheldonPeng
 * @Date : 2019-11-16
 */

public enum MachineType{

        SignatureMachine("SignatureMachine"),
        IdCardMachine("IdCardMachine"),
        FingerMachine("FingerMachine");


        private String machineType;
        MachineType(String machineType){
            this.machineType = machineType;
        }
        public String getMachineType(){
            return machineType;
        }
    }
package com.xilidou.framework.ioc.entity;

import javax.inject.Inject;

public class Robot {

    private Hand hand;

    private Mouth mouth;

    public Robot(Hand hand){
        this.hand = hand;
    }
    public void show(){

        hand.waveHand();
        mouth.speak();

    }

}

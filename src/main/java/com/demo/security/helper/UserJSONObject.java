package com.demo.security.helper;

public class UserJSONObject {
    private String username;
    private String clearName;
    private int challenge;
    private int clearChallenge;
    private int pubKey;
    private byte[] serializedGreeter;

    public byte[] getSerializedGreeter() {
        return serializedGreeter;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getClearName() {
        return clearName;
    }

    public void setClearName(String clearName) {
        this.clearName = clearName;
    }

    public void setSerializedGreeter(byte[] serializedGreeter) {
        this.serializedGreeter = serializedGreeter;
    }

    public int getChallenge() {
        return challenge;
    }

    public void setChallenge(int challenge) {
        this.challenge = challenge;
    }

    public int getClearChallenge() {
        return clearChallenge;
    }

    public void setClearChallenge(int clearChallenge) {
        this.clearChallenge = clearChallenge;
    }

    public int getPubKey() {
        return pubKey;
    }

    public void setPubKey(int pubKey) {
        this.pubKey = pubKey;
    }
}

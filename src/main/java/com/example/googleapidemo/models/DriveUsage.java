package com.example.googleapidemo.models;

import com.google.api.client.util.DateTime;

import java.util.function.Consumer;

/**
 * @author Mac Liu (linuzilla@gmail.com)
 */
public class DriveUsage {
    private final String account;
    private long mb;
    private DateTime lastUse;

    public DriveUsage(String account, Consumer<DriveUsage> consumer) {
        this.account = account;
        consumer.accept(this);
    }

    public String getAccount() {
        return account;
    }

    public long getMb() {
        return mb;
    }

    public void setMb(long mb) {
        this.mb = mb;
    }

    public DateTime getLastUse() {
        return lastUse;
    }

    public void setLastUse(DateTime lastUse) {
        this.lastUse = lastUse;
    }
}

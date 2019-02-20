package com.test.bank.model;

import javax.validation.constraints.NotNull;

public class TransferRequest {

    @NotNull
    private Integer fromUserId;

    @NotNull
    private Integer toUserId;

    @NotNull
    private Integer amount;

    public Integer getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Integer fromUserId) {
        this.fromUserId = fromUserId;
    }

    public Integer getToUserId() {
        return toUserId;
    }

    public void setToUserId(Integer toUserId) {
        this.toUserId = toUserId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}

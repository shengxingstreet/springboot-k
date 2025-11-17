package com.suke.wxk.entity;

import lombok.Data;

@Data
public class VoteOption {
    private String name; // "同意"或"不同意"
    private Integer votes; // 票数，默认0
    public VoteOption(String name,Integer votes)
    {
        this.name = name;
        this.votes = votes;
    }
}

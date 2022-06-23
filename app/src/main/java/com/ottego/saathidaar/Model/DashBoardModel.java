package com.ottego.saathidaar.Model;

public class DashBoardModel {

    public  String recent_visitors_count;
    public  String block_request_count;
    public  String accept_request_count;
    public  String sent_request_count;

    public DashBoardModel(String recent_visitors_count, String block_request_count, String accept_request_count, String sent_request_count) {
        this.recent_visitors_count = recent_visitors_count;
        this.block_request_count = block_request_count;
        this.accept_request_count = accept_request_count;
        this.sent_request_count = sent_request_count;
    }


}

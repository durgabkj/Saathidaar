package com.ottego.saathidaar.Model;

import java.util.List;

public class UpgradeModel {
    public  String plan_validity;
    public  String plan_price;
    public  String plan_id;
    public  String plan_name;

//    public  String features_id;
//    public  String features_name;
//    public  String features_valid;
    public List<PlanFeaturesModel> features;
}

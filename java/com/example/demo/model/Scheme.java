package com.example.demo.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "schemes")
public class Scheme {

    @Id
    private String id;
    private String schemeName;
    private String description;
    private int minAge;
    private int maxAge;
    private Double maxIncome;
    private String occupation;
    private String category;
    private String state;
    private String benefit;
    private String eligibility;
            

    public String getId(){
        return id;
    }
    public void setId(String id){
        this.id = id;
    }
    public String getSchemeName(){
        return schemeName;
    }
    public void setSchemeName(String schemeName){
        this.schemeName = schemeName;
    }
    public void setDescription(String description ){
        this.description = description;
    }
    public int getMinAge(){
        return minAge;
    }
    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public Double getMaxIncome() {
        return maxIncome;
    }

    public void setMaxIncome(Double maxIncome) {
        this.maxIncome = maxIncome;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getBenefit() {
        return benefit;
    }

    public void setBenefit(String benefit) {
        this.benefit = benefit;
    }
    public void setEligibility(String eligibility){
        this.eligibility=eligibility;
    }
    public String getElibility(){
        return eligibility;
    }
}
    


package com.example.ai.dto;

import java.util.List;

public class TravelPlanResponse {

    private String destination;
    private Integer days;
    private Double budget;
    private String preference;
    private String summary;
    private List<String> recommendations;
    private List<DailyPlan> itinerary;

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public String getPreference() {
        return preference;
    }

    public void setPreference(String preference) {
        this.preference = preference;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<String> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<String> recommendations) {
        this.recommendations = recommendations;
    }

    public List<DailyPlan> getItinerary() {
        return itinerary;
    }

    public void setItinerary(List<DailyPlan> itinerary) {
        this.itinerary = itinerary;
    }

    public static class DailyPlan {
        private Integer day;
        private String title;
        private String detail;

        public Integer getDay() {
            return day;
        }

        public void setDay(Integer day) {
            this.day = day;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }
    }
}

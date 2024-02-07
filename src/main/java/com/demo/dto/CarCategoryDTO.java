package com.demo.dto;

public class CarCategoryDTO {

    public enum TrunkSize{
        ONE_CASE,
        TWO_CASES,
        THREE_CASES,
        FOUR_CASES;

        public static TrunkSize fromString(String enumString) {
            switch (enumString.toUpperCase()) {
                case "TWO_CASES": return TWO_CASES;
                case "THREE_CASES": return THREE_CASES;
                case "FOUR_CASES": return FOUR_CASES;
                case "ONE_CASE":
                default:          return ONE_CASE;
            }
        }
    }
    private String description;
    private TrunkSize trunkSize;
    private int seats;
    private String exampleCar;
    private UserDTO.Role visibleTo;

    public CarCategoryDTO(){}

    public CarCategoryDTO(String description, TrunkSize trunkSize, int seats, String exampleCar, UserDTO.Role visibleTo) {
        this.description = description;
        this.trunkSize = trunkSize;
        this.seats = seats;
        this.exampleCar = exampleCar;
        this.visibleTo = visibleTo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TrunkSize getTrunkSize() {
        return trunkSize;
    }

    public void setTrunkSize(TrunkSize trunkSize) {
        this.trunkSize = trunkSize;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public String getExampleCar() {
        return exampleCar;
    }

    public void setExampleCar(String exampleCar) {
        this.exampleCar = exampleCar;
    }

    public UserDTO.Role getVisibleTo() {
        return visibleTo;
    }

    public void setVisibleTo(UserDTO.Role visibleTo) {
        this.visibleTo = visibleTo;
    }
}

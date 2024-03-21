package com.demo.dto;

/**
 * Typical and uninteresting DTO object
 */
public class CarDTO {
    private String manufacturer;
    private String modelName;
    private CarCategoryDTO belongsToCategory;


    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public CarCategoryDTO getBelongsToCategory() {
        return belongsToCategory;
    }

    public void setBelongsToCategory(CarCategoryDTO belongsToCategory) {
        this.belongsToCategory = belongsToCategory;
    }
}

package com.example.statshandler.model;

import com.example.models.dto.ProductDTO;

import java.time.LocalDate;
import java.util.List;

public class ProductVisitStatsDTO {
    private List<ProductDTO> productDTOList;
    private List<LocalDate> dateList;
    public ProductVisitStatsDTO(){

    }

    public ProductVisitStatsDTO(List<ProductDTO> productDTOList, List<LocalDate> dateList) {
        this.productDTOList = productDTOList;
        this.dateList = dateList;
    }

    public List<ProductDTO> getProductDTOList() {
        return productDTOList;
    }

    public void setProductDTOList(List<ProductDTO> productDTOList) {
        this.productDTOList = productDTOList;
    }

    public List<LocalDate> getDateList() {
        return dateList;
    }

    public void setDateList(List<LocalDate> dateList) {
        this.dateList = dateList;
    }
}

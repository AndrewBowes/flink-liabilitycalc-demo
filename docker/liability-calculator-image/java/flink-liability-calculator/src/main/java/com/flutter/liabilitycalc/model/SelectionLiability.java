package com.flutter.liabilitycalc.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class SelectionLiability {

    private String selectionId;
    private Double liability;

    public SelectionLiability(String selectionId, Double liability) {
        this.selectionId = selectionId;
        this.liability = liability;
    }

    public String getSelectionId() {
        return selectionId;
    }

    public Double getLiability() {
        return liability;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("selectionId", selectionId)
                .append("liability", liability)
                .toString();
    }
}

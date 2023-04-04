package com.flutter.liabilitycalc.flink;

import com.flutter.liabilitycalc.model.SelectionLiability;
import org.apache.flink.api.common.functions.ReduceFunction;

public class SelectionLiabilityReducer implements ReduceFunction<SelectionLiability> {
    @Override
    public SelectionLiability reduce(SelectionLiability t1, SelectionLiability t2) throws Exception {
        return new SelectionLiability(t1.getSelectionId(), t1.getLiability() + t2.getLiability());
    }
}

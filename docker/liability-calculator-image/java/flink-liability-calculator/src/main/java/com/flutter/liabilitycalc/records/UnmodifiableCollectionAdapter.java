package com.flutter.liabilitycalc.records;

import com.twitter.chill.java.UnmodifiableCollectionSerializer;
import org.apache.commons.compress.utils.Lists;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class UnmodifiableCollectionAdapter extends UnmodifiableCollectionSerializer {
    protected Collection<?> newInstance(Collection<?> var1) {
        List<Object> list = Lists.newArrayList();
        list.addAll(var1);
        return list;
    }
}

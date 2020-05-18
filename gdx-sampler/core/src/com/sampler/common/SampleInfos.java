package com.sampler.common;

import com.sampler.OrthoSample;
import com.sampler.SpriteBatchSample;
import com.sampler.ViewportSample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by goran on 20/08/2016.
 */
public class SampleInfos {

    public static final List<SampleInfo> ALL = Arrays.asList(
            OrthoSample.INFO,
            ViewportSample.INFO,
            SpriteBatchSample.INFO
    );

    private SampleInfos() {
    }

    public static List<String> getSampleNames() {
        List<String> ret = new ArrayList<>();

        for (SampleInfo info : ALL) {
            ret.add(info.getName());
        }

        Collections.sort(ret);
        return ret;
    }

    public static SampleInfo find(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("name argument is required.");
        }

        SampleInfo ret = null;

        for (SampleInfo info : ALL) {
            // IMPORTANT!
            // == operator compares references not string content
            // equals method compares objects for equality in case of strings that is content
            if (info.getName().equals(name)) {
                ret = info;
                break;
            }
        }

        if (ret == null) {
            throw new IllegalArgumentException("Could not find sample with name= " + name);
        }

        return ret;
    }
}

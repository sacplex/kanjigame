package com.tgd.kanjigame.math;

import java.util.Random;

public class ComplexMath
{
    final static public Random RANDOM = new Random(System.currentTimeMillis());

    static public double nextSkewedBoundedDouble(double min, double max, double skew, double bias)
    {
        double range = max - min;
        double mid = min + range / 2.0;
        double unitGaussian = RANDOM.nextGaussian();
        double biasFactor = Math.exp(bias);
        double result = mid+(range*(biasFactor/(biasFactor+Math.exp(-unitGaussian/skew))-0.5));

        return result;
    }
}

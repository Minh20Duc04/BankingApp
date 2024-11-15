package com.BankingApplication.Util;

public class RandomUtil {

    public static Long generateRandom(int length)
    {
        StringBuilder sb = new StringBuilder(length);
        for(int i = 0; i < length; ++i)
        {
            int digit = (int) (Math.random() * 10);
            sb.append(digit);
        }
        return Long.parseLong(sb.toString());
    }

}

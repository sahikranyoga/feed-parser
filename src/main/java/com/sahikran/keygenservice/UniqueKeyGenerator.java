package com.sahikran.keygenservice;

/**
 * Generates a unique key based on epoch time + an incrementing sequence number
 * Currently, the rate at which the feeds are generated is 60 feeds per second of maximum.
 * So, a call to generate key method need to generate 60 keys, 
 * post which the method resets the auto inc number and generates new set of 60 keys
 */
public interface UniqueKeyGenerator {
    long generateUniqueKey();
}

package com.sahikran.hasher;

public final class StringHashImpl implements Hash<String> {

    public static final int POLYNOMIAL = 31;
    public StringHashImpl(){

    }
    /**
     * This generates a hash value based on hashing function.
     * Referenced by Rabin Karp Algorith
     * @param t input string to be hashed
     */
    @Override
    public long generateHash(String t) {
        if(t == null){
            return 0L;
        }

        int x = StringHashImpl.POLYNOMIAL;
        long hash = 0L;
        
        for(int i = 0; i < t.length(); i++){
            hash = hash * x + t.charAt(i); 
        }
        return hash;
    }
    
}

package com.agilemaple.utils;

public class Base62 {

        public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        public static final int BASE62 = ALPHABET.length();
        /**
         * Returns the base 62 string of a long.
         * @param a long.
         * @return the base 62 string of a long
         */
        public static String toBase62(long value) {
            final StringBuilder sb = new StringBuilder(1);
            do {
                sb.insert(0, ALPHABET.charAt((int) (value % BASE62)));
                value /= BASE62;
            } while (value > 0);
            return sb.toString();
        }

        /**
         * Returns the base 62 value of a string.
         * @param a string.
         * @return the base 62 value of a string.
         */
        public static long toBase10(String str) {
            return toBase10(new StringBuilder(str).reverse().toString().toCharArray());
        }

        private static long toBase10(char[] chars) {
            long result = 0;
            for (int i = chars.length - 1; i >= 0; i--) {
                result += toBase10(ALPHABET.indexOf(chars[i]), i);
            }
            return result;
        }

        private static long toBase10(long result, int pow) {
            return result * (int) Math.pow(BASE62, pow);
        }
}

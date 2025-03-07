package com.google.zxing.client.result;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import java.util.regex.Pattern;

public final class VINResultParser extends ResultParser {
    private static final Pattern AZ09 = Pattern.compile("[A-Z0-9]{17}");
    private static final Pattern IOQ = Pattern.compile("[IOQ]");

    public VINParsedResult parse(Result result) {
        String wmi;
        if (result.getBarcodeFormat() != BarcodeFormat.CODE_39) {
            return null;
        }
        String rawText = IOQ.matcher(result.getText()).replaceAll("").trim();
        if (!AZ09.matcher(rawText).matches()) {
            return null;
        }
        try {
            if (!checkChecksum(rawText)) {
                return null;
            }
            wmi = rawText.substring(0, 3);
            try {
                return new VINParsedResult(rawText, wmi, rawText.substring(3, 9), rawText.substring(9, 17), countryCode(wmi), rawText.substring(3, 8), modelYear(rawText.charAt(9)), rawText.charAt(10), rawText.substring(11));
            } catch (IllegalArgumentException e) {
                String str = wmi;
                return null;
            }
        } catch (IllegalArgumentException e2) {
            wmi = null;
            String str2 = wmi;
            return null;
        }
    }

    private static boolean checkChecksum(CharSequence vin) {
        int sum = 0;
        for (int i = 0; i < vin.length(); i++) {
            sum += vinPositionWeight(i + 1) * vinCharValue(vin.charAt(i));
        }
        return vin.charAt(8) == checkChar(sum % 11);
    }

    private static int vinCharValue(char c) {
        if (c >= 'A' && c <= 'I') {
            return (c - 'A') + 1;
        }
        if (c >= 'J' && c <= 'R') {
            return (c - 'J') + 1;
        }
        if (c >= 'S' && c <= 'Z') {
            return (c - 'S') + 2;
        }
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        throw new IllegalArgumentException();
    }

    private static int vinPositionWeight(int position) {
        if (position > 0 && position <= 7) {
            return 9 - position;
        }
        if (position == 8) {
            return 10;
        }
        if (position == 9) {
            return 0;
        }
        if (position >= 10 && position <= 17) {
            return 19 - position;
        }
        throw new IllegalArgumentException();
    }

    private static char checkChar(int remainder) {
        if (remainder < 10) {
            return (char) (remainder + 48);
        }
        if (remainder == 10) {
            return 'X';
        }
        throw new IllegalArgumentException();
    }

    private static int modelYear(char c) {
        if (c >= 'E' && c <= 'H') {
            return (c - 'E') + 1984;
        }
        if (c >= 'J' && c <= 'N') {
            return (c - 'J') + 1988;
        }
        if (c == 'P') {
            return 1993;
        }
        if (c >= 'R' && c <= 'T') {
            return (c - 'R') + 1994;
        }
        if (c >= 'V' && c <= 'Y') {
            return (c - 'V') + 1997;
        }
        if (c >= '1' && c <= '9') {
            return (c - '1') + 2001;
        }
        if (c >= 'A' && c <= 'D') {
            return (c - 'A') + 2010;
        }
        throw new IllegalArgumentException();
    }

    private static String countryCode(CharSequence wmi) {
        char c1 = wmi.charAt(0);
        char c2 = wmi.charAt(1);
        switch (c1) {
            case '1':
            case '4':
            case '5':
                return "US";
            case '2':
                return "CA";
            case '3':
                if (c2 < 'A' || c2 > 'W') {
                    return null;
                }
                return "MX";
            case '9':
                if (c2 >= 'A' && c2 <= 'E') {
                    return "BR";
                }
                if (c2 < '3' || c2 > '9') {
                    return null;
                }
                return "BR";
            case 'J':
                if (c2 < 'A' || c2 > 'T') {
                    return null;
                }
                return "JP";
            case 'K':
                if (c2 < 'L' || c2 > 'R') {
                    return null;
                }
                return "KO";
            case 'L':
                return "CN";
            case 'M':
                if (c2 < 'A' || c2 > 'E') {
                    return null;
                }
                return "IN";
            case 'S':
                if (c2 >= 'A' && c2 <= 'M') {
                    return "UK";
                }
                if (c2 < 'N' || c2 > 'T') {
                    return null;
                }
                return "DE";
            case 'V':
                if (c2 >= 'F' && c2 <= 'R') {
                    return "FR";
                }
                if (c2 < 'S' || c2 > 'W') {
                    return null;
                }
                return "ES";
            case 'W':
                return "DE";
            case 'X':
                if (c2 == '0') {
                    return "RU";
                }
                if (c2 < '3' || c2 > '9') {
                    return null;
                }
                return "RU";
            case 'Z':
                if (c2 < 'A' || c2 > 'R') {
                    return null;
                }
                return "IT";
            default:
                return null;
        }
    }
}

package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitArray;
import java.util.EnumMap;
import java.util.Map;

final class UPCEANExtension2Support {
    private final int[] decodeMiddleCounters = new int[4];
    private final StringBuilder decodeRowStringBuffer = new StringBuilder();

    UPCEANExtension2Support() {
    }

    /* access modifiers changed from: package-private */
    public Result decodeRow(int rowNumber, BitArray row, int[] extensionStartRange) throws NotFoundException {
        StringBuilder sb = this.decodeRowStringBuffer;
        StringBuilder result = sb;
        sb.setLength(0);
        int end = decodeMiddle(row, extensionStartRange, result);
        String sb2 = result.toString();
        String resultString = sb2;
        Map<ResultMetadataType, Object> extensionData = parseExtensionString(sb2);
        Result extensionResult = new Result(resultString, (byte[]) null, new ResultPoint[]{new ResultPoint(((float) (extensionStartRange[0] + extensionStartRange[1])) / 2.0f, (float) rowNumber), new ResultPoint((float) end, (float) rowNumber)}, BarcodeFormat.UPC_EAN_EXTENSION);
        if (extensionData != null) {
            extensionResult.putAllMetadata(extensionData);
        }
        return extensionResult;
    }

    private int decodeMiddle(BitArray row, int[] startRange, StringBuilder resultString) throws NotFoundException {
        int[] iArr = this.decodeMiddleCounters;
        int[] counters = iArr;
        iArr[0] = 0;
        counters[1] = 0;
        counters[2] = 0;
        counters[3] = 0;
        int end = row.getSize();
        int rowOffset = startRange[1];
        int checkParity = 0;
        for (int x = 0; x < 2 && rowOffset < end; x++) {
            int bestMatch = UPCEANReader.decodeDigit(row, counters, rowOffset, UPCEANReader.L_AND_G_PATTERNS);
            resultString.append((char) ((bestMatch % 10) + 48));
            for (int counter : counters) {
                rowOffset += counter;
            }
            if (bestMatch >= 10) {
                checkParity |= 1 << (1 - x);
            }
            if (x != 1) {
                rowOffset = row.getNextUnset(row.getNextSet(rowOffset));
            }
        }
        if (resultString.length() != 2) {
            throw NotFoundException.getNotFoundInstance();
        } else if (Integer.parseInt(resultString.toString()) % 4 == checkParity) {
            return rowOffset;
        } else {
            throw NotFoundException.getNotFoundInstance();
        }
    }

    private static Map<ResultMetadataType, Object> parseExtensionString(String raw) {
        if (raw.length() != 2) {
            return null;
        }
        Map<ResultMetadataType, Object> enumMap = new EnumMap<>(ResultMetadataType.class);
        Map<ResultMetadataType, Object> result = enumMap;
        enumMap.put(ResultMetadataType.ISSUE_NUMBER, Integer.valueOf(raw));
        return result;
    }
}

package com.gussoft.customer.core.utils;

public class Util {

  public Util() {}

  public static String maskSensitiveData(String data, int visibleLength) {
    if (data == null || data.isEmpty()) {
      return "";
    }
    int length = data.length();
    if (length <= visibleLength) {
      return data;
    }
    int maskedLength = length - visibleLength;
    return "*".repeat(Math.max(0, maskedLength)) +
      data.substring(maskedLength);
  }
}

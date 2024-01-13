package com.springsecurity.util;

import com.springsecurity.constant.Constant;
import java.util.ResourceBundle;
import lombok.experimental.UtilityClass;
import org.springframework.context.i18n.LocaleContextHolder;

@UtilityClass
public class I18nMessageUtil {
    public static String getLocalizedMessage(String message) {
        return ResourceBundle
                .getBundle(Constant.I18N, LocaleContextHolder.getLocale())
                .getString(message);
    }
}

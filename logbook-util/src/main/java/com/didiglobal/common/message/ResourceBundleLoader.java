package com.didiglobal.common.message;

import java.util.Locale;
import java.util.ResourceBundle;

public interface ResourceBundleLoader {

    ResourceBundle loadResourceBundle(Locale locale);
}

package org.smartly.application.desktopgap.impl.app.applications.window.javascript.tools.i18n;

import org.smartly.application.desktopgap.impl.app.applications.window.AppLocalization;
import org.smartly.application.desktopgap.impl.app.applications.window.frame.AppFrame;
import org.smartly.commons.util.LocaleUtils;
import org.smartly.commons.util.StringUtils;

/**
 *
 */
public class ToolI18n {

    private final AppLocalization _i18n;
    private final String _lang;

    public ToolI18n(final AppFrame frame) {
        _i18n = frame.getApp().getI18n();
        _lang = LocaleUtils.getCurrent().getLanguage();
    }

    public String get(final String name_key) {
        return this.get(_lang, name_key);
    }

    public String get(final String param1, final String param2) {
        if (param2.contains(".")) {
            // param1 is LANG
            // split dictionaryName and key
            final String[] tokens = StringUtils.split(param2, ".");
            if (tokens.length == 2) {
                return this.get(param1, tokens[0], tokens[1]);
            }
        }
        return this.get(_lang, param1, param2);
    }

    public String get(final String lang, final String dictionaryName, final String key) {
        final String result = _i18n.get(StringUtils.hasText(lang) ? lang : _lang,
                dictionaryName, key);
        return result;
    }

    // ------------------------------------------------------------------------
    //                      p r i v a t e
    // ------------------------------------------------------------------------

}

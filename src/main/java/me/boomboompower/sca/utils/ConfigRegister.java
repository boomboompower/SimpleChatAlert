package me.boomboompower.sca.utils;

/*
* Made for SimpleChatAlert
* by boomboompower 18/05/2016
*/

import me.boomboompower.sca.SimpleChatAlert;

import static me.boomboompower.sca.utils.Utils.translate;

/**
 * The Utils class for getting things from the SimpleChatAlert config.
 *
 * @version 1.0
 * @author boomboompower
 */
public class ConfigRegister {

    private SimpleChatAlert simpleChatAlert;

    /**
     * Initializes the private simpleChatAlert variable.
     *
     * @param simpleChatAlert main class for the plugin.
     */
    public ConfigRegister(SimpleChatAlert simpleChatAlert) {
        this.simpleChatAlert = simpleChatAlert;
    }

    /**
     * Gets the deny message from the config with the colors translated.
     *
     * @return The deny message set by the user.
     */
    public String getDenyMessage() {
        return translate(getString("DenyMessage"));
    }

    /**
     * Gets the main format from the config with the colors translated.
     *
     * @return The main alert format set by the user.
     */
    public String getAlertFormat() {
        return translate(getString("Message"));
    }

    /**
     * Gets the title top format from the config with the colors translated.
     *
     * @return The title format from config.
     */
    public String getTitleTopFormat() {
        return translate(getString("Top"));
    }

    /**
     * Gets the subtitle format from the config with the colors translated.
     *
     * @return The subtitle format from config.
     * @see #isHeaderFooterEnabled() isHeaderFooterEnabled for if this is used.
     */
    public String getTitleBottomFormat() {
        return translate(getString("Bottom"));
    }

    /**
     * Gets the PlayerList header format from the config with the colors translated.
     *
     * @return The PlayerList header format from config.
     * @see #isHeaderFooterEnabled() isHeaderFooterEnabled for if this is used.
     */
    public String getHeaderFormat() {
        return translate(getString("Header"));
    }

    /**
     * Gets the PlayerList footer format from the config with the colors translated.
     *
     * @return The PlayerList footer format from config.
     */
    public String getFooterFormat() {
        return translate(getString("Footer"));
    }

    /**
     * Gets the format for if no arguments are entered for the alert command.
     *
     * @return The format for when no arguments are given
     */
    public String getNoMessageFormat() {
        return translate(getString("NoMessage"));
    }

    /**
     * Gets a boolean from config for if the title alert is enabled.
     *
     * @return boolean for if title alert is enabled.
     */
    public boolean isChatEnabled() {
        return getBoolean("ChatAlert");
    }

    /**
     * Gets a boolean from config for if the actionbar is enabled.
     *
     * @return boolean for if actionbar is enabled
     */
    public boolean isActionBarEnabled() {
        return getBoolean("ActionBar");
    }

    /**
     * Gets a boolean from config for if the title is enabled
     *
     * @return boolean for if title is enabled
     */
    public boolean isTitleEnabled() {
        return getBoolean("Title");
    }

    /**
     * Gets a boolean from config for if PlayerList header/footer is enabled.
     *
     * @return boolean for if PlayerList header/footer is enabled.
     * @see #getTitleTopFormat() getTitleTopFormat Format for the title.
     * @see #getTitleBottomFormat() getTitleTopFormat Format for the subtitle.
     */
    public boolean isHeaderFooterEnabled() {
        return getBoolean("PlayerList");
    }

    /**
     * Gets a boolean from config for if metrics should be enabled.
     *
     * @return boolean for if metrics is enabled.
     * @see #getHeaderFormat() for the header format.
     * @see #getFooterFormat() for the footer format.
     */
    public boolean isMetricsEnabled() {
        return getBoolean("Metrics");
    }

    /**
     * Gets a boolean from config for if the PlayerList header/footer should be enabled.
     *
     * @return boolean for if the PlayerList header/footer should be enabled.
     * @see #getHeaderFooterDisableTime() for time in ticks if enabled.
     */
    public boolean isHeaderFooterDisableable() {
        return getBoolean("Disable");
    }

    /**
     * Gets the time in ticks for when to disable the header/footer alert.
     *
     * @return The Disable time for the header/footer
     * @see #isHeaderFooterDisableable() isHeaderFooterDisableable for if this is used.
     */
    public int getHeaderFooterDisableTime() {
        return getInt("DisableTime");
    }

    /**
     * Gets the fadeIn time for titles from the config.
     *
     * @return the fadeIn time for titles from the config
     * @deprecated Currently does nothing... For now
     */
    @Deprecated // TODO
    public int getFadeIn() {
        return 0; //return getInt("FadeIn");
    }

    /**
     * Gets the stay time for titles from the config.
     *
     * @return the stay time for titles from the config
     * @deprecated Currently does nothing... For now
     */
    @Deprecated // TODO
    public int getStay() {
        return 0; //return getInt("Stay");
    }

    /**
     * Gets the fadeOut time for titles from the config.
     *
     * @return the fadeOut time for titles from the config
     * @deprecated Currently does nothing... For now
     */
    @Deprecated // TODO
    public int getFadeOut() {
        return 0; //return getInt("FadeOut");
    }

    // Base Utils - START

    /**
     * Base way of getting boolean from the config.
     *
     * @param string The string key used to get the boolean from the config
     * @return the boolean from the config,
     *         or <code>null</code> if there is nothing in the config with that key.
     */
    private boolean getBoolean(String string) {
        return simpleChatAlert.getConfig().getBoolean(string);
    }

    /**
     * Base way of getting integers from the config.
     *
     * @param string The string key used to get the integer from the config
     * @return the integer from the config,
     *         or <code>null</code> if there is nothing in the config with that key.
     */
    private int getInt(String string) {
        return simpleChatAlert.getConfig().getInt(string);
    }

    /**
     * Base way of getting strings from the config.
     *
     * @param string The string key used to get the string from the config
     * @return the string from the config,
     *         or <code>null</code> if there is nothing in the config with that key.
     */
    private String getString(String string) {
        return simpleChatAlert.getConfig().getString(string);
    }
    // Base Utils - END
}

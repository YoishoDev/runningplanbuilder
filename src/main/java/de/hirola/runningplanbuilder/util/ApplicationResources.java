package de.hirola.runningplanbuilder.util;

import de.hirola.runningplanbuilder.Global;
import de.hirola.runningplanbuilder.RunningPlanBuilder;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

/**
 * Copyright 2022 by Michael Schmidt, Hirola Consulting
 * This software us licensed under the AGPL-3.0 or later.
 *
 * Manager for app resources.
 *
 * @author Michael Schmidt (Hirola)
 * @since v.0.1
 */
public final class ApplicationResources
{
	private static ApplicationResources instance = null;
	private static Preferences userPreferences = null;
	private static final String RESOURCE_NOT_FOUND = "[Resource cannot be found]";
	private ResourceBundle resourceBundle;

	public static ApplicationResources getInstance() {
		if (instance == null) {
			instance = new ApplicationResources();
		}
		return instance;
	}

	/**
	 * Returns a resource string for the key. If the key cannot be found or is not a string,
	 * returns a string that indicates an error.
	 * 
	 * @param forKey for the string
	 * @return The string for the given key or a default error string.
	 */
	public String getString(@NotNull String forKey) {
		try {
			return resourceBundle.getString(forKey);
		} catch( MissingResourceException | ClassCastException exception ) {
			return RESOURCE_NOT_FOUND;
		}
	}
	
	/**
	 * Check if exist a string for a key.
	 *
	 * @param key for string
	 * @return True if the key is found in the resources.
	 */
	public boolean containsKey(@NotNull String key) {
		return resourceBundle.containsKey(key);
	}

	private ApplicationResources() {
		try {
			// get the localization from user preferences
			userPreferences = Preferences.userRoot().node(RunningPlanBuilder.class.getName());
			// en_GB, de_AT, ... or en
			String localeString = userPreferences.get(Global.UserPreferencesKeys.LOCALE, "en");
			Locale locale;
			if (localeString.contains("_") && localeString.length() == 5) {
				// language and country
				String language = localeString.substring(0, 2).toLowerCase(Locale.ROOT);
				String country  = localeString.substring(2, 5).toUpperCase(Locale.ROOT);
				locale = new Locale(language, country);
			} else {
				// only the language
				locale = new Locale(localeString);
			}
			resourceBundle = ResourceBundle.getBundle(RunningPlanBuilder.class.getSimpleName(), locale);
		} catch (SecurityException exception) {
			// load default (english) bundle
			resourceBundle = ResourceBundle.getBundle(RunningPlanBuilder.class.getSimpleName());
		}
	}
}

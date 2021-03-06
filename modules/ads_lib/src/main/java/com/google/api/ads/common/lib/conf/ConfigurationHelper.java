// Copyright 2012 Google Inc. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.api.ads.common.lib.conf;

import com.google.api.ads.common.lib.utils.logging.AdsServiceLoggers;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;

import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.configuration.CombinedConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.MapConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.tree.OverrideCombiner;

import java.io.File;
import java.net.URL;
import java.security.AccessControlException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.annotation.Nullable;

/**
 * Helper class that loads {@link Configuration} from various sources.
 *
 * @author Kevin Winter
 * @author Jeff Sham
 * @author Adam Rogal
 */
public class ConfigurationHelper {

  /**
   * Loads configuration from a specified path. If not absolute, will look in
   * the user home directory, the current classpath and the system classpath.
   * Absolute classpath references will not work.
   *
   * @param path the path to try first as a resource, then as a file
   * @throws ConfigurationLoadException if the configuration could not be
   *         loaded.
   * @returns properties loaded from the specified path or null.
   */
  public Configuration fromFile(String path) throws ConfigurationLoadException {
    PropertiesConfiguration propertiesConfiguration = new PropertiesConfiguration();
    try {
      propertiesConfiguration.load(path);
    } catch (ConfigurationException e) {
      if (Throwables.getRootCause(e) instanceof AccessControlException){
        AdsServiceLoggers.ADS_API_LIB_LOG.debug("Properties could not be loaded.", e);
      } else {
        throw new ConfigurationLoadException(
            "Encountered a problem reading the provided configuration file \"" + path + "\"!", e);
      }
    }
    return propertiesConfiguration;
  }

  /**
   * Loads configuration from a specified path.
   *
   * @param path the path to try first as a resource, then as a file
   * @throws ConfigurationLoadException if the configuration could not be
   *         loaded.
   * @returns properties loaded from the specified path or null.
   */
  public Configuration fromFile(File path) throws ConfigurationLoadException {
    try {
      return new PropertiesConfiguration(path);
    } catch (ConfigurationException e) {
      throw new ConfigurationLoadException(
          "Encountered a problem reading the provided configuration file \"" + path + "\"!", e);
    }
  }

  /**
   * Loads configuration from a specified path.
   *
   * @param path the path to try first as a resource, then as a file
   * @throws ConfigurationLoadException if the configuration could not be
   *         loaded.
   * @returns properties loaded from the specified path or null.
   */
  public Configuration fromFile(URL path) throws ConfigurationLoadException {
    try {
      return new PropertiesConfiguration(path);
    } catch (ConfigurationException e) {
      throw new ConfigurationLoadException(
          "Encountered a problem reading the provided configuration file \"" + path + "\"!", e);
    }
  }

  /**
   * Loads configuration from system defined arguments, i.e. -Dapi.x.y.z=abc.
   */
  public Configuration fromSystem() {
    return new MapConfiguration((Properties) System.getProperties().clone());
  }

  /**
   * Creates the combined configuration. System properties will overwrite path
   * properties, which overwrite URL properties. The configuration is created
   * by:
   * <ol><li>Loading properties {@link #fromSystem() from the system}.</li>
   * <li>Loading properties from each path with lower indexed paths overwritting
   * higher indexed paths using {@link #fromFile(String)}.</li>
   * <li>Loading properties from each URL with lower indexed urls overwritting
   * higher indexed URLs using {@link #fromFile(URL)}.</li></ol>
   * @throws ConfigurationLoadException if any required configuration could not
   *         be loaded
   */
  public CombinedConfiguration createCombinedConfiguration(
      @Nullable List<ConfigurationInfo<String>> paths,
      @Nullable List<ConfigurationInfo<URL>> urls) throws ConfigurationLoadException {
    CombinedConfiguration combinedConfiguration = new CombinedConfiguration(new OverrideCombiner());

    // System configuration will override all other configurations.
    addConfiguration(combinedConfiguration, fromSystem());

    // Classpath and file path configurations will override URL configurations.
    if (paths != null) {
      for (ConfigurationInfo<String> path : paths) {
        if (path != null && path.getLocation() != null) {
          try {
            addConfiguration(combinedConfiguration, fromFile(path.getLocation()));
          } catch (ConfigurationLoadException e) {
            if (!path.isOptional) {
              throw e;
            } else {
              AdsServiceLoggers.ADS_API_LIB_LOG.debug("Did not load optional configuration "
                  + path.getLocation() + ":", e);
            }
          }
        }
      }
    }

    if (urls != null) {
      for (ConfigurationInfo<URL> url : urls) {
        if (url != null && url.getLocation() != null) {
          try {
            addConfiguration(combinedConfiguration, fromFile(url.getLocation()));
          } catch (ConfigurationLoadException e) {
            if (!url.isOptional) {
              throw e;
            } else {
              AdsServiceLoggers.ADS_API_LIB_LOG.debug(
                  "Did not load optional configuration" + url.getLocation() + ":", e);
            }
          }
        }
      }
    }
    return combinedConfiguration;
  }

  /**
   * Adds a configuration to the combined configuration, overwriting any
   * existing properties.
   */
  @VisibleForTesting
  void addConfiguration(CombinedConfiguration combinedConfiguration,
      final Configuration configuration) {
    if (configuration instanceof AbstractConfiguration) {
      combinedConfiguration.addConfiguration((AbstractConfiguration) configuration);
    } else {
      combinedConfiguration.addConfiguration(new AbstractConfiguration() {

        public boolean isEmpty() {
          return configuration.isEmpty();
        }

        public Object getProperty(String key) {
          return configuration.getProperty(key);
        }

        @SuppressWarnings("rawtypes") // No rawtype in class.
        public Iterator getKeys() {
          return configuration.getKeys();
        }

        public boolean containsKey(String key) {
          return configuration.containsKey(key);
        }

        @Override
        protected void addPropertyDirect(String key, Object value) {
          configuration.addProperty(key, value);
        }
      });
    }
  }

  /**
   * Creates a list of configuration infos from the locations and if they are
   * optional. A {@code null} locations will return {@code null}
   *
   * @param <T> the type of location. Only {@code String} and {@code URL} are
   *            supported.
   */
  public static <T> List<ConfigurationInfo<T>> newList(@Nullable List<T> locations,
      final boolean isOptional) {
    if (locations == null) {
      return null;
    }

    return Lists.transform(locations, new Function<T, ConfigurationInfo<T>>() {
      public ConfigurationInfo<T> apply(T input) {
        return new ConfigurationInfo<T>(input, isOptional);
      }
    });
  }

  /**
   * Creates a list of configuration infos from the locations and if they are
   * optional.
   *
   * @param <T> the type of location. Only {@code String} and {@code URL} are
   *            supported.
   */
  public static <T> List<ConfigurationInfo<T>> newList(boolean isOptional, T... locations) {
    if (locations == null) {
      throw new IllegalArgumentException("locations cannot be null");
    }
    return newList(Lists.<T>newArrayList(locations), isOptional);
  }

  /**
   * Information about the configuration.

   * @param <T> the location type
   */
  public static class ConfigurationInfo<T> {

    private final T location;
    private final boolean isOptional;

    /**
     * Constructor.
     *
     * @param location the location (String for file path or {@code URL}) of the
     *        properties file
     * @param isOptional {@code true} if no exception should be thrown if it
     *        can't be loaded.
     * @throws IllegalArgumentException if location is anything but {@code String}
     *         or {@code URL}.
     */
    public ConfigurationInfo(T location, boolean isOptional) {
      if (!(location instanceof String || location instanceof URL)) {
        throw new IllegalArgumentException("Type " + location.getClass()
            + " not supported as a configuration location.");
      }
      this.location = location;
      this.isOptional = isOptional;
    }

    public T getLocation() {
      return location;
    }

    public boolean isOptional() {
      return isOptional;
    }
  }
}

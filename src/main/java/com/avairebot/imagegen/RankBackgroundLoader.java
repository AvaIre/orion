/*
 * Copyright (c) 2018.
 *
 * This file is part of AvaIre.
 *
 * AvaIre is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AvaIre is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AvaIre.  If not, see <https://www.gnu.org/licenses/>.
 *
 *
 */
package com.avairebot.imagegen;

import com.avairebot.config.YamlConfiguration;
import com.avairebot.utilities.ColorUtil;

import javax.annotation.Nonnull;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;


public class RankBackgroundLoader {

    private final YamlConfiguration config;
    private RankBackground background;

    /**
     * Instantiates a new Rank background loader using
     * the given background resource name. Data is
     * loaded from the classpath of the internal jar rather than on the file system.
     *
     * @param backgroundResource The path inside the backgrounds directory pointing to the resource.
     */
    public RankBackgroundLoader(@Nonnull String backgroundResource) {
        config = YamlConfiguration.loadConfiguration(new InputStreamReader(
            getClass().getClassLoader().getResourceAsStream("backgrounds/" + backgroundResource)
        ));

        background = new RankBackground(
            config.getInt("id"),
            config.getInt("cost"),
            config.getString("name"),
            config.getString("backgroundImage"),
            getColors()
        );
    }

    /**
     * Instantiates a new Rank background loader
     * that loads a resource from anywhere in the filesystem outside the jar,
     * given the full path.
     *
     * @param backgroundResource A path to the background resource in the filesystem.
     * @throws FileNotFoundException If the file cannot be found.
     */
    public RankBackgroundLoader(@Nonnull File backgroundResource) throws FileNotFoundException {
        config = YamlConfiguration.loadConfiguration(new InputStreamReader(new FileInputStream(backgroundResource)));

        int id = config.getInt("id");
        int cost = config.getInt("cost");

        String name = config.getString("name");
        String backgroundImage = config.getString("backgroundImage");

        background = new RankBackground(id, cost, name, backgroundImage, getColors(), true);
    }

    private BackgroundRankColors getColors() {
        BackgroundRankColors colors = new BackgroundRankColors();
        colors.setBackgroundColor(loadColorFromYaml("colors.backgroundColor"));
        colors.setMainTextColor(loadColorFromYaml("colors.mainTextColor"));
        colors.setSecondaryTextColor(loadColorFromYaml("colors.secondaryTextColor"));
        colors.setExperienceBackgroundColor(loadColorFromYaml("colors.experienceBackgroundColor"));
        colors.setExperienceForegroundColor(loadColorFromYaml("colors.experienceForegroundColor"));
        colors.setExperienceSeparatorColor(loadColorFromYaml("colors.experienceSeparatorColor"));

        if (config.contains("colors.backgroundCoverColor")) {
            colors.setBackgroundCoverColor(loadColorFromYaml("colors.backgroundCoverColor"));
        }

        if (config.contains("colors.experienceTextColor")) {
            colors.setExperienceTextColor(loadColorFromYaml("colors.experienceTextColor"));
        }

        return colors;
    }

    private Color loadColorFromYaml(String heading) {
        int red = config.getInt(heading + ".red");
        int green = config.getInt(heading + ".green");
        int blue = config.getInt(heading + ".blue");

        if (!config.contains((heading + ".alpha"))) {
            return ColorUtil.makeColor(red, green, blue);
        }

        return ColorUtil.makeColor(red, green, blue,
            config.getInt(heading + ".alpha")
        );
    }

    /**
     * Returns the rank background information loaded by this container
     * including the id, cost, name, background graphics, color scheme,
     * and where this configuration was loaded from.
     *
     * @return The rank background information
     */
    public RankBackground getRankBackground() {
        return background;
    }

    /**
     * Gets the Rank Background configuration, the config
     * can be used to directly load the image information
     * and id of the rank background.
     *
     * @return The rank background configuration.
     */
    public YamlConfiguration getConfig() {
        return config;
    }

    @Override
    public String toString() {
        return background.getName();
    }
}

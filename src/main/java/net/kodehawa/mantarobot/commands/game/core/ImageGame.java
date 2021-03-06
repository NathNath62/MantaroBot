/*
 * Copyright (C) 2016-2018 David Alejandro Rubio Escares / Kodehawa
 *
 * Mantaro is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * Mantaro is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Mantaro.  If not, see http://www.gnu.org/licenses/
 */

package net.kodehawa.mantarobot.commands.game.core;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.requests.RestAction;
import net.kodehawa.mantarobot.data.MantaroData;
import net.kodehawa.mantarobot.utils.cache.URLCache;

import java.util.function.Consumer;

public abstract class ImageGame extends Game<String> {
    private final URLCache cache;

    public ImageGame(int cacheSize) {
        cache = new URLCache(cacheSize);
    }

    protected RestAction<Message> sendEmbedImage(MessageChannel channel, String url, Consumer<EmbedBuilder> embedConfigurator) {
        EmbedBuilder eb = new EmbedBuilder();
        embedConfigurator.accept(eb);
        if(MantaroData.config().get().cacheGames) {
            eb.setImage("attachment://image.png");
            return channel.sendFile(cache.getInput(url), "image.png", new MessageBuilder().setEmbed(eb.build()).build());
        }
        eb.setImage(url);
        return channel.sendMessage(eb.build());
    }
}

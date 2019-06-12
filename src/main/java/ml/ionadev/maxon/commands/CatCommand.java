/*
 * Copyright 2019 Piyush Bhangale (officialpiyush)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ml.ionadev.maxon.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.ChannelType;

import java.awt.*;

/**
 * @author Piyush Bhangale(bhangalepiyush@gmail.com)
 */
public class CatCommand extends Command
{
    public CatCommand()
    {
        this.name = "cat";
        this.help = "Generate A Random Cat";
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
        this.guildOnly = false;
    }

    @Override
    protected void execute(CommandEvent event)
    {
        Unirest.get("http://aws.random.cat/meow").asJsonAsync(new Callback<JsonNode>() {
            @Override
            public void completed(HttpResponse<JsonNode> response)
            {
                event.reply(new EmbedBuilder()
                .setColor(event.isFromType(ChannelType.TEXT) ? event.getSelfMember().getColor() : Color.GREEN)
                .setImage(response.getBody().getObject().getString("file"))
                .build());
            }

            @Override
            public void failed(UnirestException e)
            {
                event.reactError();
            }

            @Override
            public void cancelled()
            {
                event.reactError();
            }
        });
    }
}

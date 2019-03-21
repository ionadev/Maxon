/*
 * Copyright 2019 Piyush Bhangale (ionadev)
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
package ml.ionadev.maxon;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import ml.ionadev.maxon.commands.KickCommand;
import ml.ionadev.maxon.commands.moderation.BanCommand;
import ml.ionadev.maxon.commands.CatCommand;
import ml.ionadev.maxon.commands.owner.EvalCommand;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author Piyush Bhangale(bhangalepiyush@gmail.com)
 */
public class Maxon {
    public Maxon() throws Exception
    {
        List<String> list = Files.readAllLines(Paths.get("config.txt"));
        String token = list.get(0);
        String ownerId = list.get(1);

        EventWaiter waiter = new EventWaiter();
        CommandClientBuilder client = new CommandClientBuilder()
                .setServerInvite("https://discord.gg/hWbb4Ee")
                .setLinkedCacheSize(0);

        client.useDefaultGame();
        client.setOwnerId(ownerId);
        client.setEmojis("\uD83D\uDE03", "\uD83D\uDE2E", "\uD83D\uDE26");
        client.setPrefix(")");

        client.addCommands(
                new CatCommand(),
                new BanCommand(),
                new KickCommand(),
                new EvalCommand(this)
        );

        new JDABuilder(AccountType.BOT)
                .setToken(token)
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .addEventListener(waiter)
                .addEventListener(client.build())
                .build();

    }

    public static void main(String[] args) throws Exception
    {
        new Maxon();
    }
}


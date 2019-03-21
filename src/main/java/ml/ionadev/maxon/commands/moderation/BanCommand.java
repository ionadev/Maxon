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
package ml.ionadev.maxon.commands.moderation;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.exceptions.PermissionException;

import java.util.List;

/**
 * @author Piyush Bhangale(bhangalepiyush@gmail.com)
 */
public class BanCommand extends Command {
    public BanCommand()
    {
        this.name = "ban";
        this.help = "Ban User(s)";
        this.userPermissions = new Permission[]{Permission.BAN_MEMBERS};
        this.botPermissions = new Permission[]{Permission.BAN_MEMBERS};
        this.guildOnly = true;
    }

    @Override
    protected void execute(CommandEvent event)
    {
        Guild guild = event.getGuild();
        Member selfMember = event.getSelfMember();
        TextChannel channel = event.getTextChannel();

        if(event.getMessage().getMentionedUsers().isEmpty())
        {
            channel.sendMessage("You must mention 1 or more Users to be banned!").queue();
            return;
        }

        List<User> mentionedUsers = event.getMessage().getMentionedUsers();

        for(User user : mentionedUsers)
        {
            Member member = guild.getMember(user);

            if(!selfMember.canInteract(member)){
                channel.sendMessage("Cannot Ban member: ")
                        .append(member.getEffectiveName())
                        .append(", they are higher in the hierarchy than I am!")
                        .queue();
                continue;
            }

            guild.getController().ban(member , 1).queue(
                    success -> channel.sendMessage("Banned ").append(member.getEffectiveName()).append("! Cya!").queue(),
                    error ->
                    {
                        if (error instanceof PermissionException)
                        {
                            PermissionException pe = (PermissionException) error;
                            Permission missingPermission = pe.getPermission();

                            channel.sendMessage("PermissionError Banning [")
                                    .append(member.getEffectiveName()).append("]: ")
                                    .append(error.getMessage()).queue();
                        }
                        else
                        {
                            channel.sendMessage("Unknown error while Banning [")
                                    .append(member.getEffectiveName())
                                    .append("]: <").append(error.getClass().getSimpleName()).append(">: ")
                                    .append(error.getMessage()).queue();
                        }
                    });
        }
    }
}

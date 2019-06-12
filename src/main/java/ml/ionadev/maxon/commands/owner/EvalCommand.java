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
package ml.ionadev.maxon.commands.owner;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import ml.ionadev.maxon.Maxon;

import javax.script.*;
import java.io.Reader;

/**
 * @author Piyush Bhangale(bhangalepiyush@gmail.com)
 */
public class EvalCommand extends Command
{
    public final Maxon maxon;
    public EvalCommand(Maxon maxon)
    {
        this.maxon = maxon;
        this.name = "eval";
        this.aliases = new String[]{"ev"};
        this.help = "Evaluate Java Code";
        this.ownerCommand = true;
        this.guildOnly = false;
        this.hidden = true;
    }

    @Override
    protected void execute(CommandEvent event)
    {
        event.getChannel().sendTyping().queue();
        event.async(() ->
        {
            ScriptEngine se = new ScriptEngineManager().getEngineByName("Nashorn");
            se.put("bot", maxon);
            se.put("event", event);
            se.put("jda", event.getJDA());
            se.put("guild", event.getGuild());
            se.put("channel", event.getChannel());
            String args = event.getArgs().replaceAll("([^(]+?)\\s*->", "function($1)");
            try
            {
                event.replySuccess("Evaluated Successfully:\n```\n"+se.eval(args)+" ```");
            }
            catch(Exception e)
            {
                event.replyError("An exception was thrown:\n```\n"+e+" ```");
            }
        });
    }
}

/*******************************************************************************
 * This file is part of Minebot.
 *
 * Minebot is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Minebot is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Minebot.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package net.famzangl.minecraft.minebot.ai.command;

import com.google.common.base.Function;
import net.famzangl.minecraft.minebot.ai.commands.*;
import net.famzangl.minecraft.minebot.ai.scripting.CommandJs;
import net.famzangl.minecraft.minebot.build.commands.*;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

import java.util.List;

/**
 * Controlls the AI from a chat line.
 * 
 * @author michael
 * 
 */
public class AIChatController {
	private static final CommandRegistry registry = new CommandRegistry();

	private static final int PER_PAGE = 8;

	static {
		registerCommand(CommandHelp.class);
		registerCommand(CommandMine.class);
		registerCommand(CommandTunnel.class);
		registerCommand(CommandStop.class);
		registerCommand(CommandResume.class);
		registerCommand(CommandUngrab.class);
		registerCommand(CommandRun.class);
		registerCommand(CommandLoad.class);
		registerCommand(CommandJs.class);
		registerCommand(CommandPlant.class);
		registerCommand(CommandLumberjack.class);
		registerCommand(CommandTint.class);
		registerCommand(CommandBuildWay.class);
		registerCommand(CommandShear.class);
		registerCommand(CommandPause.class);
		registerCommand(CommandStore.class);
		registerCommand(CommandFurnace.class);
		registerCommand(CommandGet.class);
		registerCommand(CommandCraft.class);
		registerCommand(CommandEnchant.class);
		registerCommand(CommandEat.class);
		registerCommand(CommandRespawn.class);
		registerCommand(CommandXPFarm.class);
		registerCommand(CommandAirbridge.class);
		
		registerCommand(CommandWalk.class);
		registerCommand(CommandLookAt.class);
		registerCommand(CommandThrow.class);
		registerCommand(CommandPathfind.class);

		registerCommand(CommandKill.class);
		registerCommand(CommandFish.class);
		registerCommand(CommandSit.class);
		registerCommand(CommandFeed.class);
		registerCommand(CommandSettings.class);
		
		registerCommand(CommandDumpSigns.class);
		registerCommand(CommandRenderMap.class);
		registerCommand(CommandTestMinectaft.class);
		registerCommand(CommandStats.class);


		registerCommand(CommandBuild.class);
		registerCommand(CommandClearArea.class);
		registerCommand(CommandFillArea.class);
		registerCommand(CommandReset.class);
		registerCommand(CommandScheduleBuild.class);
		registerCommand(CommandSetPos.class);
		registerCommand(CommandExpand.class);
		registerCommand(CommandStepNext.class);
		registerCommand(CommandStepPlace.class);
		registerCommand(CommandStepWalk.class);
		registerCommand(CommandListBuilds.class);
		registerCommand(CommandReverse.class);
		registerCommand(CommandMove.class);
		registerCommand(CommandCount.class);
	}

	private AIChatController() {
	}

	private static void registerCommand(Class<?> commandClass) {
		registry.register(commandClass);
	}

	public static void addChatLine(String message) {
		addToChat("§8[§5S§fpicy§8] §f" + message);
	}

	public static CommandRegistry getRegistry() {
		return registry;
	}

	private static void addToChat(String string) {
		Minecraft.getMinecraft().thePlayer
				.addChatMessage(new ChatComponentText(string));
	}

	public static <T> void addToChatPaged(String title, int page, List<T> data,
			Function<T, String> convert) {
		AIChatController.addChatLine(title + " " + page + " / "
				+ (int) Math.ceil((float) data.size() / PER_PAGE));
		for (int i = Math.max(0, page - 1) * PER_PAGE; i < Math.min(page
				* PER_PAGE, data.size()); i++) {
			final String line = convert.apply(data.get(i));
			addChatLine(line);
		}
	}

}

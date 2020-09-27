package spicy.command;

import spicy.command.commands.Bind;
import spicy.command.commands.Cosmetic;
import spicy.command.commands.Friend;
import spicy.command.commands.SetValue;
import spicy.main.Wrapper;

import java.util.ArrayList;

public class CommandManager
{
    private ArrayList<Command> commands;
    
    public CommandManager() {
        this.commands = new ArrayList<Command>();
        this.addCommand(new Bind());
        this.addCommand(new Friend());
        this.addCommand(new SetValue());
        this.addCommand(new Cosmetic());

    }
    
    public void addCommand(final Command c) {
        this.commands.add(c);
    }
    
    public ArrayList<Command> getCommands() {
        return this.commands;
    }
    
    public void callCommand(final String input) {
        final String[] split = input.split(" ");
        final String command = split[0];
        final String args = input.substring(command.length()).trim();
        for (final Command c : this.getCommands()) {
            if (c.getNames().equalsIgnoreCase(command)) {
                try {
                    c.onCommand(args, args.split(" "));
                }
                catch (Exception e) {
                    Wrapper.addChatMessage(c.getError());
                }
                return;
            }
        }
        Wrapper.addChatMessage("Unknown command!");
    }
}

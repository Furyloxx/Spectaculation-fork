package me.superischroma.spectaculation.command;

@CommandParameters(description = "The main command for Detroitx.", aliases = "dtx")
public class SpectaculationCommand extends SCommand
{
    @Override
    public void run(CommandSource sender, String[] args)
    {
        send("Spectaculation v" + plugin.getDescription().getVersion());
    }
}
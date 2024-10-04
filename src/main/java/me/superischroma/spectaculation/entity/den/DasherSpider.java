package me.superischroma.spectaculation.entity.den;

public class DasherSpider extends BaseSpider
{
    @Override
    public String getEntityName()
    {
        return "Dasher Spider";
    }

    @Override
    public double getEntityMaxHealth()
    {
        return 170.0;
    }
    
    @Override
    public double getEntityLevel() {
      return 4;
    }

    @Override
    public double getDamageDealt()
    {
        return 55.0;
    }

    @Override
    public double getMovementSpeed()
    {
        return 0.4;
    }

    @Override
    public double getXPDropped()
    {
        return 10.8;
    }
}
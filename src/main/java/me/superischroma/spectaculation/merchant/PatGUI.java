package me.superischroma.spectaculation.merchant;

import me.superischroma.spectaculation.item.SItem;
import me.superischroma.spectaculation.item.SMaterial;
import me.superischroma.spectaculation.merchant.ShopGUI;
import me.superischroma.spectaculation.util.SUtil;

public class PatGUI extends ShopGUI
{
      private static final SItem[] ITEMS = {
            MerchantItemHandler.getItem(SMaterial.FLINT),
            MerchantItemHandler.getItem(SMaterial.GRAVEL)
    };
        public PatGUI(int page)
    {
        super("Pat", page, ITEMS);
    }

    public PatGUI()
    {
        this(1);
    }
}
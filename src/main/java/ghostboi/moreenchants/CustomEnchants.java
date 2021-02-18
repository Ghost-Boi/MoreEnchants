package ghostboi.moreenchants;

import org.bukkit.enchantments.Enchantment;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;

public class CustomEnchants {

    public static final Enchantment TELEPATHY = new EnchantmentWrapper("telepathy", "Telepathy", 1);
    public static final Enchantment AUTOSMELT = new EnchantmentWrapper("autosmelt", "Auto Smelt", 1);

    public static void register(){
        boolean registeredTELEPATHY = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(TELEPATHY);
        boolean registeredAUTOSMELT = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(AUTOSMELT);

        if(!registeredTELEPATHY){
            registerEnchantment(TELEPATHY);
        }
        if(!registeredAUTOSMELT){
            registerEnchantment(AUTOSMELT);
        }
    }

    public static void registerEnchantment(Enchantment enchantment){
        boolean registered = true;
        try{
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
            Enchantment.registerEnchantment(enchantment);
        }catch (Exception e){
            registered = false;
            e.printStackTrace();
        }
        if(registered){
            System.out.println("[MoreEnchants] " + enchantment + " Loaded!");
        }
    }

}

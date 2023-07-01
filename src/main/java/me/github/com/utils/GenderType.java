package me.github.com.utils;

import lombok.Getter;
import me.github.com.IdentityCardPlugin;
import net.laxelib.com.utils.ColorsAPI;

public enum GenderType {

    MALE(ColorsAPI.color(IdentityCardPlugin.getMessagesConfiguration().getString("Messages.male"))),
    FEMALE(ColorsAPI.color(IdentityCardPlugin.getMessagesConfiguration().getString("Messages.female")));

    @Getter String displayName;

    GenderType(String displayName){
        this.displayName = displayName;
    }

}

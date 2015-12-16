package io.github.fanky10.sociallogin.module.constants.enums;

/**
 * Created by carlospienovi1 on 12/4/15.
 */
public enum ExternalLoginProviders {

    None(0),
    Google(1),
    Facebook(2);

    private final int value;

    public int getValue() {
        return value;
    }

    private ExternalLoginProviders(int value) {
        this.value = value;
    }
}

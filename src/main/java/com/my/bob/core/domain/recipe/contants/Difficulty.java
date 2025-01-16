package com.my.bob.core.domain.recipe.contants;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.my.bob.core.constants.interfaces.EnumPropertyType;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Difficulty implements EnumPropertyType {
    ANYONE("아무나"),
    BEGINNER("초급"),
    MIDRANGE("중급"),
    HIGH("고급"),
    GODHOOD("신의 경지"),

    ;

    private String title;

    @Override
    public String getCode() {
        return name();
    }

    @Override
    public String getTitle() {
        return title;
    }

    Difficulty(String title) {
        this.title = title;
    }
}

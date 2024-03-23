package io.github.eoinkanro.commons.utils.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TestBigJson {

    private TestSimpleJson json;

    @Override
    public String toString() {
        return "{\"json\":" + json.toString() + "}";
    }
}

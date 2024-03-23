package io.github.eoinkanro.commons.utils.model;

import lombok.*;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestSimpleJson {

    private String name;
    private int number;
    private List<String> list;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestSimpleJson testJson = (TestSimpleJson) o;
        return number == testJson.number && Objects.equals(name, testJson.name) && Objects.equals(list, testJson.list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, number, list);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        builder.append("\"name\":\"");
        builder.append(name);
        builder.append("\"");
        builder.append(",\"number\":");
        builder.append(number);
        builder.append(",\"list\":");
        builder.append("[");
        for (int i = 0; i < list.size(); i++) {
            builder.append("\"");
            builder.append(list.get(i));
            builder.append("\"");
            if (i < list.size() - 1) {
                builder.append(",");
            }
        }
        builder.append("]}");
        return builder.toString();
    }
}

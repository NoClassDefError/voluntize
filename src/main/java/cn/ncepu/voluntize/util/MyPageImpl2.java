package cn.ncepu.voluntize.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

public class MyPageImpl2<T> extends PageImpl<T> {
    private static final long serialVersionUID = 3248189030448292002L;

    /**
     * Jackson序列化,反序列化重要方法
     */
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public MyPageImpl2(@JsonProperty("content") List<T> content, @JsonProperty("number") int number, @JsonProperty("size") int size,
                       @JsonProperty("totalElements") Long totalElements, @JsonProperty("pageable") JsonNode pageable, @JsonProperty("last") boolean last,
                       @JsonProperty("totalPages") int totalPages, @JsonProperty("sort") JsonNode sort, @JsonProperty("first") boolean first,
                       @JsonProperty("numberOfElements") int numberOfElements, @JsonProperty("empty") boolean empty) {
        super(content, PageRequest.of(number, size), totalElements);
    }

    public MyPageImpl2(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public MyPageImpl2(List<T> content) {
        super(content);
    }

    public MyPageImpl2() {
        super(new ArrayList<T>());
    }

    public MyPageImpl2(Page<T> page) {
        super(page.getContent(), page.getPageable(), page.getTotalPages());
    }
}

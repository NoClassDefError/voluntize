package cn.ncepu.voluntize.util;

import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Page对象要被序列化缓存在redis中，但在从redis取出时会无法反序列化，
 * 因为Page对象没有空构造器。只能自己来写page实现。
 * <p>
 * 本类综合了PageImpl类和Chunk抽象类，可以被dao注入，也能够被反序列化
 *
 * @author Gehanchen
 * @see org.springframework.data.domain.Page
 */
public class MyPageImpl<T> implements Page<T>, Serializable {

    public MyPageImpl() {
    }

    public MyPageImpl(Page<T> page) {
        this.content.addAll(page.getContent());
        this.pageable = page.getPageable();
        this.total = content.size();
    }

    public MyPageImpl(List<T> content, Pageable pageable) {
        Assert.notNull(content, "Content must not be null!");
        Assert.notNull(pageable, "Pageable must not be null!");
        this.content.addAll(content);
        this.pageable = pageable;
        this.total = content.size();
    }

    private final List<T> content = new ArrayList<>();
    private long total;
    private @Getter
    Pageable pageable = Pageable.unpaged();

    @Override
    public int getTotalPages() {
        return getSize() == 0 ? 1 : (int) Math.ceil((double) total / (double) getSize());
    }

    @Override
    public long getTotalElements() {
        return total;
    }

    @Override
    public <U> Page<U> map(Function<? super T, ? extends U> converter) {
        return new PageImpl<>(getConvertedContent(converter), getPageable(), total);
    }

    protected <U> List<U> getConvertedContent(Function<? super T, ? extends U> converter) {
        Assert.notNull(converter, "Function must not be null!");
        return this.stream().map(converter).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        String contentType = "UNKNOWN";
        List<T> content = getContent();
        if (content.size() > 0) contentType = content.get(0).getClass().getName();
        return String.format("MyPage %s of %d containing %s instances", getNumber() + 1, getTotalPages(), contentType);
    }

    @Override
    public int getNumber() {
        return pageable.isPaged() ? pageable.getPageNumber() : 0;
    }

    @Override
    public int getSize() {
        return pageable.isPaged() ? pageable.getPageSize() : content.size();
    }

    @Override
    public int getNumberOfElements() {
        return content.size();
    }

    @Override
    public List<T> getContent() {
        return Collections.unmodifiableList(content);
    }

    @Override
    public boolean hasContent() {
        return !content.isEmpty();
    }

    @Override
    public Sort getSort() {
        return pageable.getSort();
    }

    @Override
    public boolean isFirst() {
        return !hasPrevious();
    }

    @Override
    public boolean isLast() {
        return !hasNext();
    }

    @Override
    public boolean hasNext() {
        return getNumber() + 1 < getTotalPages();
    }

    @Override
    public boolean hasPrevious() {
        return getNumber() > 0;
    }

    @Override
    public Pageable nextPageable() {
        return hasNext() ? pageable.next() : Pageable.unpaged();
    }

    @Override
    public Pageable previousPageable() {
        return hasPrevious() ? pageable.previousOrFirst() : Pageable.unpaged();
    }

    @Override
    public Iterator<T> iterator() {
        return content.iterator();
    }
}
package de.dealog.msg.converter;

import com.google.common.base.Converter;
import com.google.common.collect.Lists;
import de.dealog.common.converter.UnsupportedConversionException;
import de.dealog.msg.rest.model.PagedListRest;
import de.dealog.msg.service.model.PagedList;

/**
 * Convert a {@link PagedList} of type {@link S} to {@link PagedListRest} of type {@link T}
 *
 * @param <S> Type of the {@link PagedList#getContent()}s to convert from
 * @param <T> Type of the {@link PagedListRest#getContent()}s to convert to
 */
public abstract class PagedListConverter<S, T> extends Converter<PagedList<? extends S>, PagedListRest<T>> {

    /**
     * Content converter to convert content from {@link S} to {@link T}
     */
    private Converter<S, T> contentConverter;

    /**
     * Set...
     * @param contentConverter the content converter
     */
    public void setContentConverter(final Converter<S,T> contentConverter) {
        this.contentConverter = contentConverter;
    }

    @Override
    protected PagedListRest<T> doForward(final PagedList<? extends S> pagedList) {
        return PagedListRest.<T>builder()
                .content(Lists.newArrayList(contentConverter.convertAll(pagedList.getContent())))
                .number(pagedList.getPage())
                .size(pagedList.getPageSize())
                .totalPages(pagedList.getPageCount())
                .totalElements(pagedList.getCount())
                .build();
    }

    @Override
    protected PagedList<S> doBackward(final PagedListRest<T> pagedListRest) {
        throw new UnsupportedConversionException(String.format("Unsupported conversion from %s", pagedListRest));
    }
}

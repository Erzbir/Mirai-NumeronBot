package com.erzbir.numeron.api.filter.annotation;

import com.erzbir.numeron.annotation.CommonFilter;
import com.erzbir.numeron.api.filter.AnnotationFilter;
import com.erzbir.numeron.api.filter.CustomFilter;
import com.erzbir.numeron.api.filter.DefaultFilter;
import com.erzbir.numeron.utils.NumeronLogUtil;
import net.mamoe.mirai.event.Event;

import java.lang.reflect.InvocationTargetException;

/**
 * 针对 {@link CommonFilter } 注解的内容过滤
 *
 * @author Erzbir
 * @Date 2023/8/11
 * @see CommonFilter
 */
public class CommonAnnotationFilter extends AbstractAnnotationFilter<CommonFilter> implements AnnotationFilter {
    @Override
    public boolean filter(Event event) {
        Class<? extends CustomFilter> filter = annotation.filter();
        if (!filter.equals(DefaultFilter.class)) {
            try {
                CustomFilter customFilter = filter.getConstructor().newInstance();
                return customFilter.filter(event);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                NumeronLogUtil.logger.error(e.getMessage(), e);
            }
        }
        return true;
    }
}

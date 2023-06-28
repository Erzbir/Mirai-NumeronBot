package com.erzbir.numeron.core.filter.event.permission;

import com.erzbir.numeron.core.filter.EventFilter;
import com.erzbir.numeron.core.filter.Filter;
import net.mamoe.mirai.event.events.MessageEvent;

/**
 * @author Erzbir
 * @Date: 2022/11/26 15:54
 * <p>所有人权限过滤类, 此类是所有人权限不做过滤</p>
 */
public class AllPermissionFilter extends AbstractPermissionFilter implements EventFilter<MessageEvent> {
    public AllPermissionFilter(Filter filter) {
        super(filter);
    }

    @Override
    public void filter(MessageEvent event) {
        setFilterRule(t -> true, event);
    }

    @Override
    public boolean filter() {
        return super.filter() && filterRule.test(arg);
    }
}

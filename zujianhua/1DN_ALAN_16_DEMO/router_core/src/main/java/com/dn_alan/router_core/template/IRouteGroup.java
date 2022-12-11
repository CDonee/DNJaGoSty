package com.dn_alan.router_core.template;


import com.dn_alan.router_annotation.model.RouteMeta;

import java.util.Map;

public interface IRouteGroup {

    void loadInto(Map<String, RouteMeta> atlas);
}

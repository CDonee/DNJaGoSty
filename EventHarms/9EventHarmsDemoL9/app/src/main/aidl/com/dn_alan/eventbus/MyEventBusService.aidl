// MyEventBusService.aidl
package com.dn_alan.eventbus;

// Declare any non-default types here with import statements
import com.dn_alan.eventbus.Request;
import com.dn_alan.eventbus.Responce;
interface MyEventBusService {
 Responce send(in Request request);
}

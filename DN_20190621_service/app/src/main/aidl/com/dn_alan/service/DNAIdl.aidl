// DNAIdl.aidl
package com.dn_alan.service;

// Declare any non-default types here with import statements
import com.dn_alan.service.Person;

interface DNAIdl {
    void addPerson(in Person person);
    List<Person> getPersonList();
}

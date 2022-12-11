package com.dn_alan.eventbus;

/**
 * Created by Administrator on 2018/5/23.
 */

public class FileRecord {
    private String path;
    private long size;

    public FileRecord(String path, long size) {
        this.path = path;
        this.size = size;
    }

    public FileRecord( ) {


    }

    @Override
    public String toString() {
        return "FileRecord{" +
                "path='" + path + '\'' +
                ", size=" + size +
                '}';
    }
}

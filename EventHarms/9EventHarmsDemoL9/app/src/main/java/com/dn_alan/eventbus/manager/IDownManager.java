package com.dn_alan.eventbus.manager;


import com.dn_alan.eventbus.FileRecord;
import com.dn_alan.eventbus.annotion.ClassId;

/**
 * Created by Administrator on 2018/5/23.
 */
@ClassId("com.dn_alan.eventbus.manager.DownManager")
public interface IDownManager  {
    public FileRecord getFileRecord();

    public void setFileRecord(FileRecord fileRecord);

}

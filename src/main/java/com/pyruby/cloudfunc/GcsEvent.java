package com.pyruby.cloudfunc;

import lombok.Data;

import java.util.Date;

@Data
public class GcsEvent {
    private String bucket;
    private String name;
    private String metageneration;
    private Date timeCreated;
    private Date updated;
}

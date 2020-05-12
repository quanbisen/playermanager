package com.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Album {
    private Integer id;
    private String name;
    private Integer singerID;
    private String singerName;
    private Date publishTime;
    private String description;
    private String imageURL;
}

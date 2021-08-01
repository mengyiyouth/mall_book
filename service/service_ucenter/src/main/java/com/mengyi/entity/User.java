package com.mengyi.entity;

import lombok.Data;
import org.springframework.web.bind.annotation.DeleteMapping;

/**
 * @author mengyiyouth
 * @date 2021/4/22 8:33
 **/
@Data
public class User {

    String mobile;
    String password;
}

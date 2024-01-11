package com.lys.sys.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    private String ip;
}

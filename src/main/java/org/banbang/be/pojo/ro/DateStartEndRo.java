package org.banbang.be.pojo.ro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@ApiModel("起末日期请求对象")
@Data
@Slf4j
public class DateStartEndRo {

    @ApiModelProperty(value = "开始日期", example = "20221020")
    Date start;
    @ApiModelProperty(value = "结束日期", example = "20231020")
    Date end;

    public void setStart(Date start) {
        this.start = start;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public void setStart(String start) {
        this.start = parseStringToDate(start);

    }

    public void setEnd(String end) {
        this.end = parseStringToDate(end);
    }

   private Date parseStringToDate(String s) {
       SimpleDateFormat yyyyMMddFormater = new SimpleDateFormat("yyyyMMdd");
       try {
           return yyyyMMddFormater.parse(s);
       } catch (ParseException e) {
           e.printStackTrace();
           log.error(String.valueOf(e));
           return new Date(1666197082L); // 2022-10-20 00:31:22
       }
   }
}

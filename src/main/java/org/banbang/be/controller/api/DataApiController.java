package org.banbang.be.controller.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.var;
import org.banbang.be.pojo.ro.DateStartEndRo;
import org.banbang.be.service.DataService;
import org.banbang.be.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;


/**
 * 网站数据
 */
@RestController
@Api(tags = "网站数据（admin、moderator only）")
public class DataApiController {

    @Autowired
    private DataService dataService;

    /**
     * 统计网站 uv
     * @param dser
     * @param resp
     * @return
     */
    @PostMapping("/api/data/uv")
    @ApiOperation("统计网站 uv")
    public R getUV(
            @RequestBody
            @ApiParam(required = true)
            DateStartEndRo dser,
            HttpServletResponse resp
    ) {
        var start = dser.getStart();
        var end = dser.getEnd();
        var data = new HashMap<String, Object>();
        long uv = dataService.calculateUV(start, end);
        data.put("uvResult", uv);
        data.put("uvStartDate", start);
        data.put("uvEndDate", end);
        return R.ok(resp, "统计网站UV", data);
//        return "forward:/data";
    }

    /**
     * 统计网站 DAU
     * @param dser
     * @param resp
     * @return
     */
    @PostMapping("/api/data/dau")
    @ApiOperation("统计网站 DAU")
    public R getDAU(
            @RequestBody
            @ApiParam(required = true)
            DateStartEndRo dser,
            HttpServletResponse resp
    ) {
        var start = dser.getStart();
        var end = dser.getEnd();
        var data = new HashMap<String, Object>();
        long dau = dataService.calculateDAU(start, end);
        data.put("dauResult", dau);
        data.put("dauStartDate", start);
        data.put("dauEndDate", end);
//        return "forward:/data";
        return R.ok(resp, "统计网站DAU", data);

    }

}

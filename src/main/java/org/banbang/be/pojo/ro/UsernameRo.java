package org.banbang.be.pojo.ro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("文件名请求对象")
@Data
public class FileNameRo {
    @ApiModelProperty(value = "文件名", example = "这里是文件名")
    private String fileName;
}

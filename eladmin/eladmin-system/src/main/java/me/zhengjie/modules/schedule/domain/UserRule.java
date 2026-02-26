package me.zhengjie.modules.schedule.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseEntity;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

@Getter
@Setter
@TableName("sch_user_rule")
public class UserRule extends BaseEntity implements Serializable {

    @NotNull(groups = Update.class)
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键ID", hidden = true)
    private Long id;

    @NotNull
    @ApiModelProperty(value = "员工ID")
    private Long userId;

    @NotNull
    @ApiModelProperty(value = "规则ID")
    private Long ruleId;

    @ApiModelProperty(value = "生效开始日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date startDate;

    @ApiModelProperty(value = "生效结束日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date endDate;

    @NotNull
    @ApiModelProperty(value = "是否启用")
    private Boolean enabled;

    @TableField(exist = false)
    @ApiModelProperty(value = "员工姓名")
    private String username;

    @TableField(exist = false)
    @ApiModelProperty(value = "规则名称")
    private String ruleName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRule userRule = (UserRule) o;
        return Objects.equals(id, userRule.id) &&
                Objects.equals(userId, userRule.userId) &&
                Objects.equals(ruleId, userRule.ruleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, ruleId);
    }
}

package me.zhengjie.modules.forest.service.mapstruct;

import me.zhengjie.modules.forest.domain.ForestResource;
import me.zhengjie.modules.forest.service.dto.ForestResourceDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * 森林资源MapStruct映射接口
 * @author [你的名字]
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ForestResourceMapperStruct {

    ForestResourceDto toDto(ForestResource forestResource);

    ForestResource toEntity(ForestResourceDto forestResourceDto);

}
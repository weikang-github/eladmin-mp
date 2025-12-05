-- 创建森林资源表
CREATE TABLE IF NOT EXISTS forest_resource (
    id BIGSERIAL PRIMARY KEY,
    forest_class_number VARCHAR(255) NOT NULL COMMENT '林班编号',
    sub_class_number VARCHAR(255) NOT NULL COMMENT '小班编号',
    tree_species VARCHAR(255) NOT NULL COMMENT '树种',
    area DECIMAL(10, 2) NOT NULL COMMENT '面积（公顷）',
    volume DECIMAL(10, 2) NOT NULL COMMENT '蓄积量',
    geometry GEOMETRY(POLYGON, 4326) NOT NULL COMMENT '几何边界',
    create_by VARCHAR(255) COMMENT '创建人',
    update_by VARCHAR(255) COMMENT '更新人',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_forest_sub_class (forest_class_number, sub_class_number)
) COMMENT '森林资源表';

-- 创建空间索引
CREATE INDEX IF NOT EXISTS idx_forest_resource_geometry ON forest_resource USING GIST (geometry);
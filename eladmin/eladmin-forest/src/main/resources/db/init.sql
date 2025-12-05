-- 启用PostGIS扩展
CREATE EXTENSION IF NOT EXISTS postgis;

-- 创建森林资源表
CREATE TABLE IF NOT EXISTS forest_resource (
    id BIGSERIAL PRIMARY KEY,
    forest_block_no VARCHAR(50) NOT NULL COMMENT '林班编号',
    sub_block_no VARCHAR(50) NOT NULL COMMENT '小班编号',
    tree_species VARCHAR(100) NOT NULL COMMENT '树种',
    area DECIMAL(10, 2) NOT NULL COMMENT '面积（公顷）',
    volume DECIMAL(10, 2) NOT NULL COMMENT '蓄积量',
    boundary GEOMETRY(POLYGON, 4326) NOT NULL COMMENT '几何边界（WGS84坐标系）',
    created_by BIGINT COMMENT '创建人',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by BIGINT COMMENT '更新人',
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标识（0：未删除，1：已删除）',
    UNIQUE KEY unique_forest_sub_block (forest_block_no, sub_block_no)
) COMMENT '森林资源表';

-- 创建空间索引以提高查询性能
CREATE INDEX IF NOT EXISTS idx_forest_resource_boundary ON forest_resource USING GIST(boundary);

-- 创建普通索引
CREATE INDEX IF NOT EXISTS idx_forest_resource_forest_block_no ON forest_resource(forest_block_no);
CREATE INDEX IF NOT EXISTS idx_forest_resource_sub_block_no ON forest_resource(sub_block_no);
CREATE INDEX IF NOT EXISTS idx_forest_resource_tree_species ON forest_resource(tree_species);

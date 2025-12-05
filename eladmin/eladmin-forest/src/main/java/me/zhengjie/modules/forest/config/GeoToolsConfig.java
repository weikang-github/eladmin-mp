/*
 *  Copyright 2019-2025 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package me.zhengjie.modules.forest.config;

import org.geotools.geojson.feature.FeatureJSON;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * GeoTools配置类
 * @author Zheng Jie
 * @date 2025-01-01
 */
@Configuration
public class GeoToolsConfig {

    /**
     * 配置FeatureJSON，用于空间数据的GeoJSON格式转换
     * @return FeatureJSON实例
     */
    @Bean
    public FeatureJSON featureJSON() {
        FeatureJSON featureJSON = new FeatureJSON();
        // 设置坐标精度
        featureJSON.setEncodeFeatureCollectionBounds(true);
        featureJSON.setEncodeFeatureBounds(true);
        return featureJSON;
    }
}
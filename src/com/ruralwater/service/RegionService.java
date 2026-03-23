package com.ruralwater.service;

import com.ruralwater.dao.RegionDAO;
import com.ruralwater.entity.Region;

import java.util.List;

/**
 * 区域服务层
 */
public class RegionService {
    
    private RegionDAO regionDAO = new RegionDAO();
    
    /**
     * 根据层级查询区域
     */
    public List<Region> getRegionsByLevel(String level) throws Exception {
        return regionDAO.findByLevel(level);
    }
    
    /**
     * 根据父 ID 查询子区域
     */
    public List<Region> getChildRegions(Integer parentId) throws Exception {
        return regionDAO.findByParentId(parentId);
    }
    
    /**
     * 获取所有省份
     */
    public List<Region> getAllProvinces() throws Exception {
        return regionDAO.findByLevel("province");
    }
    
    /**
     * 获取指定省份下的所有城市
     */
    public List<Region> getCitiesByProvince(Integer provinceId) throws Exception {
        return regionDAO.findByParentId(provinceId);
    }
    
    /**
     * 关键字搜索区域
     */
    public List<Region> searchRegions(String keyword) throws Exception {
        if (keyword == null || keyword.trim().isEmpty()) {
            return regionDAO.findAll();
        }
        return regionDAO.findByKeyword(keyword);
    }
    
    /**
     * 获取区域详情
     */
    public Region getRegionById(Integer regionId) throws Exception {
        if (regionId == null || regionId <= 0) {
            throw new IllegalArgumentException("区域 ID 无效");
        }
        return regionDAO.findById(regionId);
    }
    
    /**
     * 添加区域
     */
    public void addRegion(Region region) throws Exception {
        if (region == null) {
            throw new IllegalArgumentException("区域信息不能为空");
        }
        
        // 验证必填字段
        if (region.getRegionCode() == null || region.getRegionCode().trim().isEmpty()) {
            throw new IllegalArgumentException("区域编码不能为空");
        }
        if (region.getRegionName() == null || region.getRegionName().trim().isEmpty()) {
            throw new IllegalArgumentException("区域名称不能为空");
        }
        if (region.getLevel() == null || region.getLevel().trim().isEmpty()) {
            throw new IllegalArgumentException("区域级别不能为空");
        }
        
        // 自动生成 full_path
        generateFullPath(region);
        
        regionDAO.save(region);
    }
    
    /**
     * 更新区域
     */
    public void updateRegion(Region region) throws Exception {
        if (region == null || region.getRegionId() == null) {
            throw new IllegalArgumentException("区域信息无效");
        }
        
        // 验证必填字段
        if (region.getRegionCode() == null || region.getRegionCode().trim().isEmpty()) {
            throw new IllegalArgumentException("区域编码不能为空");
        }
        if (region.getRegionName() == null || region.getRegionName().trim().isEmpty()) {
            throw new IllegalArgumentException("区域名称不能为空");
        }
        if (region.getLevel() == null || region.getLevel().trim().isEmpty()) {
            throw new IllegalArgumentException("区域级别不能为空");
        }
        
        // 重新生成 full_path
        generateFullPath(region);
        
        regionDAO.update(region);
    }
    
    /**
     * 删除区域
     */
    public void deleteRegion(Integer regionId) throws Exception {
        if (regionId == null || regionId <= 0) {
            throw new IllegalArgumentException("区域 ID 无效");
        }
        
        // 检查是否有子区域
        List<Region> children = regionDAO.findByParentId(regionId);
        if (!children.isEmpty()) {
            throw new IllegalStateException("该区域下还有子区域，无法删除");
        }
        
        regionDAO.delete(regionId);
    }
    
    /**
     * 生成完整路径
     */
    private void generateFullPath(Region region) throws Exception {
        StringBuilder fullPath = new StringBuilder(region.getRegionName());
        
        Integer parentId = region.getParentId();
        if (parentId != null && parentId > 0) {
            Region parent = regionDAO.findById(parentId);
            if (parent != null) {
                String parentPath = parent.getFullPath();
                if (parentPath != null && !parentPath.isEmpty()) {
                    fullPath.insert(0, parentPath + "/");
                }
            }
        }
        
        region.setFullPath(fullPath.toString());
    }
    
    /**
     * 获取区域总数
     */
    public int getTotalCount() throws Exception {
        return regionDAO.getCount();
    }
}

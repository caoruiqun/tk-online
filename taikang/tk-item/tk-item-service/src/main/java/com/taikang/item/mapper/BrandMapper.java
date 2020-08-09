package com.taikang.item.mapper;

import com.taikang.item.pojo.Brand;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author CaoRuiqun on 2020/4/9
 */
@org.apache.ibatis.annotations.Mapper
public interface BrandMapper extends Mapper<Brand>, IdListMapper<Brand,Long> {

    @Insert("INSERT INTO tb_category_brand (category_id, brand_id) VALUES (#{cid},#{bid})")
    int insertCategoryBrand(@Param("cid") Long cid,@Param("bid") Long id);

    /**
     * 根据category id查询brand,左连接
     * @param cid
     * @return
     */
    @Select("SELECT b.id,b.name,b.letter,b.image FROM tb_brand b LEFT JOIN tb_category_brand cb ON b.id=cb.brand_id WHERE cb.category_id=#{cid}")
    List<Brand> selectBrandByCategoryId(@Param("cid") Long cid);
}

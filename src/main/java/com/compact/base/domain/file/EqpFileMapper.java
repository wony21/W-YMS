package com.compact.base.domain.file;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.compact.base.domain.file.dto.FileList;
import com.compact.base.domain.file.dto.YMS_FILEINDEX_10Y;

@Mapper
public interface EqpFileMapper {
	
	List<FileList> getFileList(Map<String, Object> parameter);
	List<YMS_FILEINDEX_10Y> getFile10Y(Map<String, Object> parameter);
}

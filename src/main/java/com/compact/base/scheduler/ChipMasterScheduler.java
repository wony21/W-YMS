package com.compact.base.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.compact.base.domain.chipmaster.file.FileBulkParserService;


import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ChipMasterScheduler {
	
	@Autowired
	private FileBulkParserService bulkParserService;
	
	public Boolean BulkRun = false;
	
	@Value("${sch.ctq.daily.cron.enable:true}")
	public Boolean isEnabled;
	
	@Scheduled(cron = "${sch.master.bulk.cron}")
	public void Bulk() throws Exception {
		
		if( !isEnabled ) return;
		
		if (BulkRun) return;
		
		BulkRun = true;
		
		try {
			
			log.info("<<<-- MASTER FILE BULK SCHEDULER START -->>>");
			
			bulkParserService.moveFromUploadToWork();
			log.info("Target files move to work directory.");
			
			bulkParserService.unZipFiles();
			log.info("Unzip files.");
			
			bulkParserService.DecryptDRM();
			log.info("Decrpting DRM.");
			
			bulkParserService.DBInsert();
			log.info("Insert file to Db.");
			
			bulkParserService.ClearWorkDirectory();
			log.info("Clear work directory.");
			
			log.info("<<<-- MASTER FILE BULK SCHEDULER END -->>>");
			
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		
		BulkRun = false;
		
	}

}

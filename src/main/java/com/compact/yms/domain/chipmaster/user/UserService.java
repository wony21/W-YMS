package com.compact.yms.domain.chipmaster.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.DefaultParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import oracle.jdbc.internal.OracleConnection.TransactionState;


@Service
public class UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	UserMapper mapper;
	
	@Autowired
	DataSourceTransactionManager transactionManager;
	
	public int AddUser(List<User> userInfo) throws Exception {
		
		DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
		txDefinition.setName("transaction-x");
		txDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager.getTransaction(txDefinition);
		
		try {
			
			for(User user : userInfo) {
				mapper.AddUser(user);
			}

		} catch(Exception e) {
			
			transactionManager.rollback(status);
		}
		transactionManager.commit(status);
		
		
		
		return 0;
	}
	
}
